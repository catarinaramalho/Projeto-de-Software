package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.dto.ProdutoNomePatchRequestDTO;
import com.ufcg.psoft.mercadofacil.dto.ProdutoPostPutRequestDTO;
import com.ufcg.psoft.mercadofacil.model.Produto;

@FunctionalInterface
public interface ProdutoAlterarNomeService {
    Produto alterarParcialmente(Long id, ProdutoNomePatchRequestDTO produtoNomePatchRequestDTO);
}
