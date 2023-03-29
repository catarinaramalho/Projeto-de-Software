package com.ufcg.psoft.mercadofacil.repository;

public interface ProdutoRepository<Produto, Long> {
    Produto update(Produto produtoAlterado);
    Produto find(Long id);
}
