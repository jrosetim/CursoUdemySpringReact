package com.jrosetim.minhasfinancas.api.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LancamentoDTO {

    private Long id;
    private String Descricao;
    private Integer mes;
    private Integer ano;
    private BigDecimal valor;
    private Long uuario;
    private String tipo;
    private String status;
}
