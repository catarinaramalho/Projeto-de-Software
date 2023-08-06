package com.ufcg.psoft.pitsa.service.estabelecimento;

import com.ufcg.psoft.pitsa.dto.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.pitsa.model.Entregador;
import com.ufcg.psoft.pitsa.model.Estabelecimento;
import com.ufcg.psoft.pitsa.repository.EntregadorRepository;
import com.ufcg.psoft.pitsa.repository.EstabelecimentoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstabelecimentoAlterarFilaEntregadoresPadraoService implements EstabelecimentoAlterarFilaEntregadoresService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Override
    public void alterarFilaEntregadores(Entregador entregador, Estabelecimento estabelecimento, EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO) {
        List<Entregador> entregadoresDisponiveis = estabelecimento.getEntregadoresDisponiveis();
        entregadoresDisponiveis.add(entregador);
        estabelecimento.setEntregadoresDisponiveis(entregadoresDisponiveis);

        modelMapper.map(estabelecimentoPostPutRequestDTO, estabelecimento);
        estabelecimentoRepository.save(estabelecimento);
    }
}
