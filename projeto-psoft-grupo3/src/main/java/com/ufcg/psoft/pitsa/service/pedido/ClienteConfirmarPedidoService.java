package com.ufcg.psoft.pitsa.service.pedido;

import com.ufcg.psoft.pitsa.dto.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.pitsa.dto.PedidoPostPutRequestDTO;
import com.ufcg.psoft.pitsa.model.Pedido;

@FunctionalInterface
public interface ClienteConfirmarPedidoService {
    Pedido confirmar(Long pedidoId, Long clienteId, String codigoAcessoCliente, PedidoPostPutRequestDTO pedidoPostPutRequestDTO);
}
