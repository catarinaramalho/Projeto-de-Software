package com.ufcg.psoft.pitsa.service.sabor;


import com.ufcg.psoft.pitsa.dto.SaborPostPutRequestDTO;
import com.ufcg.psoft.pitsa.model.Sabor;

@FunctionalInterface
public interface SaborAlterarService {
    Sabor alterar(Long saborId, SaborPostPutRequestDTO saborPostPutRequestDTO, Long estabelecimentoId, String estabelecimentoCodigoAcesso);
}
