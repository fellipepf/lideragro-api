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
import com.lideragro.api.model.Fabricante;
import com.lideragro.api.repository.FabricanteRepository;

@RestController
@RequestMapping("/fabricante")
public class FabricanteResource {
	
	@Autowired
	private FabricanteRepository fabricanteRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;   //dispara o evento
	
	@GetMapping
	public List<Fabricante> listar(){
		return fabricanteRepository.findAll();
		
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)   //exibir status 201 created
	public ResponseEntity<Fabricante> criar(@Valid @RequestBody Fabricante fabricante, HttpServletResponse response) {
		Fabricante fabricanteSalvo = fabricanteRepository.save(fabricante);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, fabricanteSalvo.getId()));
		
		//para que apos a criacao exibir o item criado como resposta
		return ResponseEntity.status(HttpStatus.CREATED).body(fabricanteSalvo);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Optional<Fabricante>> buscarPorId(@PathVariable Long id) {

		Optional<Fabricante> fabricante = fabricanteRepository.findById(id);
		
		return fabricante != null ? ResponseEntity.ok(fabricante) : ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Fabricante> atualizar(@PathVariable Long id, @Valid @RequestBody Fabricante fabricante){
		Fabricante fabricanteSalvo = fabricanteRepository.getOne(id);
		BeanUtils.copyProperties(fabricante, fabricanteSalvo, "id");
		fabricanteRepository.save(fabricanteSalvo);
		
		return ResponseEntity.ok(fabricanteSalvo);
		
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		fabricanteRepository.deleteById(id);
	}
}
