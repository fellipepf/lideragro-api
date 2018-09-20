package com.lideragro.api.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
/**
 * Captura excecoes de resposta das entidades
 * compartilhada entre todos os controllers
 * @author fellipe
 *
 */
@ControllerAdvice
public class LiderAgroExceptionHandler extends ResponseEntityExceptionHandler{
	
	@Autowired
	private MessageSource messageSource;
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		String mensagemUsuario = messageSource.getMessage("mensagem.invalida", null,LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.getCause() != null ? ex.getCause().toString() : ex.toString();
		
		return handleExceptionInternal(ex, new Erro(mensagemUsuario, mensagemDesenvolvedor), headers, HttpStatus.BAD_REQUEST, request);
	}
	
	/**
	 * captura argumentos que nao estao validos
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		List<Erro> erro =  criarListaErro(ex.getBindingResult());

		return handleExceptionInternal(ex, erro, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	/**
	 * tratamento de excecao quando solicita ou acessa um recurso que nao existe
	 * 
	 * quando gerar qualquer excecao é possivel tratar adicionando a anotacao e a classe da excecao
	 */
	@ExceptionHandler({EmptyResultDataAccessException.class})
	public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request) {
		
		String mensagemUsuario = messageSource.getMessage("recurso.nao-encontrado", null,LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.getCause() != null ? ex.getCause().toString() : ex.toString();
		
		return handleExceptionInternal(ex, new Erro(mensagemUsuario, mensagemDesenvolvedor), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler({DataIntegrityViolationException.class})
	public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
		String mensagemUsuario = messageSource.getMessage("recurso.operacao-nao-permitida", null,LocaleContextHolder.getLocale());
		
		//a classe exceptionsutils da uma mensagem mais detalhada do erro
		String mensagemDesenvolvedor = ExceptionUtils.getRootCauseMessage(ex);
		
		return handleExceptionInternal(ex, new Erro(mensagemUsuario, mensagemDesenvolvedor), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	/**
	 * percorre a lista de erros e monta a mensagem para o usuario e desenvolvedor
	 * @param bindingResult
	 * @return
	 */
	private List<Erro> criarListaErro(BindingResult bindingResult){
		List<Erro> erros = new ArrayList<>();
		
		for (FieldError fieldErro : bindingResult.getFieldErrors()) {
			String mensagemUsuario = messageSource.getMessage(fieldErro, LocaleContextHolder.getLocale());
			String mensagemDesenvolvedor = fieldErro.toString();
					
			erros.add(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		}
		return erros;
	}
	
	private static class Erro {
		
		private String mensagemUsuario;
		private String mensagemDesenvolvedor;
		
		public Erro(String mensagemUsuario, String mensagemDesenvolvedor) {
			this.mensagemUsuario = mensagemUsuario;
			this.mensagemDesenvolvedor = mensagemDesenvolvedor;
		}
		public String getMensagemUsuario() {
			return mensagemUsuario;
		}
		public void setMensagemUsuario(String mensagemUsuario) {
			this.mensagemUsuario = mensagemUsuario;
		}
		public String getMensagemDesenvolvedor() {
			return mensagemDesenvolvedor;
		}
		public void setMensagemDesenvolvedor(String mensagemDesenvolvedor) {
			this.mensagemDesenvolvedor = mensagemDesenvolvedor;
		}
		
	}
}
