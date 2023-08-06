package com.ufcg.psoft.pitsa.service.entregador;

@FunctionalInterface
public interface EntregadorExcluirService {
    void excluir(Long id, String codigoAcesso);
}
