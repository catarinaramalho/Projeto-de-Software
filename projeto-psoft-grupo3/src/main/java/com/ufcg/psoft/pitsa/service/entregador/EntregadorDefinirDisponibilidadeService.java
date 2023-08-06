package com.ufcg.psoft.pitsa.service.entregador;

import com.ufcg.psoft.pitsa.dto.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.pitsa.model.Entregador;

@FunctionalInterface
public interface EntregadorDefinirDisponibilidadeService {
    Entregador definirDisponibilidade(Long id, EntregadorPostPutRequestDTO entregadorPostPutRequestDTO, String codigoAcesso, boolean disponibilidade);
}
