package com.ufcg.psoft.pitsa.service.estabelecimento;

import com.ufcg.psoft.pitsa.dto.SaborGetRequestDTO;
import com.ufcg.psoft.pitsa.exception.EstabelecimentoNaoExisteException;
import com.ufcg.psoft.pitsa.exception.TipoSaborInvalidoException;
import com.ufcg.psoft.pitsa.model.Estabelecimento;
import com.ufcg.psoft.pitsa.repository.EstabelecimentoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstabelecimentoListarCardapioPadraoService implements EstabelecimentoListarCardapioService {
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<SaborGetRequestDTO> listarCardapio(Long id, String tipo) {
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(id).orElseThrow(EstabelecimentoNaoExisteException::new);
        if (tipo != null) {
            if (!tipo.equals("doce") && !tipo.equals("salgado")) {
                throw new TipoSaborInvalidoException();
            }

            return estabelecimento.getSabores()
                    .stream()
                    .filter(sabor -> sabor.getTipo().equals(tipo))
                    .map(sabor -> modelMapper.map(sabor, SaborGetRequestDTO.class))
                    .sorted(Comparator.comparing(SaborGetRequestDTO::isDisponivel).reversed())
                    .collect(Collectors.toList());

        }

        return estabelecimento.getSabores()
                .stream()
                .map(sabor -> modelMapper.map(sabor, SaborGetRequestDTO.class))
                .sorted(Comparator.comparing(SaborGetRequestDTO::isDisponivel).reversed())
                .collect(Collectors.toList());
    }
}
