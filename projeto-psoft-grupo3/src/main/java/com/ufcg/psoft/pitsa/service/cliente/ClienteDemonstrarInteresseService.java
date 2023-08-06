package com.ufcg.psoft.pitsa.service.cliente;

import com.ufcg.psoft.pitsa.model.Sabor;

@FunctionalInterface
public interface ClienteDemonstrarInteresseService {
    Sabor demonstrarInteresse(Long clienteId, String codigoAcesso, Long saborId);
}
