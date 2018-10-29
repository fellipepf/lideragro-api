package com.lideragro.api.service;

import java.io.InputStream;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.lideragro.api.dto.ProdutoDTO;
import com.lideragro.api.model.Produto;
import com.lideragro.api.repository.ProdutoRepository;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ProdutoService {
	
	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	public Produto atualizar(Long id, Produto produto) {
		
		Produto produtoSalvo = buscarProdutoPeloCodigo(id);
		
		BeanUtils.copyProperties(produto, produtoSalvo, "id");
		
		return produtoRepository.save(produtoSalvo);
	}
	
	public void atualizarPropriedadeAtivo(Long id, Boolean ativo) {
		Produto produtoSalvo = buscarProdutoPeloCodigo(id);
		produtoSalvo.setAtivo(ativo);
		
		produtoRepository.save(produtoSalvo);
	}
	
	private Produto buscarProdutoPeloCodigo(Long id) {
		Produto produtoSalvo = produtoRepository.getOne(id);
		
		if (produtoSalvo == null) {
			throw new EmptyResultDataAccessException(1);
		}
		return produtoSalvo;
	}
	
	public byte[] relatorioListaPreco() throws Exception {
		List<ProdutoDTO> dados = geraListaProdutos();
		System.out.println(dados);

		InputStream inputStream = this.getClass().getResourceAsStream(
				"/relatorios/TabelaPreco_LiderAgro.jasper");
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, null,
				new JRBeanCollectionDataSource(dados));
		
		return JasperExportManager.exportReportToPdf(jasperPrint);
	}
	
	private List<ProdutoDTO> geraListaProdutos() {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		
		CriteriaQuery<ProdutoDTO> criteriaQuery = criteriaBuilder.createQuery(ProdutoDTO.class);
		
		Root<Produto> root = criteriaQuery.from(Produto.class);
		criteriaQuery.select(criteriaBuilder.construct(ProdutoDTO.class, 
				root.get("id"),
				root.get("nome"),
				root.get("precoVenda")
				));
		

		
		TypedQuery<ProdutoDTO> typedQuery = manager.createQuery(criteriaQuery);
		
		return typedQuery.getResultList();
	}

}
