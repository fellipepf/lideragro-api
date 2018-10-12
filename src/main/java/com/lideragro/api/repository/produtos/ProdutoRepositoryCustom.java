package com.lideragro.api.repository.produtos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lideragro.api.model.Produto;
import com.lideragro.api.repository.filter.ProdutoFilter;

public interface ProdutoRepositoryCustom {
	
	public Page<Produto> filtroProduto(ProdutoFilter produtoFilter, Pageable pageable);

}
