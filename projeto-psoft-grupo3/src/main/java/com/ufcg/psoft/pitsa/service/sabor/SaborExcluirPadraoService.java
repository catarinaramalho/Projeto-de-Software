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

@Service
public class SaborExcluirPadraoService implements SaborExcluirService {

    @Autowired
    SaborRepository saborRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Override
    public void excluir(Long saborId, Long estabelecimentoId, String estabelecimentoCodigoAcesso) {
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(estabelecimentoId).orElseThrow(EstabelecimentoNaoExisteException::new);

        if (!(estabelecimento.getCodigoAcesso().equals(estabelecimentoCodigoAcesso))) {
            throw new CodigoDeAcessoInvalidoException();
        }

        Sabor sabor = saborRepository.findById(saborId).orElseThrow(SaborNaoExisteException::new);
        saborRepository.delete(sabor);
    }
}
