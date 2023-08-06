package com.ufcg.psoft.pitsa.service.sabor;

import com.ufcg.psoft.pitsa.dto.SaborPostPutRequestDTO;
import com.ufcg.psoft.pitsa.model.Sabor;

@FunctionalInterface
public interface SaborCriarService {
    Sabor salvar(SaborPostPutRequestDTO saborPostPutRequestDTO, Long estabelecimentoId, String estabelecimentoCodigoAcesso);

}
