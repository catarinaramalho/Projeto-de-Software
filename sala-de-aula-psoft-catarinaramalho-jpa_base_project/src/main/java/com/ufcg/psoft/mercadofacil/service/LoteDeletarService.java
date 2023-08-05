package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.Lote;

@FunctionalInterface
public interface LoteDeletarService {
    Lote deletar(Long id);
}