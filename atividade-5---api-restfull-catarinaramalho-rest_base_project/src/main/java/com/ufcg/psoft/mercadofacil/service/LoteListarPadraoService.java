package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.exception.LoteNaoExisteException;
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
        if(id!=null && id > 0) {
            loteRepository.findById(id).orElseThrow(LoteNaoExisteException::new);
        }
        return loteRepository.findAll();
    }
}

