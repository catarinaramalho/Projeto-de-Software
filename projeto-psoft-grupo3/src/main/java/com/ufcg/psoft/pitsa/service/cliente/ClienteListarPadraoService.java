package com.ufcg.psoft.pitsa.service.cliente;

import com.ufcg.psoft.pitsa.dto.ClienteGetRequestDTO;
import com.ufcg.psoft.pitsa.exception.ClienteNaoExisteException;
import com.ufcg.psoft.pitsa.model.Cliente;
import com.ufcg.psoft.pitsa.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteListarPadraoService implements ClienteListarService {

    @Autowired
    ClienteRepository clienteRepository;

    @Override
    public List<ClienteGetRequestDTO> listar(Long id) {
        if (id != null && id > 0) {
            Cliente cliente = clienteRepository.findById(id).orElseThrow(ClienteNaoExisteException::new);
            return List.of(new ClienteGetRequestDTO(cliente));
        }

        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                .map(ClienteGetRequestDTO::new)
                .collect(Collectors.toList());
    }

}
