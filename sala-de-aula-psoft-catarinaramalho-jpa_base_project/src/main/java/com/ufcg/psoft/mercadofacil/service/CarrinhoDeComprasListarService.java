package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.CarrinhoDeCompras;

import java.util.List;

@FunctionalInterface
public interface CarrinhoDeComprasListarService {
    List<CarrinhoDeCompras> listar(Long id);
}