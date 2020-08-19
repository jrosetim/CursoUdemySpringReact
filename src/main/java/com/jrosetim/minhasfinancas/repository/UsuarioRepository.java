package com.jrosetim.minhasfinancas.repository;

import java.util.Optional;

//import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jrosetim.minhasfinancas.model.UsuarioModel;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

//	Optional<UsuarioModel> findByEmail(String email);
	
	boolean existsByEmail(String email);
	
	Optional<UsuarioModel> findByEmail(String email);
}
