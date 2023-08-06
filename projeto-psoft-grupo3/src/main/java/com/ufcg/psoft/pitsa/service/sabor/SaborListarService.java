package com.ufcg.psoft.pitsa.service.sabor;

import com.ufcg.psoft.pitsa.model.Sabor;

import java.util.List;

@FunctionalInterface
public interface SaborListarService {
    List<Sabor> listar(Long saborId, Long estabelecimentoId, String estabelecimentoCodigoAcesso);
}
