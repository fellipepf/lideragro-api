package com.lideragro.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lideragro.api.model.Produto;
import com.lideragro.api.repository.produtos.ProdutoRepositoryCustom;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>, ProdutoRepositoryCustom{
	

}
