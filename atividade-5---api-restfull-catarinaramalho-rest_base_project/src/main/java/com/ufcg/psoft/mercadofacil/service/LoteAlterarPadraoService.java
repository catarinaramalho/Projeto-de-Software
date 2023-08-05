package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.dto.LotePostPutRequestDTO;
import com.ufcg.psoft.mercadofacil.exception.LoteNaoExisteException;
import com.ufcg.psoft.mercadofacil.model.Lote;
import com.ufcg.psoft.mercadofacil.repository.LoteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoteAlterarPadraoService implements LoteAlterarService {
    @Autowired
    LoteRepository loteRepository;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public Lote alterar(Long id, LotePostPutRequestDTO lotePostPutRequestDTO) {
        Lote lote = loteRepository.findById(id).orElseThrow(LoteNaoExisteException::new);
        modelMapper.map(lotePostPutRequestDTO, lote);
        return loteRepository.save(lote);
    }
}
