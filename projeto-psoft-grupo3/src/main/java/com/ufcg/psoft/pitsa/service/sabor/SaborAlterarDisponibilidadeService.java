package com.ufcg.psoft.pitsa.service.sabor;

import com.ufcg.psoft.pitsa.model.Sabor;

@FunctionalInterface
public interface SaborAlterarDisponibilidadeService {
    Sabor alterarDisponibilidade(Long saborId, Long estabelecimentoId, String estabelecimentoCodigoAcesso, Boolean disponibilidade);
}
