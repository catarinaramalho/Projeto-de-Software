package com.ufcg.psoft.pitsa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.pitsa.model.Estabelecimento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstabelecimentoPostPutRequestDTO {

    @JsonProperty("codigoAcesso")
    @NotNull(message = "O codigo de acesso e obrigatorio")
    @Pattern(regexp = "^\\d{6}$", message = "Codigo de acesso deve ter exatamente 6 digitos numericos")
    private String codigoAcesso;

    public EstabelecimentoPostPutRequestDTO(Estabelecimento estabelecimento) {
        this.codigoAcesso = estabelecimento.getCodigoAcesso();
    }
}
