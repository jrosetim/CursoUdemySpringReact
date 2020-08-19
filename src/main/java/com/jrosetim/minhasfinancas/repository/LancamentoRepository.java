package com.jrosetim.minhasfinancas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jrosetim.minhasfinancas.model.LancamentosModel;

public interface LancamentoRepository extends JpaRepository<LancamentosModel, Long>{

}
