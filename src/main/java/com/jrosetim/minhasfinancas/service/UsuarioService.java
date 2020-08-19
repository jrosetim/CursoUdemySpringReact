package com.jrosetim.minhasfinancas.service;

import com.jrosetim.minhasfinancas.model.UsuarioModel;

public interface UsuarioService {

	UsuarioModel autenticar(String email, String senha);
	
	UsuarioModel salvarUsuario(UsuarioModel usuarioModel);
	
	void validarEmail(String email);
}
