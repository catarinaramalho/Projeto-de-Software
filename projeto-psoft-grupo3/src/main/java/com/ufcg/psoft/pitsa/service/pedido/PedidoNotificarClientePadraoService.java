package com.ufcg.psoft.pitsa.service.pedido;

import com.ufcg.psoft.pitsa.exception.ClienteNaoExisteException;
import com.ufcg.psoft.pitsa.exception.EntregadorNaoExisteException;
import com.ufcg.psoft.pitsa.exception.PedidoNaoExisteException;
import com.ufcg.psoft.pitsa.exception.PedidoStatusInvalidoException;
import com.ufcg.psoft.pitsa.model.Cliente;
import com.ufcg.psoft.pitsa.model.Entregador;
import com.ufcg.psoft.pitsa.model.Pedido;
import com.ufcg.psoft.pitsa.repository.ClienteRepository;
import com.ufcg.psoft.pitsa.repository.EntregadorRepository;
import com.ufcg.psoft.pitsa.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoNotificarClientePadraoService implements PedidoNotificarClienteService {

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    EntregadorRepository entregadorRepository;

    @Override
    public String notificar(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(PedidoNaoExisteException::new);
        if (!pedido.getStatusEntrega().equals("Pedido em rota")) {
            throw new PedidoStatusInvalidoException();
        }
        Cliente cliente = clienteRepository.findById(pedido.getClienteId()).orElseThrow(ClienteNaoExisteException::new);
        Entregador entregador = entregadorRepository.findById(pedido.getEntregadorId()).orElseThrow(EntregadorNaoExisteException::new);

        String notificacao = cliente.getNome() + ", o seu pedido #" + pedido.getId() + ", está em rota de entrega!\n"
                + "O entregador " + entregador.getNome() + " está a caminho do seu endereço!\n"
                + "Tipo do Veículo: " + entregador.getTipoVeiculo() + "\n"
                + "Placa do Veículo: " + entregador.getPlacaVeiculo() + "\n"
                + "Cor do Veículo: " + entregador.getCorVeiculo();
        System.out.println(notificacao);
        return notificacao;
    }
}
