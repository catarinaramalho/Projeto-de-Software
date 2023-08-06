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
@Table(name = "pizzas")
public class Pizza {
    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("tamanho")
    @Column(nullable = false)
    private String tamanho;

    @JsonProperty("sabor1")
    @OneToOne
    @JoinColumn(nullable = false)
    private Sabor sabor1;

    @JsonProperty("sabor2")
    @OneToOne
    private Sabor sabor2;
}
