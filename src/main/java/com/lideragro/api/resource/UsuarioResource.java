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
import com.lideragro.api.model.Departamento;
import com.lideragro.api.model.Fabricante;
import com.lideragro.api.model.Produto;
import com.lideragro.api.model.Usuario;
import com.lideragro.api.repository.FabricanteRepository;
import com.lideragro.api.repository.UsuarioRepository;
import com.lideragro.api.security.util.GeradorSenha;

@RestController
@RequestMapping("/usuario")
public class UsuarioResource {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;   //dispara o evento
	
	@GetMapping
	public List<Usuario> listar(){
		return usuarioRepository.findAll();
		
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)   //exibir status 201 created
	public ResponseEntity<Usuario> criar(@Valid @RequestBody Usuario usuario, HttpServletResponse response) {
		
		usuario.setSenha(GeradorSenha.passwordEncode(usuario.getSenha()));
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, usuarioSalvo.getCodigo()));
		
		//para que apos a criacao exibir o item criado como resposta
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		usuarioRepository.deleteById(id);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @Valid @RequestBody Usuario usuario){
		Usuario usuarioSalvo = usuarioRepository.getOne(id);
		BeanUtils.copyProperties(usuario, usuarioSalvo, "codigo");
		usuarioRepository.save(usuarioSalvo);
		
		return ResponseEntity.ok(usuarioSalvo);
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Optional<Usuario>> buscarPorId(@PathVariable Long id) {

		Optional<Usuario> usuario = usuarioRepository.findById(id);
		
		return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
		//return categoriaRepository.findById(id);
	}
}
