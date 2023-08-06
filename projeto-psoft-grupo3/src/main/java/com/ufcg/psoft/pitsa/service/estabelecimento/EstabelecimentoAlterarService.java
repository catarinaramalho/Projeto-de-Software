package com.ufcg.psoft.pitsa.service.estabelecimento;

import com.ufcg.psoft.pitsa.dto.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.pitsa.model.Estabelecimento;

@FunctionalInterface
public interface EstabelecimentoAlterarService {
    Estabelecimento alterar(Long id, String codigoAcesso, EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO);
}
