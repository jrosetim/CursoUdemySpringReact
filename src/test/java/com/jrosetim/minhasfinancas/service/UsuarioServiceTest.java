package com.jrosetim.minhasfinancas.service;

import java.util.Optional;

import com.jrosetim.minhasfinancas.exception.AutenticacaoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.jrosetim.minhasfinancas.exception.RegraNegocioException;
import com.jrosetim.minhasfinancas.model.UsuarioModel;
import com.jrosetim.minhasfinancas.repository.UsuarioRepository;
import com.jrosetim.minhasfinancas.service.impl.UsuarioServiceImpl;
import org.springframework.ui.ModelExtensionsKt;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class UsuarioServiceTest {
	
	private String emailLocal = "usuario@email.com";
	private String senhaLocal = "senha";
	
	@MockBean
	private UsuarioRepository usuarioRepository;

	@SpyBean
	private UsuarioServiceImpl usuarioService;

	public static UsuarioModel getUsuarioTest() {
		return UsuarioModel
				.builder()
				.nome("usuario")
				.email("usuario@email.com")
				.senha("senha")
				.build();
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
		UsuarioModel usuario = getUsuarioTest();
		Mockito.when(usuarioRepository.findByEmail(emailLocal)).thenReturn(Optional.of(usuario));
		
		UsuarioModel result = usuarioService.autenticar(emailLocal, senhaLocal);
		
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void autenticacaoErroUsuarioNaoEncontrado() {
		//pega a exception de retorno para validar se caiu na msg correta
		AutenticacaoException exceptionLocal =  Assertions.assertThrows(AutenticacaoException.class, () -> {
			Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

			usuarioService.autenticar(emailLocal, senhaLocal);
		});

		Assertions.assertTrue(exceptionLocal.getMessage().contains("Usuário não encontrado"));
	}

	@Test
	public void autenticacaoErroSenhaIncorreta(){
		//pega a exception de retorno para validar se caiu na msg correta
		AutenticacaoException exception = Assertions.assertThrows(AutenticacaoException.class, () -> {

			UsuarioModel usuario = getUsuarioTest();
			Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

			usuarioService.autenticar(emailLocal, "1234");
		});

		Assertions.assertTrue(exception.getMessage().contains("Senha incorreta"));
	}


	@Test
	public void salvarUsuarioComSucesso(){
		Mockito.doNothing().when(usuarioService).validarEmail(Mockito.anyString());

		UsuarioModel usuario = getUsuarioTest();
		usuario.setId(1l);

		Mockito.when(usuarioRepository.save(Mockito.any(UsuarioModel.class))).thenReturn(usuario);

		UsuarioModel usuarioSalvo = usuarioService.salvarUsuario(usuario);

		Assertions.assertNotNull(usuarioSalvo);
		Assertions.assertEquals(1, usuarioSalvo.getId());
		Assertions.assertEquals(emailLocal, usuarioSalvo.getEmail());
		Assertions.assertEquals(senhaLocal, usuarioSalvo.getSenha());
		Assertions.assertEquals("usuario", usuarioSalvo.getNome());
	}

	public void salvarUsuarioErroEmailJaCadastrado(){
		UsuarioModel usuario = getUsuarioTest();

		// espera que retorne uma excpetion do tipo RegraNegocioException
		Mockito.doThrow(RegraNegocioException.class).when(usuarioService).validarEmail(emailLocal);

		usuarioService.salvarUsuario(usuario);

		// verifica que o save do método nunca foi chamado, pois cai no exception acima
		Mockito.verify(usuarioRepository, Mockito.never()).save(usuario);
	}
}
