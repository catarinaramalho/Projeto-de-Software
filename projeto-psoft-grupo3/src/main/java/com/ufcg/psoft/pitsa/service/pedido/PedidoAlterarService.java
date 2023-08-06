package com.ufcg.psoft.pitsa.service.pedido;

import com.ufcg.psoft.pitsa.dto.PedidoPostPutRequestDTO;
import com.ufcg.psoft.pitsa.model.Pedido;

@FunctionalInterface
public interface PedidoAlterarService {

    Pedido alterar(Long pedidoId, String codigoAcesso, PedidoPostPutRequestDTO pedidoPostPutRequestDTO);
}
