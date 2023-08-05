package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.Lote;
import com.ufcg.psoft.mercadofacil.repository.LoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LoteDeletarPadraoService implements LoteDeletarService {

    @Autowired
    LoteRepository loteRepository;

    @Override
    public Lote deletar(Long id) {
        if(id == null){
            throw new RuntimeException("Id inválido");
        }
        Lote lote = loteRepository.findById(id).get();
        loteRepository.delete(lote);
        return lote;
    }
}