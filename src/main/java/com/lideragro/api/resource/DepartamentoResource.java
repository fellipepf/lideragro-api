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
import com.lideragro.api.model.Categoria;
import com.lideragro.api.model.Departamento;
import com.lideragro.api.repository.DepartamentoRepository;

@RestController
@RequestMapping("/departamento")
public class DepartamentoResource {
	
	@Autowired
	private DepartamentoRepository departamentoRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;   //dispara o evento
	
	@GetMapping
	public List<Departamento> listar(){
		return departamentoRepository.findAll();
		
	}
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)   //exibir status 201 created
	public ResponseEntity<Departamento> criar(@Valid @RequestBody Departamento departamento, HttpServletResponse response) {
		Departamento categoriaSalva = departamentoRepository.save(departamento);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalva.getId()));
		
		//para que apos a criacao exibir o item criado como resposta
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Optional<Departamento>> buscarPorId(@PathVariable Long id) {
	    /*return Optional
	            .ofNullable( categoriaRepository.findById(id) )
	            .map( categoria -> ResponseEntity.ok().body(categoria) )          //200 OK
	            .orElseGet( () -> ResponseEntity.notFound().build() );  //404 Not found
		*/
		Optional<Departamento> departamento = departamentoRepository.findById(id);
		
		return departamento != null ? ResponseEntity.ok(departamento) : ResponseEntity.notFound().build();
		//return categoriaRepository.findById(id);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Departamento> atualizar(@PathVariable Long id, @Valid @RequestBody Departamento departamento){
		Departamento departamentoSalvo = departamentoRepository.getOne(id);
		BeanUtils.copyProperties(departamento, departamentoSalvo, "id");
		departamentoRepository.save(departamentoSalvo);
		
		return ResponseEntity.ok(departamentoSalvo);
		
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		departamentoRepository.deleteById(id);
	}
}
