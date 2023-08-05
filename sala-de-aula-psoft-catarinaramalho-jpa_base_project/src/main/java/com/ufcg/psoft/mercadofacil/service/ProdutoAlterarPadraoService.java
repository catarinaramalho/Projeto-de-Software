package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdutoAlterarPadraoService implements ProdutoAlterarService {
    @Autowired
    ProdutoRepository produtoRepository;
    ProdutoValidarCodigoDeBarrasImpl validador = new ProdutoValidarCodigoDeBarrasImpl();
    @Override
    public Produto alterar(Produto produto) {
        if (produto == null) {
            throw new RuntimeException("Produto inválido");
        }
        if (produto.getId() == null) {
            throw new RuntimeException("Id inválido");
        }
        if (produto.getNome() == null || produto.getNome().isBlank()) {
            throw new RuntimeException("Nome inválido");
        }
        if (produto.getPreco() <= 0.0) {
            throw new RuntimeException("Preço inválido");
        }
        if (produto.getFabricante() == null || produto.getFabricante().isBlank()) {
            throw new RuntimeException("Fabricante inválido");
        }
        if (produto.getCodigoDeBarras() == null || produto.getCodigoDeBarras().isBlank() || !validador.validar(produto.getCodigoDeBarras())) {
            throw new RuntimeException("Código de barras inválido");
        }
        return produtoRepository.save(produto);
    }
}
