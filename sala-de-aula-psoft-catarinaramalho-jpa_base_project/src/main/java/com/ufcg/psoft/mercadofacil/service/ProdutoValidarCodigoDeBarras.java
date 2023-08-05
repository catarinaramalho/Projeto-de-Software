package com.ufcg.psoft.mercadofacil.service;

@FunctionalInterface
public interface ProdutoValidarCodigoDeBarras {
    Boolean validar(String codigo);
}