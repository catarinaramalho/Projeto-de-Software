package com.ufcg.psoft.pitsa.service.estabelecimento;

import com.ufcg.psoft.pitsa.dto.SaborGetRequestDTO;

import java.util.List;

@FunctionalInterface
public interface EstabelecimentoListarCardapioService {
    List<SaborGetRequestDTO> listarCardapio(Long id, String tipo);
}
