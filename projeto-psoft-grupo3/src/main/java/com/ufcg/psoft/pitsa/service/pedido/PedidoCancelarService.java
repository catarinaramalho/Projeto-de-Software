package com.ufcg.psoft.pitsa.service.pedido;

@FunctionalInterface
public interface PedidoCancelarService {
    void cancelar(Long pedidoId, String codigoAcessoCliente);
}
