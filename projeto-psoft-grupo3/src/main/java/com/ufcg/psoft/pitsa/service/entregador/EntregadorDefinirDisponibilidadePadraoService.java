package com.ufcg.psoft.pitsa.service.entregador;

import com.ufcg.psoft.pitsa.dto.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.pitsa.exception.*;
import com.ufcg.psoft.pitsa.model.Cliente;
import com.ufcg.psoft.pitsa.model.Entregador;
import com.ufcg.psoft.pitsa.model.Pedido;
import com.ufcg.psoft.pitsa.repository.ClienteRepository;
import com.ufcg.psoft.pitsa.repository.EntregadorRepository;
import com.ufcg.psoft.pitsa.repository.PedidoRepository;
import com.ufcg.psoft.pitsa.service.pedido.PedidoNotificarEstabelecimentoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntregadorDefinirDisponibilidadePadraoService implements EntregadorDefinirDisponibilidadeService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private EntregadorRepository entregadorRepository;

    public Entregador definirDisponibilidade(Long entregadorId, EntregadorPostPutRequestDTO entregadorPostPutRequestDTO, String codigoAcesso, boolean disponibilidade){
        Entregador entregador = entregadorRepository.findById(entregadorId).orElseThrow(EntregadorNaoExisteException::new);

        if (!entregador.getCodigoAcesso().equals(codigoAcesso)) {
            throw new CodigoDeAcessoInvalidoException();
        }

        entregador.setDisponibilidade(disponibilidade);

        modelMapper.map(entregadorPostPutRequestDTO, entregador);
        entregadorRepository.save(entregador);
        return entregador;
    }
}
