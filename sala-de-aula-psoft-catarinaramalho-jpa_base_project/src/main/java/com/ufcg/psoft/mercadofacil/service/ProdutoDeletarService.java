package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.Produto;

@FunctionalInterface
public interface ProdutoDeletarService {
    Produto deletar(Long id);
}