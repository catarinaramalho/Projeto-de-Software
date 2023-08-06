package com.ufcg.psoft.pitsa.service.sabor;

import com.ufcg.psoft.pitsa.exception.*;
import com.ufcg.psoft.pitsa.model.Estabelecimento;
import com.ufcg.psoft.pitsa.model.Sabor;
import com.ufcg.psoft.pitsa.repository.EstabelecimentoRepository;
import com.ufcg.psoft.pitsa.repository.SaborRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaborAlterarDisponibilidadePadraoService implements SaborAlterarDisponibilidadeService {

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    SaborRepository saborRepository;

    @Autowired
    SaborNotificarClientesService saborNotificarClientesService;

    @Override
    public Sabor alterarDisponibilidade(Long saborId, Long estabelecimentoId, String estabelecimentoCodigoAcesso, Boolean disponibilidade) {
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(estabelecimentoId).orElseThrow(EstabelecimentoNaoExisteException::new);

        if (!(estabelecimento.getCodigoAcesso().equals(estabelecimentoCodigoAcesso))) {
            throw new CodigoDeAcessoInvalidoException();
        }

        Sabor sabor = saborRepository.findById(saborId).orElseThrow(SaborNaoExisteException::new);

        if (disponibilidade) {
            if (sabor.isDisponivel()) {
                throw new SaborJaDisponivelException();
            }
            saborNotificarClientesService.notificarClientes(saborId);
        } else {
            if (!sabor.isDisponivel()) {
                throw new SaborJaIndisponivelException();
            }
        }
        sabor.setDisponivel(disponibilidade);
        return saborRepository.save(sabor);
    }
}
