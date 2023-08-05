package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.CarrinhoDeCompras;
import com.ufcg.psoft.mercadofacil.repository.CarrinhoDeComprasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CarrinhoDeComprasCriarPadraoService implements CarrinhoDeComprasCriarService {

    @Autowired
    CarrinhoDeComprasRepository carrinhoDeComprasRepository;

    @Override
    public CarrinhoDeCompras criar(CarrinhoDeCompras carrinho) {
        if (carrinho == null) {
            throw new RuntimeException("Carrinho de Compras inválido");
        }
        return carrinhoDeComprasRepository.save(carrinho);

    }
}