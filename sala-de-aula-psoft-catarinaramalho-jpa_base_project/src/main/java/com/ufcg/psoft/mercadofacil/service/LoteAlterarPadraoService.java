package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.Lote;
import com.ufcg.psoft.mercadofacil.repository.LoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoteAlterarPadraoService implements LoteAlterarService {
    @Autowired
    LoteRepository loteRepository;
    @Override
    public Lote alterar(Lote lote) {
        if (lote == null) {
            throw new RuntimeException("Lote inválido");
        }
        if (lote.getId() == null) {
            throw new RuntimeException("Id inválido");
        }
        if (lote.getNumeroDeItens() < 0) {
            throw new RuntimeException("Número de itens inválido");
        }
        if (lote.getProduto() == null) {
            throw new RuntimeException("Produto inválido");
        }
        return loteRepository.save(lote);
    }
}
