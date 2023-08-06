package com.ufcg.psoft.pitsa.service.associacao;

import com.ufcg.psoft.pitsa.exception.AssociacaoNaoExisteException;
import com.ufcg.psoft.pitsa.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.pitsa.exception.EntregadorNaoExisteException;
import com.ufcg.psoft.pitsa.exception.EstabelecimentoNaoExisteException;
import com.ufcg.psoft.pitsa.model.Associacao;
import com.ufcg.psoft.pitsa.model.Estabelecimento;
import com.ufcg.psoft.pitsa.repository.AssociacaoRepository;
import com.ufcg.psoft.pitsa.repository.EntregadorRepository;
import com.ufcg.psoft.pitsa.repository.EstabelecimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssociacaoAprovarPadraoService implements AssociacaoAprovarService {

    @Autowired
    AssociacaoRepository associacaoRepository;

    @Autowired
    EntregadorRepository entregadorRepository;

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Override
    public Associacao aprovar(Long entregadorId, Long estabelecimentoId, String codigoAcesso) {
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(estabelecimentoId).orElseThrow(EstabelecimentoNaoExisteException::new);
        entregadorRepository.findById(entregadorId).orElseThrow(EntregadorNaoExisteException::new);

        if (!(estabelecimento.getCodigoAcesso().equals(codigoAcesso))) {
            throw new CodigoDeAcessoInvalidoException();
        }

        Associacao associacao = associacaoRepository.findByEntregadorIdAndEstabelecimentoId(entregadorId, estabelecimentoId)
                .orElseThrow(AssociacaoNaoExisteException::new);

        associacao.setStatus(true);

        return associacaoRepository.save(associacao);
    }
}
