package com.ufcg.psoft.pitsa.service.sabor;

import com.ufcg.psoft.pitsa.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.pitsa.exception.EstabelecimentoNaoExisteException;
import com.ufcg.psoft.pitsa.exception.SaborNaoExisteException;
import com.ufcg.psoft.pitsa.model.Estabelecimento;
import com.ufcg.psoft.pitsa.model.Sabor;
import com.ufcg.psoft.pitsa.repository.EstabelecimentoRepository;
import com.ufcg.psoft.pitsa.repository.SaborRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaborListarPadraoService implements SaborListarService {

    @Autowired
    SaborRepository saborRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Override
    public List<Sabor> listar(Long saborId, Long estabelecimentoId, String estabelecimentoCodigoAcesso) {
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(estabelecimentoId).orElseThrow(EstabelecimentoNaoExisteException::new);

        if (!estabelecimento.getCodigoAcesso().equals(estabelecimentoCodigoAcesso)) {
            throw new CodigoDeAcessoInvalidoException();
        }

        if (saborId != null && saborId > 0) {
            return List.of(saborRepository.findById(saborId).orElseThrow(SaborNaoExisteException::new));
        }
        return saborRepository.findAll();
    }
}
