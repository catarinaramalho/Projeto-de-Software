package com.ufcg.psoft.pitsa.service.estabelecimento;

import com.ufcg.psoft.pitsa.dto.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.pitsa.model.Entregador;
import com.ufcg.psoft.pitsa.model.Estabelecimento;

@FunctionalInterface
public interface EstabelecimentoAlterarFilaEntregadoresService {
    void alterarFilaEntregadores(Entregador entregador, Estabelecimento estabelecimento, EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO);
}
