package com.jrosetim.minhasfinancas.service.impl;

import com.jrosetim.minhasfinancas.enums.StatusLancamento;
import com.jrosetim.minhasfinancas.exception.RegraNegocioException;
import com.jrosetim.minhasfinancas.model.LancamentosModel;
import com.jrosetim.minhasfinancas.repository.LancamentoRepository;
import com.jrosetim.minhasfinancas.service.LancamentoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LancamentoServiceImpl implements LancamentoService {

    @Autowired
    private LancamentoRepository repository;

    @Override
    @Transactional
    public LancamentosModel salvar(LancamentosModel lancamentosModel) {
        validar(lancamentosModel);

        lancamentosModel.setStatus(StatusLancamento.PENDENTE);
        return repository.save(lancamentosModel);
    }

    @Override
    @Transactional
    public LancamentosModel atualizar(LancamentosModel lancamentosModel) {
        Objects.requireNonNull(lancamentosModel.getId());

        validar(lancamentosModel);

        return repository.save(lancamentosModel);
    }

    @Override
    @Transactional
    public void deletar(LancamentosModel lancamentosModel) {
        Objects.requireNonNull(lancamentosModel.getId());
        repository.delete(lancamentosModel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LancamentosModel> buscar(LancamentosModel lancamentosFiltro) {
        Example example = Example.of(lancamentosFiltro, ExampleMatcher.matching()
                                                        .withIgnoreCase()
                                                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
        return repository.findAll(example);
    }

    @Override
    public void atualizarStatus(LancamentosModel lancamentosModel, StatusLancamento statusLancamento) {
        lancamentosModel.setStatus(statusLancamento);
        atualizar(lancamentosModel);
    }

    @Override
    public void validar(LancamentosModel lancamentosModel) {

        if (lancamentosModel.getDescricao() == null || lancamentosModel.getDescricao().trim().equals("")){
            throw new RegraNegocioException("Descrição inválida");
        }

        if (lancamentosModel.getMes() == null || lancamentosModel.getMes() < 1 || lancamentosModel.getMes() > 12){
            throw new RegraNegocioException("Mês de lançamento inválido");
        }

        if (lancamentosModel.getAno() == null || lancamentosModel.getAno().toString().length() != 4){
            throw new RegraNegocioException("Ano inválido");
        }

        if (lancamentosModel.getUsuario() == null || lancamentosModel.getUsuario().getId() == null){
            throw new RegraNegocioException("Usuário inválido");
        }

        if (lancamentosModel.getValor() == null || lancamentosModel.getValor().compareTo(BigDecimal.ZERO) < 1 ){
            throw new RegraNegocioException("Valor inválido");
        }

        if (lancamentosModel.getTipo() == null ){
            throw new RegraNegocioException("Tipo de Lançamento inválido");
        }
    }

    @Override
    public Optional<LancamentosModel> obterPorId(Long id) {
        return repository.findById(id);
    }
}
