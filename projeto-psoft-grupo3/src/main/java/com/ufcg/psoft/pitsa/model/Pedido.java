package com.ufcg.psoft.pitsa.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pedidos")
public class Pedido {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("preco")
    @Column(nullable = false)
    private Double preco;

    @JsonProperty("enderecoEntrega")
    @Column
    private String enderecoEntrega;

    @JsonProperty("clienteId")
    @Column(nullable = false)
    private Long clienteId;

    @JsonProperty("estabelecimentoId")
    @Column(nullable = false)
    private Long estabelecimentoId;

    @JsonProperty("statusPagamento")
    @Column(nullable = false)
    @Builder.Default
    private Boolean statusPagamento = false;

    @JsonProperty("statusEntrega")
    @Column
    @Builder.Default
    private String statusEntrega = "Pedido recebido";

    @JsonProperty("entregadorId")
    @Column
    private Long entregadorId;

    @JsonProperty
    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "pizzas")
    private List<Pizza> pizzas = new ArrayList<>();
}