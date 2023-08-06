package com.ufcg.psoft.pitsa.service.pedido;

import com.ufcg.psoft.pitsa.dto.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.pitsa.dto.PedidoPostPutRequestDTO;
import com.ufcg.psoft.pitsa.model.Pedido;

@FunctionalInterface
public interface EstabelecimentoPrepararPedidoService {
    Pedido prepararPedido(Long pedidoId, Long estabelecimentoId, String codigoAcessoEstabelecimento, PedidoPostPutRequestDTO pedidoPostPutRequestDTO);
}
