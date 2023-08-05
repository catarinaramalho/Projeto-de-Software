package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.CarrinhoDeCompras;
import com.ufcg.psoft.mercadofacil.repository.CarrinhoDeComprasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CarrinhoDeComprasDeletarPadraoService implements CarrinhoDeComprasDeletarService {

    @Autowired
    CarrinhoDeComprasRepository carrinhoDeComprasRepository;

    @Override
    public CarrinhoDeCompras deletar(Long id) {
        if(id == null){
            throw new RuntimeException("Id inválido");
        }
        CarrinhoDeCompras carrinho = carrinhoDeComprasRepository.findById(id).get();
        carrinhoDeComprasRepository.delete(carrinho);
        return carrinho;
    }
}