package com.jrosetim.minhasfinancas.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//import com.jrosetim.minhasfinancas.exception.RegraNegocioException;
import com.jrosetim.minhasfinancas.model.UsuarioModel;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {
	
	private String EMAIL = "usuario@email.com";
	private String USUARIO = "usuario";
	private String SENHA = "senha";

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	TestEntityManager testEntityManager;

	public static UsuarioModel getUsuarioTest() {
		return UsuarioModel
				.builder()
				.nome("usuario")
				.email("usuario@email.com")
				.senha("senha")
				.build();		
	}	
	
	@Test
	public void emailExistente() {
		UsuarioModel usuario = getUsuarioTest();
		testEntityManager.persist(usuario);
		
		boolean result = usuarioRepository.existsByEmail(EMAIL);
		
		
		Assertions.assertThat(result).isTrue();		
	}
	
	@Test
	public void emailNaoExiste() {	
		boolean result = usuarioRepository.existsByEmail(EMAIL);
		
		Assertions.assertThat(result).isFalse();
	}
	
	@Test
	public void salvandoUsuarioComSucesso() {
		UsuarioModel usuario = getUsuarioTest();
		
		UsuarioModel usuarioSalvo = usuarioRepository.save(usuario);
		
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();		
	}
	
	@Test
	public void getUsuarioByEmailCadastrado() {
		UsuarioModel usuario = getUsuarioTest();
		
		usuarioRepository.save(usuario);
		
		Optional<UsuarioModel> result = usuarioRepository.findByEmail(EMAIL);	
		
		Assertions.assertThat(result.isPresent()).isTrue();
	}

	
	@Test
	public void getUsuarioByEmailnaoCadastrado() {
		Optional<UsuarioModel> result = usuarioRepository.findByEmail(EMAIL);	
		
		Assertions.assertThat(result.isPresent()).isFalse();
	}
	
	
}


