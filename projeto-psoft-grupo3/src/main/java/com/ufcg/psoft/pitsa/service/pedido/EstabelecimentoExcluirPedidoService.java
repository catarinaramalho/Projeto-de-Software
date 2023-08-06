package com.ufcg.psoft.pitsa.service.pedido;

@FunctionalInterface
public interface EstabelecimentoExcluirPedidoService {
    void estabelecimentoExcluir(Long pedidoId, Long estabelecimentoExclusorId);
}
