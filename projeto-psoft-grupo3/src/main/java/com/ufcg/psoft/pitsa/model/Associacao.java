package com.ufcg.psoft.pitsa.model;

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
@Table(name = "relacoes")
public class Associacao {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("entregador")
    @Column(nullable = false)
    private Long entregadorId;

    @JsonProperty("estabelecimento")
    @Column(nullable = false)
    private Long estabelecimentoId;

    @JsonProperty("status")
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean status;
}
