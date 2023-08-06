package com.ufcg.psoft.pitsa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.pitsa.model.Entregador;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntregadorGetRequestDTO {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("nome")
    @NotBlank(message = "Nome obrigatorio")
    private String nome;

    @JsonProperty("placa_veiculo")
    @NotBlank(message = "Placa do veiculo obrigatoria")
    private String placaVeiculo;

    @JsonProperty("tipo_veiculo")
    @NotBlank(message = "Tipo do veiculo obrigatorio")
    private String tipoVeiculo;

    @JsonProperty("cor_veiculo")
    @NotBlank(message = "Cor do veiculo obrigatoria")
    private String corVeiculo;

    public EntregadorGetRequestDTO(Entregador entregador) {
        this.id = entregador.getId();
        this.nome = entregador.getNome();
        this.placaVeiculo = entregador.getPlacaVeiculo();
        this.tipoVeiculo = entregador.getTipoVeiculo();
        this.corVeiculo = entregador.getCorVeiculo();
    }
}
