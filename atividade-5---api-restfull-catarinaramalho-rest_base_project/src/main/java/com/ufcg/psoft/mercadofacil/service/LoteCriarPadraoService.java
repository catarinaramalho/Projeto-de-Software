package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.dto.LotePostPutRequestDTO;
import com.ufcg.psoft.mercadofacil.model.Lote;
import com.ufcg.psoft.mercadofacil.repository.LoteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoteCriarPadraoService implements LoteCriarService {
    @Autowired
    LoteRepository loteRepository;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public Lote salvar(LotePostPutRequestDTO lotePostPutRequestDTO) {
        Lote lote = modelMapper.map(lotePostPutRequestDTO, Lote.class);
        return loteRepository.save(lote);
    }
}
