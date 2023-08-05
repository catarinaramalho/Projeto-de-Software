package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.CarrinhoDeCompras;
import com.ufcg.psoft.mercadofacil.repository.CarrinhoDeComprasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarrinhoDeComprasAlterarPadraoService implements CarrinhoDeComprasAlterarService {
    @Autowired
    CarrinhoDeComprasRepository carrinhoDeComprasRepository;
    @Override
    public CarrinhoDeCompras alterar(CarrinhoDeCompras carrinho) {
        if (carrinho == null) {
            throw new RuntimeException("Carrinho De Compras inválido");
        }
        if (carrinho.getId() == null) {
            throw new RuntimeException("Id inválido");
        }
        if (carrinho.getQuantidade() < 0) {
            throw new RuntimeException("Quantidade inválida");
        }
        if (carrinho.getLote() == null) {
            throw new RuntimeException("Lote inválido");
        }
        if (carrinho.getCliente() == null) {
            throw new RuntimeException("Cliente inválido");
        }
        return carrinhoDeComprasRepository.save(carrinho);
    }
}
