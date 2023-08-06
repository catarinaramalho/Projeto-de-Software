package com.ufcg.psoft.pitsa.service.pedido;

import com.ufcg.psoft.pitsa.model.Pedido;

import java.util.List;

@FunctionalInterface
public interface ClienteListarPedidoService {
    List<Pedido> clienteListar(Long pedidoId, Long clienteListadorId);
}
