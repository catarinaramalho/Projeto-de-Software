package com.ufcg.psoft.pitsa.service.pedido;

@FunctionalInterface
public interface PedidoNotificarEstabelecimentoService {
    String notificar(Long pedidoId);
}
