package com.ufcg.psoft.pitsa.service.entregador;

import com.ufcg.psoft.pitsa.dto.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.pitsa.dto.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.pitsa.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.pitsa.exception.EntregadorNaoExisteException;
import com.ufcg.psoft.pitsa.exception.EstabelecimentoNaoExisteException;
import com.ufcg.psoft.pitsa.model.Entregador;
import com.ufcg.psoft.pitsa.model.Estabelecimento;
import com.ufcg.psoft.pitsa.repository.EntregadorRepository;
import com.ufcg.psoft.pitsa.repository.EstabelecimentoRepository;
import com.ufcg.psoft.pitsa.service.estabelecimento.EstabelecimentoAlterarFilaEntregadoresService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstabelecimentoAprovarEntregadorPadraoService implements EstabelecimentoAprovarEntregadorService{
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    EntregadorRepository entregadorRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    EstabelecimentoAlterarFilaEntregadoresService estabelecimentoAlterarFilaEntregadoresService;
    @Override
    public void aprovar(Long entregadorId, EntregadorPostPutRequestDTO entregadorPostPutRequestDTO, String codigoAcessoEstabelecimento, Long estabelecimentoId){
        Entregador entregador = entregadorRepository.findById(entregadorId).orElseThrow(EntregadorNaoExisteException::new);
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(estabelecimentoId).orElseThrow(EstabelecimentoNaoExisteException::new);

        if (!estabelecimento.getCodigoAcesso().equals(codigoAcessoEstabelecimento)) {
            throw new CodigoDeAcessoInvalidoException();
        }

        entregador.setStatusAprovacao(true);
        entregador.setDisponibilidade(false);

        List<Entregador> entregadoresDisponiveis = estabelecimento.getEntregadoresDisponiveis();
        entregadoresDisponiveis.add(entregador);
        estabelecimento.setEntregadoresDisponiveis(entregadoresDisponiveis);

        modelMapper.map(estabelecimento, estabelecimentoRepository.findById(estabelecimento.getId()).get());
        estabelecimentoRepository.save(estabelecimento);
        modelMapper.map(entregadorPostPutRequestDTO, entregador);
        entregadorRepository.save(entregador);
    }

}
