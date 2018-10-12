package com.lideragro.api.repository.projection;

public class ResumoProduto {
	
	private Long id;
	private Long codigoBarras;
	private String unidadeMedida;
	private Boolean ativo;
	private String nome;
	private Double preco;
	
	public ResumoProduto(Long id, Long codigoBarras, String unidadeMedida, Boolean ativo, String nome,
			Double preco) {
		this.id = id;
		this.codigoBarras = codigoBarras;
		this.unidadeMedida = unidadeMedida;
		this.ativo = ativo;
		this.nome = nome;
		this.preco = preco;
	}
	

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
	public String getUnidadeMedida() {
		return unidadeMedida;
	}
	public void setUnidadeMedida(String unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}
	public Boolean getAtivo() {
		return ativo;
	}
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Double getPreco() {
		return preco;
	}
	public void setPreco(Double preco) {
		this.preco = preco;
	}

}
