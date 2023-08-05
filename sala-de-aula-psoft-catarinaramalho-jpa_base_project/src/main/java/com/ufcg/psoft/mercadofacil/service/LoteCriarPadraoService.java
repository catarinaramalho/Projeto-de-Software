package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.Lote;
import com.ufcg.psoft.mercadofacil.repository.LoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LoteCriarPadraoService implements LoteCriarService {

    @Autowired
    LoteRepository loteRepository;

    @Override
    public Lote criar(Lote lote) {
        if (lote == null) {
            throw new RuntimeException("Lote inválido");
        }
        return loteRepository.save(lote);

    }
}