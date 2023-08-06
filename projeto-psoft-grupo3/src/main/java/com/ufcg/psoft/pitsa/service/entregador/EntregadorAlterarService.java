package com.ufcg.psoft.pitsa.service.entregador;

import com.ufcg.psoft.pitsa.dto.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.pitsa.model.Entregador;

@FunctionalInterface
public interface EntregadorAlterarService {
    Entregador alterar(Long id, EntregadorPostPutRequestDTO entregadorPostPutRequestDTO, String codigoAcesso);
}
