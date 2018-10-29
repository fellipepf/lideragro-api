package com.lideragro.api.resource;

import java.util.List;
import java.util.Optional;

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
import com.lideragro.api.model.Fornecedor;
import com.lideragro.api.repository.FornecedorRepository;

@RestController
@RequestMapping("/fornecedor")
public class FornecedorResource {
	
	@Autowired
	private FornecedorRepository fornecedorRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;   //dispara o evento
	
	@GetMapping
	public List<Fornecedor> listar(){
		return fornecedorRepository.findAll();
		
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)   //exibir status 201 created
	public ResponseEntity<Fornecedor> criar(@Valid @RequestBody Fornecedor fornecedor, HttpServletResponse response) {
		Fornecedor fornecedorSalvo = fornecedorRepository.save(fornecedor);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, fornecedorSalvo.getId()));
		
		//para que apos a criacao exibir o item criado como resposta
		return ResponseEntity.status(HttpStatus.CREATED).body(fornecedorSalvo);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Optional<Fornecedor>> buscarPorId(@PathVariable Long id) {

		Optional<Fornecedor> fornecedor = fornecedorRepository.findById(id);
		
		return fornecedor != null ? ResponseEntity.ok(fornecedor) : ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Fornecedor> atualizar(@PathVariable Long id, @Valid @RequestBody Fornecedor fornecedor){
		Fornecedor fornecedorSalvo = fornecedorRepository.getOne(id);
		BeanUtils.copyProperties(fornecedor, fornecedorSalvo, "id");
		fornecedorRepository.save(fornecedorSalvo);
		
		return ResponseEntity.ok(fornecedorSalvo);
		
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		fornecedorRepository.deleteById(id);
	}
}
