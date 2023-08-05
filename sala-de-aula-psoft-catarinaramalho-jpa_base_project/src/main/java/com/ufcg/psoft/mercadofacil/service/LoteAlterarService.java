package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.Lote;

@FunctionalInterface
public interface LoteAlterarService {
    Lote alterar(Lote lote);
}