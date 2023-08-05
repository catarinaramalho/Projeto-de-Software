package com.ufcg.psoft.mercadofacil.model;

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
@Table(name = "clientes")
public class Cliente {
    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @JsonProperty("cpf")
    @Column(nullable = false)
    private Long cpf;
    @JsonProperty("nome")
    @Column(nullable = false)
    private String nome;
    @JsonProperty("idade")
    @Column(nullable = false)
    private Integer idade;
    @JsonProperty("endereco")
    @Column(nullable = false)
    private String endereco;
}

