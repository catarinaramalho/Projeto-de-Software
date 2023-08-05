package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.CarrinhoDeCompras;

@FunctionalInterface
public interface CarrinhoDeComprasCriarService {
    CarrinhoDeCompras criar(CarrinhoDeCompras carrinho);
}