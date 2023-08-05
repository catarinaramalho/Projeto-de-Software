package com.ufcg.psoft.mercadofacil.repository;

import java.util.List;

public interface ProdutoRepository<T, ID> {
    T save(T produto);
    T find(ID id);
    List<T> findAll();
    T update(T produto);
    void delete(T produto);
    void deleteAll();
}

