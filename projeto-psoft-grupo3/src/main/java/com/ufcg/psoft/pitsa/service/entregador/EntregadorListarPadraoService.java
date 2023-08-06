package com.ufcg.psoft.pitsa.service.entregador;

import com.ufcg.psoft.pitsa.dto.EntregadorGetRequestDTO;
import com.ufcg.psoft.pitsa.exception.EntregadorNaoExisteException;
import com.ufcg.psoft.pitsa.model.Entregador;
import com.ufcg.psoft.pitsa.repository.EntregadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EntregadorListarPadraoService implements EntregadorListarService {
    @Autowired
    EntregadorRepository entregadorRepository;

    @Override
    public List<EntregadorGetRequestDTO> listar(Long id) {
        if (id != null && id > 0) {
            Entregador entregador = entregadorRepository.findById(id).orElseThrow(EntregadorNaoExisteException::new);
            return List.of(new EntregadorGetRequestDTO(entregador));
        }
        List<Entregador> entregadores = entregadorRepository.findAll();

        return entregadores.stream()
                .map(EntregadorGetRequestDTO::new)
                .collect(Collectors.toList());
    }
}
