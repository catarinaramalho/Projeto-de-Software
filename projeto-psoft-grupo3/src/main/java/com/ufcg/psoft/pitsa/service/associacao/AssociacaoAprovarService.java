package com.ufcg.psoft.pitsa.service.associacao;

import com.ufcg.psoft.pitsa.model.Associacao;

@FunctionalInterface
public interface AssociacaoAprovarService {
    Associacao aprovar(Long entregadorId, Long estabelecimentoId, String codigoAcesso);
}
