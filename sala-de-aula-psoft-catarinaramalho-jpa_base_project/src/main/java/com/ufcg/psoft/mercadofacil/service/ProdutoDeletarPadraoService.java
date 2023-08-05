package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProdutoDeletarPadraoService implements ProdutoDeletarService {

    @Autowired
    ProdutoRepository produtoRepository;

    @Override
    public Produto deletar(Long id) {
        if(id == null){
            throw new RuntimeException("Id inválido");
        }
        Produto produto = produtoRepository.findById(id).get();
        produtoRepository.delete(produto);
        return produto;
    }
}