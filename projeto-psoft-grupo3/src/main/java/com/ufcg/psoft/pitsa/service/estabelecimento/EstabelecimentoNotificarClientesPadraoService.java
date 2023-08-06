package com.ufcg.psoft.pitsa.service.estabelecimento;

import com.ufcg.psoft.pitsa.exception.*;
import com.ufcg.psoft.pitsa.model.Cliente;
import com.ufcg.psoft.pitsa.model.Estabelecimento;
import com.ufcg.psoft.pitsa.model.Pedido;
import com.ufcg.psoft.pitsa.repository.ClienteRepository;
import com.ufcg.psoft.pitsa.repository.EstabelecimentoRepository;
import com.ufcg.psoft.pitsa.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstabelecimentoNotificarClientesPadraoService implements EstabelecimentoNotificarClientesService {

    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    ClienteRepository clienteRepository;

    @Override
    public String notificar(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(PedidoNaoExisteException::new);
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(pedido.getEstabelecimentoId()).orElseThrow(EstabelecimentoNaoExisteException::new);
        Cliente cliente = clienteRepository.findById(pedido.getClienteId()).orElseThrow(ClienteNaoExisteException::new);

        if (!pedido.getStatusEntrega().equals("Pedido pronto")) {
            throw new PedidoStatusInvalidoException();
        }
        if (estabelecimento.getEntregadoresDisponiveis().size() > 0) {
            throw new EstabelecimentoEntregadorDisponivelException();
        }

        String notificacao = "Olá, " + cliente.getNome() + "! Seu pedido #" + pedido.getId() + " está pronto mas não há entregadores disponíveis.";
        System.out.println(notificacao);
        return notificacao;
    }
}
