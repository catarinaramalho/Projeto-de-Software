package com.ufcg.psoft.pitsa.service.pedido;

import com.ufcg.psoft.pitsa.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.pitsa.exception.EstabelecimentoNaoExisteException;
import com.ufcg.psoft.pitsa.exception.PedidoNaoExisteException;
import com.ufcg.psoft.pitsa.model.Estabelecimento;
import com.ufcg.psoft.pitsa.model.Pedido;
import com.ufcg.psoft.pitsa.repository.EstabelecimentoRepository;
import com.ufcg.psoft.pitsa.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstabelecimentoExcluirPedidoPadraoService implements EstabelecimentoExcluirPedidoService {

    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    public void estabelecimentoExcluir(Long pedidoId, Long estabelecimentoExclusorId) {
        if (pedidoId != null && pedidoId > 0) {
            Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(PedidoNaoExisteException::new);
            Estabelecimento estabelecimentoPedido = estabelecimentoRepository.findById(pedido.getEstabelecimentoId()).orElseThrow(EstabelecimentoNaoExisteException::new);
            Estabelecimento estabelecimentoExclusor = estabelecimentoRepository.findById(estabelecimentoExclusorId).orElseThrow(EstabelecimentoNaoExisteException::new);

            if (!estabelecimentoPedido.getCodigoAcesso().equals(estabelecimentoExclusor.getCodigoAcesso())) {
                throw new CodigoDeAcessoInvalidoException();
            }
        }

        if (pedidoId == null) {
            pedidoRepository.findAll().forEach(pedido -> {
                if (pedido.getEstabelecimentoId().equals(estabelecimentoExclusorId)) {
                    pedidoRepository.deleteById(pedido.getId());
                }
            });
        } else {
            pedidoRepository.deleteById(pedidoId);
        }
    }
}
