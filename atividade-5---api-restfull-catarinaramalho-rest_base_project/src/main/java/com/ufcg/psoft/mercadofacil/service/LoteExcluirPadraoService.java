package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.exception.LoteNaoExisteException;
import com.ufcg.psoft.mercadofacil.model.Lote;
import com.ufcg.psoft.mercadofacil.repository.LoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoteExcluirPadraoService implements LoteExcluirService {
    @Autowired
    LoteRepository loteRepository;
    @Override
    public void excluir(Long id) {
        Lote lote = loteRepository.findById(id).orElseThrow(LoteNaoExisteException::new);
        loteRepository.delete(lote);
    }
}
