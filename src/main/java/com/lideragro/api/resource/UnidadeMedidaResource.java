package com.lideragro.api.resource;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lideragro.api.events.RecursoCriadoEvent;
import com.lideragro.api.model.UnidadeMedida;
import com.lideragro.api.repository.UnidadeMedidaRepository;

@RestController
@RequestMapping("/unidade")
public class UnidadeMedidaResource {
	@Autowired
	private UnidadeMedidaRepository unidadeMedidaRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;   //dispara o evento
	
	@GetMapping
	public List<UnidadeMedida> listar(){
		return unidadeMedidaRepository.findAll();
		
	}
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)   //exibir status 201 created
	public ResponseEntity<UnidadeMedida> criar(@Valid @RequestBody UnidadeMedida unidadeMedida, HttpServletResponse response) {
		UnidadeMedida unidadeMedidaSalva = unidadeMedidaRepository.save(unidadeMedida);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, unidadeMedidaSalva.getId()));
		
		//para que apos a criacao exibir o item criado como resposta
		return ResponseEntity.status(HttpStatus.CREATED).body(unidadeMedidaSalva);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Optional<UnidadeMedida>> buscarPorId(@PathVariable Long id) {

		Optional<UnidadeMedida> unidadeMedida = unidadeMedidaRepository.findById(id);
		
		return unidadeMedida != null ? ResponseEntity.ok(unidadeMedida) : ResponseEntity.notFound().build();

	}
}
