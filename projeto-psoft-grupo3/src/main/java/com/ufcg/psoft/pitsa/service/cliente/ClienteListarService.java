package com.ufcg.psoft.pitsa.service.cliente;

import com.ufcg.psoft.pitsa.dto.ClienteGetRequestDTO;

import java.util.List;

@FunctionalInterface
public interface ClienteListarService {
    List<ClienteGetRequestDTO> listar(Long id);
}
