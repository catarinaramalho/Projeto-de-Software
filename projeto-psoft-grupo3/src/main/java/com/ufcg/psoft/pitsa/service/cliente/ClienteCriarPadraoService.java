package com.ufcg.psoft.pitsa.service.cliente;

import com.ufcg.psoft.pitsa.dto.ClientePostPutRequestDTO;
import com.ufcg.psoft.pitsa.model.Cliente;
import com.ufcg.psoft.pitsa.repository.ClienteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteCriarPadraoService implements ClienteCriarService {

    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Cliente salvar(ClientePostPutRequestDTO clientePostPutRequestDTO) {
        Cliente cliente = modelMapper.map(clientePostPutRequestDTO, Cliente.class);
        return clienteRepository.save(cliente);
    }
}
