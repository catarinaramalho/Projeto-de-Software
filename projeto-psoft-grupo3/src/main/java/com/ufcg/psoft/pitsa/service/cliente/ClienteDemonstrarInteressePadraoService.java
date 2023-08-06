package com.ufcg.psoft.pitsa.service.cliente;

import com.ufcg.psoft.pitsa.exception.ClienteNaoExisteException;
import com.ufcg.psoft.pitsa.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.pitsa.exception.SaborJaDisponivelException;
import com.ufcg.psoft.pitsa.exception.SaborNaoExisteException;
import com.ufcg.psoft.pitsa.model.Cliente;
import com.ufcg.psoft.pitsa.model.Sabor;
import com.ufcg.psoft.pitsa.repository.ClienteRepository;
import com.ufcg.psoft.pitsa.repository.SaborRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteDemonstrarInteressePadraoService implements ClienteDemonstrarInteresseService {

    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    SaborRepository saborRepository;

    @Override
    public Sabor demonstrarInteresse(Long clienteId, String codigoAcesso, Long saborId) {
        Sabor sabor = saborRepository.findById(saborId).orElseThrow(SaborNaoExisteException::new);
        if (sabor.isDisponivel()) {
            throw new SaborJaDisponivelException();
        }

        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(ClienteNaoExisteException::new);
        if (!cliente.getCodigoAcesso().equals(codigoAcesso)) {
            throw new CodigoDeAcessoInvalidoException();
        }

        sabor.getClientesInteressados().add(cliente);
        return saborRepository.save(sabor);
    }
}
