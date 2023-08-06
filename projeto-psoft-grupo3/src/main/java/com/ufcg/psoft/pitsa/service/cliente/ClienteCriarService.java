package com.ufcg.psoft.pitsa.service.cliente;

import com.ufcg.psoft.pitsa.dto.ClientePostPutRequestDTO;
import com.ufcg.psoft.pitsa.model.Cliente;

@FunctionalInterface
public interface ClienteCriarService {
    Cliente salvar(ClientePostPutRequestDTO clientePostPutRequestDTO);
}
