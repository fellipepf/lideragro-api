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
import javax.security.auth.callback.LanguageCallback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lideragro.api.model.Produto;
import com.lideragro.api.repository.filter.ProdutoFilter;
import com.lideragro.api.repository.projection.ResumoProduto;

@Component
public class ProdutoRepositoryImpl implements ProdutoRepositoryCustom{
	
	@PersistenceContext
	private EntityManager manager;
	

	@Override
	public Page<Produto> filtroProduto(ProdutoFilter produtoFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Produto> criteria = builder.createQuery(Produto.class);
		Root<Produto> root = criteria.from(Produto.class);
		
		Predicate[] predicates = criarRestricoes(produtoFilter,builder,root);
		criteria.where(predicates);
		
		TypedQuery<Produto> query = manager.createQuery(criteria);
		adicionarRestricoesDePaginacao(query,pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(produtoFilter));
	}

	private Long total(ProdutoFilter produtoFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Produto> root = criteria.from(Produto.class);
		
		Predicate[] predicates = criarRestricoes(produtoFilter, builder, root);
		criteria.select(builder.count(root));
		
		return manager.createQuery(criteria).getSingleResult();
	}

	private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroPagina = paginaAtual * totalRegistrosPorPagina;
		
		query.setFirstResult(primeiroRegistroPagina);
		query.setMaxResults(totalRegistrosPorPagina);
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
	
	@Override
	public Page<ResumoProduto> resumir(ProdutoFilter produtoFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<ResumoProduto> criteria = builder.createQuery(ResumoProduto.class);
		Root<Produto> root = criteria.from(Produto.class);
		
		criteria.select(builder.construct(ResumoProduto.class, 
				root.get("id"),
				root.get("codigoBarras"),
				root.get("unidadeMedida").get("nome"),
				root.get("ativo"),
				root.get("nome"),
				root.get("precoVenda")));
		
		Predicate[] predicates = criarRestricoes(produtoFilter,builder,root);
		criteria.where(predicates);
		
		TypedQuery<ResumoProduto> query = manager.createQuery(criteria);
		adicionarRestricoesDePaginacao(query,pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(produtoFilter));
	}
}
