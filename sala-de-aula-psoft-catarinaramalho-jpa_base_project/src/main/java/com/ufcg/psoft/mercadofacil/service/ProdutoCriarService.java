package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.Produto;

@FunctionalInterface
public interface ProdutoCriarService {
    Produto criar(Produto produto);
}