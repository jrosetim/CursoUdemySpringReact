package com.jrosetim.minhasfinancas.api.resources;

import com.jrosetim.minhasfinancas.api.dto.LancamentoDTO;
import com.jrosetim.minhasfinancas.enums.StatusLancamento;
import com.jrosetim.minhasfinancas.enums.TipoLancamento;
import com.jrosetim.minhasfinancas.exception.RegraNegocioException;
import com.jrosetim.minhasfinancas.model.LancamentosModel;
import com.jrosetim.minhasfinancas.model.UsuarioModel;
import com.jrosetim.minhasfinancas.service.LancamentoService;
import com.jrosetim.minhasfinancas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoResource {

    @Autowired
    private LancamentoService lancamentoService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity salvar(@RequestBody LancamentoDTO lancamentoDTO){
        try {
            LancamentosModel lancamentosModel = converter(lancamentoDTO);

            lancamentosModel = lancamentoService.salvar(lancamentosModel);

            return ResponseEntity.ok(lancamentosModel);
        }catch(RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody LancamentoDTO dto ) {
        return lancamentoService.obterPorId(id).map(entity -> {
            try {
                LancamentosModel lancamento = converter(dto);
                lancamento.setId(entity.getId());
                lancamentoService.atualizar(lancamento);
                return ResponseEntity.ok(lancamento);
            }catch (RegraNegocioException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }

        }).orElseGet(() -> new ResponseEntity("Lançamento não encontrado", HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@PathVariable Long id){
        return lancamentoService.obterPorId(id).map(entity -> {
            lancamentoService.deletar(entity);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }).orElseGet(() -> new ResponseEntity("Lançamento não encontrado", HttpStatus.BAD_REQUEST));
    }

    @GetMapping
    public ResponseEntity buscar(
            @RequestParam(value = "descricao", required = false) String descricao,
            @RequestParam(value="mes", required = false) Integer mes,
            @RequestParam(value="ano", required = false) Integer ano,
            @RequestParam("usuario") Long idUsuario
    ){
        LancamentosModel lancamentoFiltro = new LancamentosModel();

        UsuarioModel usuario = usuarioService
                .obertPorId(idUsuario)
                .orElseThrow( () -> new RegraNegocioException("Usuário não encontrado"));

        lancamentoFiltro.builder().descricao(descricao).ano(ano).mes(mes).usuario(usuario).build();

        List<LancamentosModel> lancamentos = lancamentoService.buscar(lancamentoFiltro);

        return ResponseEntity.ok(lancamentos);
    }

    private LancamentosModel converter(LancamentoDTO dto){
        UsuarioModel usuario = usuarioService
                .obertPorId(dto.getUuario())
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado"));

        return LancamentosModel
                .builder()
                .id(dto.getId())
                .descricao(dto.getDescricao())
                .ano(dto.getAno())
                .mes(dto.getMes())
                .valor(dto.getValor())
                .usuario(usuario)
                .tipo(TipoLancamento.valueOf(dto.getTipo()))
                .status(StatusLancamento.valueOf(dto.getStatus()))
                .build();
    }
}
