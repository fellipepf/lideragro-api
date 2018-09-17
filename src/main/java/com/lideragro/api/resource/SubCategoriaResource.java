package com.lideragro.api.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lideragro.api.model.SubCategoria;
import com.lideragro.api.repository.SubCategoriaRepository;

@RestController
@RequestMapping("/subcategoria")
public class SubCategoriaResource {
	
	@Autowired
	private SubCategoriaRepository subCategoriaRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;   //dispara o evento
	
	@GetMapping
	public List<SubCategoria> listar(){
		return subCategoriaRepository.findAll();
		
	}
	
}
