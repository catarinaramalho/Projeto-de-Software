package com.ufcg.psoft.pitsa.service.pedido;

import com.ufcg.psoft.pitsa.dto.PedidoPostPutRequestDTO;
import com.ufcg.psoft.pitsa.model.Pedido;

@FunctionalInterface
public interface PedidoCriarService {
    Pedido salvar(Long clienteId, String clienteCodigoAcesso, Long estabelecimentoId, PedidoPostPutRequestDTO pedidoPostPutRequestDTO);
}
