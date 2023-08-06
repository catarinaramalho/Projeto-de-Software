package com.ufcg.psoft.pitsa.service.pedido;

@FunctionalInterface
public interface ClienteExcluirPedidoService {
    void clienteExcluir(Long pedidoId, Long clienteExclusorId);
}
