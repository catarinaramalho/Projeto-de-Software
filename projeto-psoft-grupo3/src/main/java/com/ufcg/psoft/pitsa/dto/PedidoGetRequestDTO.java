package com.ufcg.psoft.pitsa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.pitsa.model.Pedido;
import com.ufcg.psoft.pitsa.model.Pizza;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoGetRequestDTO {
    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("clienteId")
    @NotNull(message = "Cliente obrigatorio")
    private Long clienteId;

    @JsonProperty("estabelecimentoId")
    @NotNull(message = "Estabelecimento obrigatorio")
    private Long estabelecimentoId;

    @JsonProperty("enderecoEntrega")
    @NotBlank(message = "Endereco de entrega obrigatorio")
    private String enderecoEntrega;

    @JsonProperty("preco")
    @NotNull(message = "Preco obrigatorio")
    private Double preco;

    @JsonProperty("statusPagamento")
    @NotNull(message = "Status de pagamento obrigatorio")
    private Boolean statusPagamento;

    @JsonProperty("statusEntrega")
    @Column
    @Builder.Default
    private String statusEntrega = "Pedido recebido";

    @JsonProperty
    @Builder.Default
    private List<Pizza> pizzas = List.of();

    public PedidoGetRequestDTO(Pedido pedido) {
        this.id = pedido.getId();
        this.clienteId = pedido.getClienteId();
        this.estabelecimentoId = pedido.getEstabelecimentoId();
        this.enderecoEntrega = pedido.getEnderecoEntrega();
        this.preco = pedido.getPreco();
        this.statusPagamento = pedido.getStatusPagamento();
        this.statusEntrega = pedido.getStatusEntrega();
        this.pizzas = pedido.getPizzas();
    }
}
