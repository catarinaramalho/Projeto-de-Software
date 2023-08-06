package com.ufcg.psoft.pitsa.service.estabelecimento;

@FunctionalInterface
public interface EstabelecimentoNotificarPedidoProntoService {
    String notificar(Long pedidoId);
}
