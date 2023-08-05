package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.CarrinhoDeCompras;
import com.ufcg.psoft.mercadofacil.repository.CarrinhoDeComprasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarrinhoDeComprasListarPadraoService implements CarrinhoDeComprasListarService {

    @Autowired
    CarrinhoDeComprasRepository carrinhoDeComprasRepository;

    @Override
    public List<CarrinhoDeCompras> listar(Long id) {
        if (id == null) {
            return carrinhoDeComprasRepository.findAll();
        }
        return carrinhoDeComprasRepository.findAllById(List.of(id));
    }
}
