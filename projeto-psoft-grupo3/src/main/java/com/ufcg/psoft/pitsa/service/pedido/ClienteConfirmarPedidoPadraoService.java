package com.ufcg.psoft.pitsa.service.pedido;

import com.ufcg.psoft.pitsa.dto.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.pitsa.dto.PedidoPostPutRequestDTO;
import com.ufcg.psoft.pitsa.exception.*;
import com.ufcg.psoft.pitsa.model.Cliente;
import com.ufcg.psoft.pitsa.model.Entregador;
import com.ufcg.psoft.pitsa.model.Estabelecimento;
import com.ufcg.psoft.pitsa.model.Pedido;
import com.ufcg.psoft.pitsa.repository.ClienteRepository;
import com.ufcg.psoft.pitsa.repository.EntregadorRepository;
import com.ufcg.psoft.pitsa.repository.EstabelecimentoRepository;
import com.ufcg.psoft.pitsa.repository.PedidoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteConfirmarPedidoPadraoService implements ClienteConfirmarPedidoService {

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    PedidoNotificarEstabelecimentoService pedidoNotificarEstabelecimentoService;
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    EntregadorRepository entregadorRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Override
    public Pedido confirmar(Long pedidoId, Long clienteId, String codigoAcessoCliente, PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(ClienteNaoExisteException::new);

        if (!cliente.getCodigoAcesso().equals(codigoAcessoCliente)) {
            throw new CodigoDeAcessoInvalidoException();
        }

        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(PedidoNaoExisteException::new);

        if (!pedido.getStatusEntrega().equals("Pedido em rota")) {
            throw new PedidoStatusInvalidoException();
        }

        // todo alteracoes feitas: mds vou morrerr! gente nao to entendendo mais nada to entendendo agora

        Entregador entregador = entregadorRepository.findById(pedido.getEntregadorId()).orElseThrow(EntregadorNaoExisteException::new);

        if (entregador.isDisponibilidade()) {
            Estabelecimento estabelecimento = estabelecimentoRepository.findById(pedido.getEstabelecimentoId()).orElseThrow(EstabelecimentoNaoExisteException::new);
            List<Entregador> entregadoresDisponiveis = estabelecimento.getEntregadoresDisponiveis();
            entregadoresDisponiveis.add(entregador);
            estabelecimento.setEntregadoresDisponiveis(entregadoresDisponiveis);
            modelMapper.map(estabelecimento, estabelecimentoRepository.findById(estabelecimento.getId()).get());
            estabelecimentoRepository.save(estabelecimento);
        }

        pedido.setStatusEntrega("Pedido entregue");

        modelMapper.map(pedidoPostPutRequestDTO, pedido);
        pedidoRepository.save(pedido);
        pedidoNotificarEstabelecimentoService.notificar(pedidoId);
        return pedido;
    }
}
