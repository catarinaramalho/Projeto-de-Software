package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdutoAlterarPadraoService implements ProdutoAlterarService {
    @Autowired
    ProdutoRepository<Produto, Long> produtoRepository;
    ProdutoValidarCodigoDeBarrasImpl validador = new ProdutoValidarCodigoDeBarrasImpl();
    @Override
    public Produto alterar(Produto produto) {
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
        if (produto.getCodigoBarra() == null || produto.getCodigoBarra().isBlank() || !validador.validar(produto.getCodigoBarra())) {
            throw new RuntimeException("Código de barras inválido");
        } 
        return produtoRepository.update(produto);
    }
}
