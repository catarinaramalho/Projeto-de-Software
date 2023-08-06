package com.ufcg.psoft.pitsa.service.estabelecimento;

import com.ufcg.psoft.pitsa.dto.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.pitsa.model.Estabelecimento;
import com.ufcg.psoft.pitsa.model.Pedido;
import com.ufcg.psoft.pitsa.repository.EstabelecimentoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstabelecimentoAlterarFilaPedidoPadraoService implements EstabelecimentoAlterarFilaPedidoService{
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    public void alterarFilaPedidos(Pedido pedido, Estabelecimento estabelecimento, EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO){
        List<Pedido> pedidosPendentes = estabelecimento.getPedidosPendentes();
        pedidosPendentes.add(pedido);
        estabelecimento.setPedidosPendentes(pedidosPendentes);

        modelMapper.map(estabelecimentoPostPutRequestDTO, estabelecimento);
        estabelecimentoRepository.save(estabelecimento);
    }
}
