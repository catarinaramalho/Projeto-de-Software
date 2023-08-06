package com.ufcg.psoft.pitsa.service.sabor;

import com.ufcg.psoft.pitsa.dto.SaborPostPutRequestDTO;
import com.ufcg.psoft.pitsa.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.pitsa.exception.EstabelecimentoNaoExisteException;
import com.ufcg.psoft.pitsa.model.Estabelecimento;
import com.ufcg.psoft.pitsa.model.Sabor;
import com.ufcg.psoft.pitsa.repository.EstabelecimentoRepository;
import com.ufcg.psoft.pitsa.repository.SaborRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SaborCriarPadraoService implements SaborCriarService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    SaborRepository saborRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Override
    public Sabor salvar(SaborPostPutRequestDTO saborPostPutRequestDTO, Long estabelecimentoId, String estabelecimentoCodigoAcesso) {
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(estabelecimentoId).orElseThrow(EstabelecimentoNaoExisteException::new);

        if (!(estabelecimento.getCodigoAcesso().equals(estabelecimentoCodigoAcesso))) {
            throw new CodigoDeAcessoInvalidoException();
        }

        Sabor sabor = modelMapper.map(saborPostPutRequestDTO, Sabor.class);
        Sabor resultado = saborRepository.save(sabor);

        Set<Sabor> sabores = estabelecimento.getSabores();
        if (sabores == null) {
            sabores = new HashSet<>();
        }
        sabores.add(sabor);
        estabelecimento.setSabores(sabores);
        estabelecimentoRepository.save(estabelecimento);

        return resultado;
    }
}
