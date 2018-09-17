package com.lideragro.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "produto")
public class Produto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_produto")
	private Long id;
	
	@Column(name = "codigo_barras")
	private Long codigoBarras;
	
	@ManyToOne
	@JoinColumn(name = "id_categoria")
	private Categoria categoria;
	
	@Column(name = "id_subcategoria")
	private Long subCategoria;
	
	@Column(name = "id_fabricante")
	private Long fabricante;
	
	@Column(name = "id_fornecedor")
	private Long fornecedor;
	
	@ManyToOne
	@JoinColumn(name = "id_unidade_medida")
	private UnidadeMedida unidadeMedida;
	

	@Column(name = "ativo")
	private Boolean ativo;
	
	@Column(name = "observacoes")
	private String observacoes;
	
	@NotNull
	@Size(min = 3, max = 40)
	@Column(name = "nome_produto")
	private String nome;
	
	@Column(name = "preco_custo")
	private Double precoCusto;
	
	@NotNull
	@Column(name = "preco_venda")
	private Double precoVenda;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(Long codigoBarras) {
		this.codigoBarras = codigoBarras;
	}


	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Long getSubCategoria() {
		return subCategoria;
	}

	public void setSubCategoria(Long subCategoria) {
		this.subCategoria = subCategoria;
	}

	public Long getFabricante() {
		return fabricante;
	}

	public void setFabricante(Long fabricante) {
		this.fabricante = fabricante;
	}

	public Long getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Long fornecedor) {
		this.fornecedor = fornecedor;
	}

	public UnidadeMedida getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Double getPrecoCusto() {
		return precoCusto;
	}

	public void setPrecoCusto(Double precoCusto) {
		this.precoCusto = precoCusto;
	}

	public Double getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(Double precoVenda) {
		this.precoVenda = precoVenda;
	}
	
	
}
