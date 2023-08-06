package com.ufcg.psoft.pitsa.service.entregador;

import com.ufcg.psoft.pitsa.dto.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.pitsa.model.Entregador;

@FunctionalInterface
public interface EntregadorCriarService {
    Entregador criar(EntregadorPostPutRequestDTO entregadorPostPutRequestDTO);
}
