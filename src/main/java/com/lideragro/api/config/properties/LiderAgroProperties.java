package com.lideragro.api.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("lideragro")
public class LiderAgroProperties {
	
	private String origemPermitida = "http://localhost:4200"; 
	
	private final Seguranca seguranca = new Seguranca();
	
	
	public Seguranca getSeguranca() {
		return seguranca;
	}

	
	
	public String getOrigemPermitida() {
		return origemPermitida;
	}
	
	public void setOrigemPermitida(String origemPermitida) {
		this.origemPermitida = origemPermitida;
	}


	public static class Seguranca {
		
		private boolean enableHttps;

		public void setEnableHttps(boolean enableHttps) {
			this.enableHttps = enableHttps;
		}

		public boolean isEnableHttps() {
			return enableHttps;
		}
		
	}

}
