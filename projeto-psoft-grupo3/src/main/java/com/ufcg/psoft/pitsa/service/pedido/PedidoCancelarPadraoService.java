package com.ufcg.psoft.pitsa.service.pedido;

import com.ufcg.psoft.pitsa.exception.CancelamentoPedidoInvalidoException;
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
public class PedidoCancelarPadraoService implements PedidoCancelarService {
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    ClienteRepository clienteRepository;


    @Override
    public void cancelar(Long pedidoId, String clienteCodigoAcesso) {
        if (pedidoId != null && pedidoId > 0) {
            Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(PedidoNaoExisteException::new);
            Cliente clientePedido = clienteRepository.findById(pedido.getClienteId()).orElseThrow(ClienteNaoExisteException::new);

            if (!clientePedido.getCodigoAcesso().equals(clienteCodigoAcesso)) {
                throw new CodigoDeAcessoInvalidoException();
            }

            if (pedido.getStatusEntrega().equals("Pedido pronto") || pedido.getStatusEntrega().equals("Pedido em rota")) {
                throw new CancelamentoPedidoInvalidoException();
            } else {
                pedidoRepository.deleteById(pedidoId);
            }
        }
    }
}
