package com.ufcg.psoft.pitsa.service.cliente;

@FunctionalInterface
public interface ClienteExcluirService {
    void excluir(Long id, String codigoAcesso);
}
