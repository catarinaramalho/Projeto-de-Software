package com.ufcg.psoft.pitsa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaborGetRequestDTO {

    @JsonProperty("nome")
    @NotBlank(message = "Nome obrigatorio")
    private String nome;

    @JsonProperty("precoM")
    @NotBlank(message = "Preco obrigatorio")
    private double precoM;

    @JsonProperty("precoG")
    @NotBlank(message = "Preco obrigatorio")
    private double precoG;

    @JsonProperty("disponivel")
    @NotBlank(message = "Disponibilidade obrigatoria")
    private boolean disponivel;
}
