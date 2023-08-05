package com.ufcg.psoft.mercadofacil.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.mercadofacil.constraints.CodigoDeBarras;
import jakarta.validation.constraints.NotBlank;
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
public class ProdutoPostPutRequestDTO {
    @JsonProperty("nome")
    @NotBlank(message = "Nome obrigatorio")
    private String nome;
    @JsonProperty("preco")
    @Positive(message = "Preco deve ser maior ou igual a zero")
    private Double preco;
    @JsonProperty("codigoDeBarras")
    @CodigoDeBarras(message = "Codigo de Barras invalido")
    private String codigoDeBarras;
    @JsonProperty("fabricante")
    @NotBlank(message = "Fabricante obrigatorio")
    private String fabricante;
}
