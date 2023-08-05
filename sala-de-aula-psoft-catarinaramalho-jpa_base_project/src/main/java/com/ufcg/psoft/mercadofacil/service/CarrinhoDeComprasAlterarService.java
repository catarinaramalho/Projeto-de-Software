package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.CarrinhoDeCompras;

@FunctionalInterface
public interface CarrinhoDeComprasAlterarService {
    CarrinhoDeCompras alterar(CarrinhoDeCompras carrinho);
}