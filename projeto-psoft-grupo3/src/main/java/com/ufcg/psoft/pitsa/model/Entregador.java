package com.ufcg.psoft.pitsa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "entregadores")
public class Entregador {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("nome")
    @Column(nullable = false)
    private String nome;

    @JsonProperty("placa")
    @Column(nullable = false)
    private String placaVeiculo;

    @JsonProperty("tipo")
    @Column(nullable = false)
    private String tipoVeiculo;

    @JsonProperty("cor")
    @Column(nullable = false)
    private String corVeiculo;

    @JsonProperty("disponilibidade")
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean disponibilidade;

    @JsonProperty("statusAprovacao")
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean statusAprovacao;

    @JsonIgnore
    @Column(nullable = false)
    private String codigoAcesso;
}
