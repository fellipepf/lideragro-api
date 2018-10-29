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
import com.lideragro.api.repository.CategoriaRepository;

@RestController
@RequestMapping("/categoria")
public class CategoriaResource {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;   //dispara o evento
	
	@GetMapping
	public List<Categoria> listar(){
		return categoriaRepository.findAll();
		
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)   //exibir status 201 created
	public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
		Categoria categoriaSalva = categoriaRepository.save(categoria);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalva.getId()));
		
		//para que apos a criacao exibir o item criado como resposta
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Optional<Categoria>> buscarPorId(@PathVariable Long id) {
	    /*return Optional
	            .ofNullable( categoriaRepository.findById(id) )
	            .map( categoria -> ResponseEntity.ok().body(categoria) )          //200 OK
	            .orElseGet( () -> ResponseEntity.notFound().build() );  //404 Not found
		*/
		Optional<Categoria> categoria = categoriaRepository.findById(id);
		
		return categoria != null ? ResponseEntity.ok(categoria) : ResponseEntity.notFound().build();
		//return categoriaRepository.findById(id);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Categoria> atualizar(@PathVariable Long id, @Valid @RequestBody Categoria categoria){
		Categoria categoriaSalva = categoriaRepository.getOne(id);
		BeanUtils.copyProperties(categoria, categoriaSalva, "id");
		categoriaRepository.save(categoriaSalva);
		
		return ResponseEntity.ok(categoriaSalva);
		
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		categoriaRepository.deleteById(id);
	}
}