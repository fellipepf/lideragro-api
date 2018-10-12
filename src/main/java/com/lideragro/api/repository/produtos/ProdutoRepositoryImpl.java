package com.lideragro.api.repository.produtos;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lideragro.api.model.Produto;
import com.lideragro.api.repository.filter.ProdutoFilter;

@Component
public class ProdutoRepositoryImpl implements ProdutoRepositoryCustom{
	
	@PersistenceContext
	private EntityManager manager;
	

	@Override
	public List<Produto> filtroProduto(ProdutoFilter produtoFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Produto> criteria = builder.createQuery(Produto.class);
		Root<Produto> root = criteria.from(Produto.class);
		
		Predicate[] predicates = criarRestricoes(produtoFilter,builder,root);
		criteria.where(predicates);
		
		TypedQuery<Produto> query = manager.createQuery(criteria);
		
		return query.getResultList();
	}

	private Predicate[] criarRestricoes(ProdutoFilter produtoFilter, CriteriaBuilder builder, Root<Produto> root) {
		List<Predicate> predicates = new ArrayList<>();
		
		if (produtoFilter.getNome() != null) {
			predicates.add(builder.like(
					builder.lower(root.get("nome")), "%"+ produtoFilter.getNome().toLowerCase() + "%"));
		}
		
		if (produtoFilter.getCodigoBarras() != null) {
			predicates.add(builder.equal(
					root.get("codigoBarras"), produtoFilter.getCodigoBarras()));
		}

		
		return predicates.toArray(new Predicate[predicates.size()]);
	}

}
