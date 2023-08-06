package com.ufcg.psoft.pitsa.service.sabor;

@FunctionalInterface
public interface SaborExcluirService {
    void excluir(Long saborId, Long estabelecimentoId, String estabelecimentoCodigoAcesso);
}
