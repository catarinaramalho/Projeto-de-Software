package com.ufcg.psoft.pitsa.service.estabelecimento;

import com.ufcg.psoft.pitsa.dto.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.pitsa.model.Estabelecimento;
import com.ufcg.psoft.pitsa.model.Pedido;

@FunctionalInterface
public interface EstabelecimentoAlterarFilaPedidoService {

    void alterarFilaPedidos(Pedido pedido, Estabelecimento estabelecimento, EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO);
}
