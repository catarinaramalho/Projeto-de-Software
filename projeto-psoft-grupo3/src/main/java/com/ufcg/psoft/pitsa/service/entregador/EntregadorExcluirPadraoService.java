package com.ufcg.psoft.pitsa.service.entregador;

import com.ufcg.psoft.pitsa.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.pitsa.exception.EntregadorNaoExisteException;
import com.ufcg.psoft.pitsa.model.Entregador;
import com.ufcg.psoft.pitsa.repository.EntregadorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntregadorExcluirPadraoService implements EntregadorExcluirService {
    @Autowired
    EntregadorRepository entregadorRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public void excluir(Long id, String codigoAcesso) {
        Entregador entregador = entregadorRepository.findById(id).orElseThrow(EntregadorNaoExisteException::new);
        if (!entregador.getCodigoAcesso().equals(codigoAcesso)) {
            throw new CodigoDeAcessoInvalidoException();
        }
        entregadorRepository.delete(entregador);
    }
}
