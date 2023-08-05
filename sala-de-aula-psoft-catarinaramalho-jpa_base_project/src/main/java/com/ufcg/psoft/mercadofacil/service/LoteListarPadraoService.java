package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.Lote;
import com.ufcg.psoft.mercadofacil.repository.LoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoteListarPadraoService implements LoteListarService {

    @Autowired
    LoteRepository loteRepository;

    @Override
    public List<Lote> listar(Long id) {
        if (id == null) {
            return loteRepository.findAll();
        }
        return loteRepository.findAllById(List.of(id));
    }
}
