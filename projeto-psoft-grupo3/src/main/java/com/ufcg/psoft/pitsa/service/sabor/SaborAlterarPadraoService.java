package com.ufcg.psoft.pitsa.service.sabor;

import com.ufcg.psoft.pitsa.dto.SaborPostPutRequestDTO;
import com.ufcg.psoft.pitsa.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.pitsa.exception.EstabelecimentoNaoExisteException;
import com.ufcg.psoft.pitsa.exception.SaborNaoExisteException;
import com.ufcg.psoft.pitsa.model.Estabelecimento;
import com.ufcg.psoft.pitsa.model.Sabor;
import com.ufcg.psoft.pitsa.repository.EstabelecimentoRepository;
import com.ufcg.psoft.pitsa.repository.SaborRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaborAlterarPadraoService implements SaborAlterarService {

    @Autowired
    SaborRepository saborRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Sabor alterar(Long saborId, SaborPostPutRequestDTO saborPostPutRequestDTO, Long estabelecimentoId, String estabelecimentoCodigoAcesso) {
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(estabelecimentoId).orElseThrow(EstabelecimentoNaoExisteException::new);

        if (!(estabelecimento.getCodigoAcesso().equals(estabelecimentoCodigoAcesso))) {
            throw new CodigoDeAcessoInvalidoException();
        }

        Sabor sabor = saborRepository.findById(saborId).orElseThrow(SaborNaoExisteException::new);

        modelMapper.map(saborPostPutRequestDTO, sabor);
        return saborRepository.save(sabor);
    }
}

