package com.ufcg.psoft.pitsa.service.pedido;


import com.ufcg.psoft.pitsa.dto.PedidoGetRequestDTO;

import java.util.List;

@FunctionalInterface
public interface ClienteListarPedidoEstabelecimentoService {
    List<PedidoGetRequestDTO> listar(Long pedidoId, Long clienteId, Long estabelecimentoId, String codigoAcesso, String status);
}
