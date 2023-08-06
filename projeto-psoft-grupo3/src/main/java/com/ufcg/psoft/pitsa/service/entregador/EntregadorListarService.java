package com.ufcg.psoft.pitsa.service.entregador;

import com.ufcg.psoft.pitsa.dto.EntregadorGetRequestDTO;

import java.util.List;

@FunctionalInterface
public interface EntregadorListarService {
    List<EntregadorGetRequestDTO> listar(Long id);
}
