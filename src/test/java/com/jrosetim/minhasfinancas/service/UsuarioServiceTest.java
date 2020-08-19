package com.jrosetim.minhasfinancas.service;


import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.jrosetim.minhasfinancas.exception.RegraNegocioException;
import com.jrosetim.minhasfinancas.model.UsuarioModel;
import com.jrosetim.minhasfinancas.repository.UsuarioRepository;
import com.jrosetim.minhasfinancas.service.impl.UsuarioServiceImpl;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class UsuarioServiceTest {
	
	private String emailLocal = "usuario@email.com";
	private String senhaLocal = "senha";
	
	@MockBean
	private UsuarioRepository usuarioRepository;
	
	private UsuarioService usuarioService;
	
	@BeforeEach
	public void setUp() {
		usuarioService = new UsuarioServiceImpl(usuarioRepository);
	}
	
	
	@Test
	public void emailNaoExiste() {
		Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		usuarioService.validarEmail(emailLocal);
	}
	
	@Test
	public void emailExistente() {
		Assertions.assertThrows(RegraNegocioException.class, () -> {
			Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(true);
			
			usuarioService.validarEmail(emailLocal);			
		});
	}
	
	@Test
	public void autenticadoComSucesso() {
		UsuarioModel usuario = UsuarioModel.builder().email(emailLocal).senha(senhaLocal).id(1l).build();
		Mockito.when(usuarioRepository.findByEmail(emailLocal)).thenReturn(Optional.of(usuario));
		
		UsuarioModel result = usuarioService.autenticar(emailLocal, senhaLocal);
		
		Assertions.assertNotNull(result);
	}
	
//	@Test
//	public void autenticacaoErroUsuarioNaoEncontrado() {
//		Assertions.assertThrows(RegraNegocioException.class, () -> {
//			Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
//			
//			usuarioService.autenticar(emailLocal, senhaLocal);
//		});
//	}

	@Test
	public void autenticacaoErroUsuarioNaoEncontrado() {
		Exception exceptionLocal =  Assertions.assertThrows(RegraNegocioException.class, () -> {
			Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
			
			usuarioService.autenticar(emailLocal, senhaLocal);
		});
		 
		Assertions.assertTrue(exceptionLocal.getMessage().contains("Usuário não encontrado"));
	}	
	

}
