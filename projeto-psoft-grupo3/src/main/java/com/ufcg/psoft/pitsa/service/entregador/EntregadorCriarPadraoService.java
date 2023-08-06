package com.ufcg.psoft.pitsa.service.entregador;

import com.ufcg.psoft.pitsa.dto.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.pitsa.model.Entregador;
import com.ufcg.psoft.pitsa.repository.EntregadorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntregadorCriarPadraoService implements EntregadorCriarService {
    @Autowired
    EntregadorRepository entregadorRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public Entregador criar(EntregadorPostPutRequestDTO entregadorPostPutRequestDTO) {
        Entregador entregador = modelMapper.map(entregadorPostPutRequestDTO, Entregador.class);
        return entregadorRepository.save(entregador);
    }
}
