package com.ufcg.psoft.pitsa.service.pedido;


import com.ufcg.psoft.pitsa.dto.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.pitsa.dto.PedidoPostPutRequestDTO;
import com.ufcg.psoft.pitsa.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.pitsa.exception.EstabelecimentoNaoExisteException;
import com.ufcg.psoft.pitsa.exception.PedidoNaoExisteException;
import com.ufcg.psoft.pitsa.exception.PedidoStatusInvalidoException;
import com.ufcg.psoft.pitsa.model.Estabelecimento;
import com.ufcg.psoft.pitsa.model.Pedido;
import com.ufcg.psoft.pitsa.repository.EstabelecimentoRepository;
import com.ufcg.psoft.pitsa.repository.PedidoRepository;
import com.ufcg.psoft.pitsa.service.estabelecimento.EstabelecimentoAlterarFilaPedidoService;
import com.ufcg.psoft.pitsa.service.estabelecimento.EstabelecimentoNotificarPedidoProntoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class EstabelecimentoPrepararPedidoPadraoService implements EstabelecimentoPrepararPedidoService {
    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    EstabelecimentoAlterarFilaPedidoService estabelecimentoAlterarFilaPedidoService;

    @Autowired
    EstabelecimentoNotificarPedidoProntoService estabelecimentoNotificarPedidoProntoService;

    @Override
    public Pedido prepararPedido(Long pedidoId, Long estabelecimentoId, String codigoAcessoEstabelecimento, PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(estabelecimentoId).orElseThrow(EstabelecimentoNaoExisteException::new);
        if (!estabelecimento.getCodigoAcesso().equals(codigoAcessoEstabelecimento)) {
            throw new CodigoDeAcessoInvalidoException();
        }

        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(PedidoNaoExisteException::new);

        if (!pedido.getStatusEntrega().equals("Pedido em preparo")) {
            throw new PedidoStatusInvalidoException();
        }

        pedido.setStatusEntrega("Pedido pronto");
        estabelecimentoAlterarFilaPedidoService.alterarFilaPedidos(pedido, estabelecimento,  new EstabelecimentoPostPutRequestDTO(estabelecimento));

        modelMapper.map(pedidoPostPutRequestDTO, pedido);
        estabelecimentoNotificarPedidoProntoService.notificar(pedido.getId());
        return pedidoRepository.save(pedido);
    }
}
