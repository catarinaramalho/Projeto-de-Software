package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProdutoCriarPadraoService implements ProdutoCriarService {

    @Autowired
    ProdutoRepository produtoRepository;

    @Override
    public Produto criar(Produto produto) {
        if (produto == null) {
            throw new RuntimeException("Produto inválido");
        }
        return produtoRepository.save(produto);

    }
}
