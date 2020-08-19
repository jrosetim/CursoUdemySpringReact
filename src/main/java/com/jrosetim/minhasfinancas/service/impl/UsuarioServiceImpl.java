package com.jrosetim.minhasfinancas.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jrosetim.minhasfinancas.exception.AutenticacaoException;
import com.jrosetim.minhasfinancas.exception.RegraNegocioException;
import com.jrosetim.minhasfinancas.model.UsuarioModel;
import com.jrosetim.minhasfinancas.repository.UsuarioRepository;
import com.jrosetim.minhasfinancas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
		super();
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public UsuarioModel autenticar(String email, String senha) {
		Optional<UsuarioModel> usuario = usuarioRepository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new AutenticacaoException("Usuário não encontrado");
		}
		
		if (!usuario.get().getSenha().equals(senha)) {
			throw new AutenticacaoException("Senha incorreta");
		}
		
		return usuario.get();
	}

	@Override
	@Transactional
	public UsuarioModel salvarUsuario(UsuarioModel usuarioModel) {
		validarEmail(usuarioModel.getEmail());
		
		return usuarioRepository.save(usuarioModel);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe  = usuarioRepository.existsByEmail(email);
		
		if(existe) {
			throw new RegraNegocioException("Já existe usuário cadastrado com este email");
		}
		
	}

}
