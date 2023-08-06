package com.ufcg.psoft.pitsa.service.pedido;

import com.ufcg.psoft.pitsa.model.Pedido;

import java.util.List;

@FunctionalInterface
public interface EstabelecimentoListarPedidoService {
    List<Pedido> estabelecimentoListar(Long pedidoId, Long estabelecimentoListadorId);
}