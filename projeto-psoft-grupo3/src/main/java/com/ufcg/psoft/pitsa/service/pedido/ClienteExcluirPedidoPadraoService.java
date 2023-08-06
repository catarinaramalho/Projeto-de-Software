package com.ufcg.psoft.pitsa.service.pedido;

import com.ufcg.psoft.pitsa.exception.ClienteNaoExisteException;
import com.ufcg.psoft.pitsa.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.pitsa.exception.PedidoNaoExisteException;
import com.ufcg.psoft.pitsa.model.Cliente;
import com.ufcg.psoft.pitsa.model.Pedido;
import com.ufcg.psoft.pitsa.repository.ClienteRepository;
import com.ufcg.psoft.pitsa.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteExcluirPedidoPadraoService implements ClienteExcluirPedidoService {
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    ClienteRepository clienteRepository;

    public void clienteExcluir(Long pedidoId, Long clienteExclusorId) {
        if (pedidoId != null && pedidoId > 0) {
            Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(PedidoNaoExisteException::new);
            Cliente clientePedido = clienteRepository.findById(pedido.getClienteId()).orElseThrow(ClienteNaoExisteException::new);
            Cliente clienteExclusor = clienteRepository.findById(clienteExclusorId).orElseThrow(ClienteNaoExisteException::new);

            if (!clientePedido.getCodigoAcesso().equals(clienteExclusor.getCodigoAcesso())) {
                throw new CodigoDeAcessoInvalidoException();
            }
        }

        if (pedidoId == null) {
            pedidoRepository.findAll().forEach(pedido -> {
                if (pedido.getClienteId().equals(clienteExclusorId)) {
                    pedidoRepository.deleteById(pedido.getId());
                }
            });

        } else {
            pedidoRepository.deleteById(pedidoId);
        }
    }
}
