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

import java.util.ArrayList;
import java.util.List;

@Service
public class EstabelecimentoListarPedidoPadraoService implements EstabelecimentoListarPedidoService {
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Override
    public List<Pedido> estabelecimentoListar(Long pedidoId, Long estabelecimentoListadorId) {
        if (pedidoId != null && pedidoId > 0) {
            Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(PedidoNaoExisteException::new);
            Estabelecimento estabelecimentoPedido = estabelecimentoRepository.findById(pedido.getEstabelecimentoId()).orElseThrow(EstabelecimentoNaoExisteException::new);
            Estabelecimento estabelecimentoListador = estabelecimentoRepository.findById(estabelecimentoListadorId).orElseThrow(EstabelecimentoNaoExisteException::new);

            if (!estabelecimentoPedido.getCodigoAcesso().equals(estabelecimentoListador.getCodigoAcesso())) {
                throw new CodigoDeAcessoInvalidoException();
            }
        }

        if (pedidoId == null) {
            // retorna todos os pedidos do estabelecimento
            List<Pedido> pedidos = new ArrayList<>();
            pedidoRepository.findAll().forEach(pedido -> {
                if (pedido.getEstabelecimentoId().equals(estabelecimentoListadorId)) {
                    pedidos.add(pedido);
                }
            });
            return pedidos;
        } else {
            Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(PedidoNaoExisteException::new);
            return List.of(pedido);
        }
    }
}
