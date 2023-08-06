package com.ufcg.psoft.pitsa.service.pedido;

import com.ufcg.psoft.pitsa.dto.PedidoPostPutRequestDTO;
import com.ufcg.psoft.pitsa.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.pitsa.exception.EstabelecimentoNaoExisteException;
import com.ufcg.psoft.pitsa.exception.PedidoNaoExisteException;
import com.ufcg.psoft.pitsa.exception.PedidoStatusInvalidoException;
import com.ufcg.psoft.pitsa.model.Entregador;
import com.ufcg.psoft.pitsa.model.Estabelecimento;
import com.ufcg.psoft.pitsa.model.Pedido;
import com.ufcg.psoft.pitsa.repository.EntregadorRepository;
import com.ufcg.psoft.pitsa.repository.EstabelecimentoRepository;
import com.ufcg.psoft.pitsa.repository.PedidoRepository;
import com.ufcg.psoft.pitsa.service.estabelecimento.EstabelecimentoNotificarClientesService;
import com.ufcg.psoft.pitsa.service.estabelecimento.EstabelecimentoNotificarPedidoProntoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstabelecimentoAssociarPedidoEntregadorPadraoService implements EstabelecimentoAssociarPedidoEntregadorService {

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    EntregadorRepository entregadorRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    EstabelecimentoNotificarClientesService estabelecimentoNotificarClientesService;


    @Override
    public Pedido associar(Long pedidoId, Long estabelecimentoId, String codigoAcessoEstabelecimento, PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(estabelecimentoId).orElseThrow(EstabelecimentoNaoExisteException::new);

        if (!estabelecimento.getCodigoAcesso().equals(codigoAcessoEstabelecimento)) {
            throw new CodigoDeAcessoInvalidoException();
        }

        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(PedidoNaoExisteException::new);

        if (!pedido.getStatusEntrega().equals("Pedido pronto")) {
            throw new PedidoStatusInvalidoException();
        }

        List<Entregador> entregadoresDisponiveis = estabelecimento.getEntregadoresDisponiveis();
        List<Pedido> pedidosPendentes = estabelecimento.getPedidosPendentes();

        if (entregadoresDisponiveis.isEmpty()) {
            estabelecimentoNotificarClientesService.notificar(pedidoId);
        } else {
            for (Entregador entregador : entregadoresDisponiveis) {
                if (entregador.isDisponibilidade()) {
                    pedido.setEntregadorId(entregador.getId());
                    entregadoresDisponiveis.remove(entregador);
                    estabelecimento.setEntregadoresDisponiveis(entregadoresDisponiveis);
                    pedidosPendentes.remove(pedido);
                    estabelecimento.setPedidosPendentes(pedidosPendentes);
                    break;
                }
            }

            pedido.setStatusEntrega("Pedido em rota");
            modelMapper.map(estabelecimento, estabelecimentoRepository.findById(estabelecimento.getId()).get());
            estabelecimentoRepository.save(estabelecimento);
            modelMapper.map(pedidoPostPutRequestDTO, pedido);
            pedidoRepository.save(pedido);
        }
        return pedido;
    }
}
