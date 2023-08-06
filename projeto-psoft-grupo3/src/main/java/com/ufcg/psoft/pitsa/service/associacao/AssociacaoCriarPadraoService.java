package com.ufcg.psoft.pitsa.service.associacao;

import com.ufcg.psoft.pitsa.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.pitsa.exception.EntregadorNaoExisteException;
import com.ufcg.psoft.pitsa.exception.EstabelecimentoNaoExisteException;
import com.ufcg.psoft.pitsa.model.Associacao;
import com.ufcg.psoft.pitsa.model.Entregador;
import com.ufcg.psoft.pitsa.repository.AssociacaoRepository;
import com.ufcg.psoft.pitsa.repository.EntregadorRepository;
import com.ufcg.psoft.pitsa.repository.EstabelecimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssociacaoCriarPadraoService implements AssociacaoCriarService {

    @Autowired
    AssociacaoRepository associacaoRepository;

    @Autowired
    EntregadorRepository entregadorRepository;

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Override
    public Associacao associar(Long entregadorId, String codigoAcesso, Long estabelecimentoId) {
        Entregador entregador = entregadorRepository.findById(entregadorId).orElseThrow(EntregadorNaoExisteException::new);
        estabelecimentoRepository.findById(estabelecimentoId).orElseThrow(EstabelecimentoNaoExisteException::new);

        if (!(entregador.getCodigoAcesso().equals(codigoAcesso))) {
            throw new CodigoDeAcessoInvalidoException();
        }

        return associacaoRepository.save(Associacao.builder()
                .entregadorId(entregadorId)
                .estabelecimentoId(estabelecimentoId)
                .status(false)
                .build());
    }
}
