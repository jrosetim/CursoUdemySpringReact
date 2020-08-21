package com.jrosetim.minhasfinancas.service;

import com.jrosetim.minhasfinancas.model.UsuarioModel;

import java.util.Optional;

public interface UsuarioService {

	UsuarioModel autenticar(String email, String senha);
	
	UsuarioModel salvarUsuario(UsuarioModel usuarioModel);
	
	void validarEmail(String email);

	Optional<UsuarioModel> obertPorId(Long id);
}
