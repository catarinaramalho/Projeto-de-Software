package com.ufcg.psoft.pitsa.service.pedido;

import com.ufcg.psoft.pitsa.dto.PedidoPostPutRequestDTO;
import com.ufcg.psoft.pitsa.exception.ClienteNaoExisteException;
import com.ufcg.psoft.pitsa.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.pitsa.exception.PedidoNaoExisteException;
import com.ufcg.psoft.pitsa.exception.PedidoStatusInvalidoException;
import com.ufcg.psoft.pitsa.model.Cliente;
import com.ufcg.psoft.pitsa.model.Pedido;
import com.ufcg.psoft.pitsa.repository.ClienteRepository;
import com.ufcg.psoft.pitsa.repository.PedidoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoConfirmarPagamentoPadraoService implements PedidoConfirmarPagamentoService {
    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public Pedido confirmarPagamento(Long clienteId, Long pedidoId, String metodoPagamento, String codigoAcessoCliente, PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(ClienteNaoExisteException::new);
        if (!cliente.getCodigoAcesso().equals(codigoAcessoCliente)) {
            throw new CodigoDeAcessoInvalidoException();
        }

        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(PedidoNaoExisteException::new);

        if (!pedido.getStatusEntrega().equals("Pedido recebido")) {
            throw new PedidoStatusInvalidoException();
        }

        if (metodoPagamento.equals("PIX")) {
            pedido.setPreco(pedido.getPreco() * 0.95);
        } else if (metodoPagamento.equals("Cartão de débito")) {
            pedido.setPreco(pedido.getPreco() * 0.975);
        }

        pedido.setStatusPagamento(true);
        pedido.setStatusEntrega("Pedido em preparo");
        modelMapper.map(pedidoPostPutRequestDTO, pedido);
        return pedidoRepository.save(pedido);
    }
}
