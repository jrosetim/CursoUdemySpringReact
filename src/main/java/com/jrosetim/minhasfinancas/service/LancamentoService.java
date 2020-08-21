package com.jrosetim.minhasfinancas.service;

import com.jrosetim.minhasfinancas.enums.StatusLancamento;
import com.jrosetim.minhasfinancas.model.LancamentosModel;

import java.util.List;
import java.util.Optional;

public interface LancamentoService {

    LancamentosModel salvar(LancamentosModel lancamentosModel);

    LancamentosModel atualizar(LancamentosModel lancamentosModel);

    void deletar(LancamentosModel lancamentosModel);

    List<LancamentosModel> buscar( LancamentosModel lancamentosFiltro);

    void atualizarStatus(LancamentosModel lancamentosModel, StatusLancamento statusLancamento);

    void validar(LancamentosModel lancamentosModel);

    Optional<LancamentosModel> obterPorId(Long id);
}
