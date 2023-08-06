package com.ufcg.psoft.pitsa.service.associacao;

import com.ufcg.psoft.pitsa.model.Associacao;

@FunctionalInterface
public interface AssociacaoCriarService {
    Associacao associar(Long entregadorId, String codigoAcesso, Long estabelecimentoId);
}
