package com.lideragro.api.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lideragro.api.events.RecursoCriadoEvent;
import com.lideragro.api.model.Produto;
import com.lideragro.api.repository.ProdutoRepository;
import com.lideragro.api.repository.filter.ProdutoFilter;
import com.lideragro.api.repository.projection.ResumoProduto;
import com.lideragro.api.service.ProdutoService;

//@CrossOrigin(maxAge = 10)

@RestController
@RequestMapping("/produto")
public class ProdutoResource {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;   //dispara o evento
	
	@Autowired
	private ProdutoService produtoService;
	
/*
	public List<Produto> listar(){
		return produtoRepository.findAll();
		
	}
	*/
	
	@GetMapping
	public Page<Produto> pesquisar(ProdutoFilter produtoFilter, Pageable pageable){
		return produtoRepository.filtroProduto(produtoFilter, pageable);
	}
	
	@GetMapping(params = "resumo")
	public Page<ResumoProduto> resumir(ProdutoFilter produtoFilter, Pageable pageable){
		return produtoRepository.resumir(produtoFilter, pageable);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {

		Produto produto = produtoRepository.getOne(id);
		
		return produto != null ? ResponseEntity.ok(produto) : ResponseEntity.notFound().build();

	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)   //exibir status 201 created
	public ResponseEntity<Produto> criar(@Valid @RequestBody Produto produto, HttpServletResponse response) {
		Produto produtoSalvo = produtoRepository.save(produto);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, produtoSalvo.getId()));
		
		//para que apos a criacao exibir o item criado como resposta
		return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Produto> atualizar(@PathVariable Long id, @Valid @RequestBody Produto produto){
		Produto produtoSalvo = produtoService.atualizar(id, produto);

		return ResponseEntity.ok(produtoSalvo);
		
	}
	
	@PutMapping("/{id}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarPropriedadeAtivo(@PathVariable Long id, @RequestBody Boolean ativo){
		produtoService.atualizarPropriedadeAtivo(id, ativo);
	
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		produtoRepository.deleteById(id);
	}
	
	@GetMapping("/relatorio")
	public ResponseEntity<byte[]>  relatorio() throws Exception {

		byte[] relatorio = produtoService.relatorioListaPreco();
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.body(relatorio);
	}
	

}
