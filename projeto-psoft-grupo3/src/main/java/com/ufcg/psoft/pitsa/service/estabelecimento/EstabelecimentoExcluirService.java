package com.ufcg.psoft.pitsa.service.estabelecimento;

import com.ufcg.psoft.pitsa.model.Estabelecimento;

@FunctionalInterface
public interface EstabelecimentoExcluirService {
    Estabelecimento excluir(Long id, String codigoAcesso);
}