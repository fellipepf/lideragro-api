package com.lideragro.api.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
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
import com.lideragro.api.model.Categoria;
import com.lideragro.api.model.Produto;
import com.lideragro.api.repository.ProdutoRepository;
import com.lideragro.api.service.ProdutoService;

@RestController
@RequestMapping("/produto")
public class ProdutoResource {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;   //dispara o evento
	
	@Autowired
	private ProdutoService produtoService;
	
	@GetMapping
	public List<Produto> listar(){
		return produtoRepository.findAll();
		
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
		Produto produtoSalvo = produtoRepository.getOne(id);
		BeanUtils.copyProperties(produto, produtoSalvo, "id");
		produtoRepository.save(produtoSalvo);
		
		return ResponseEntity.ok(produtoSalvo);
		
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		produtoRepository.deleteById(id);
	}
	
	@PutMapping("{/{id}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarPropriedadeAtivo(@PathVariable Long id, @RequestBody Boolean ativo){
		produtoService.atualizarPropriedadeAtivo(id, ativo);
	
	}
}
