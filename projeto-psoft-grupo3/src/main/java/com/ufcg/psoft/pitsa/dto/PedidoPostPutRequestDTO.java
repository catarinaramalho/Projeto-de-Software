package com.ufcg.psoft.pitsa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.pitsa.model.Pizza;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoPostPutRequestDTO {

    @JsonProperty("enderecoEntrega")
    private String enderecoEntrega;

    @JsonProperty
    @ManyToOne()
    @NotEmpty(message = "Pizzas obrigatorias")
    private List<Pizza> pizzas;

}