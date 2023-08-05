package com.ufcg.psoft.mercadofacil.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.mercadofacil.model.Produto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LotePostPutRequestDTO {
    @JsonProperty("produto")
    @NotNull(message = "Produto invalido")
    private Produto produto;
    @JsonProperty("numeroDeItens")
    @Positive(message = "O numero de itens deve ser maior ou igual a zero")
    private Integer numeroDeItens;


}

