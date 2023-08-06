package com.ufcg.psoft.pitsa.service.pedido;

@FunctionalInterface
public interface PedidoNotificarClienteService {
    String notificar(Long pedidoId);
}
