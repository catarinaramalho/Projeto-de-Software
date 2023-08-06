package com.ufcg.psoft.pitsa.service.estabelecimento;

@FunctionalInterface
public interface EstabelecimentoNotificarClientesService {
    String notificar(Long pedidoId);
}
