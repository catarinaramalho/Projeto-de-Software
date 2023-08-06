package com.ufcg.psoft.pitsa.service.sabor;

import com.ufcg.psoft.pitsa.exception.SaborNaoExisteException;
import com.ufcg.psoft.pitsa.model.Cliente;
import com.ufcg.psoft.pitsa.model.Sabor;
import com.ufcg.psoft.pitsa.repository.ClienteRepository;
import com.ufcg.psoft.pitsa.repository.SaborRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SaborNotificarClientesPadraoService implements SaborNotificarClientesService {

    @Autowired
    SaborRepository saborRepository;
    @Autowired
    ClienteRepository clienteRepository;


    @Override
    public List<String> notificarClientes(Long saborId) {
        Sabor sabor = saborRepository.findById(saborId).orElseThrow(SaborNaoExisteException::new);
        List<String> notificacoes = new ArrayList<>();
        if (sabor.isDisponivel()) {
            for (Cliente cliente : sabor.getClientesInteressados()) {
                if (clienteRepository.existsById(cliente.getId())) {
                    String notificacao = cliente.getNome() + ", o sabor " + sabor.getNome() + " está disponível!";
                    notificacoes.add(notificacao);
                    System.out.println(notificacao);
                }
                sabor.getClientesInteressados().remove(cliente);
            }
            saborRepository.save(sabor);
        }
        return notificacoes;
    }
}
