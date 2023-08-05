package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.dto.LotePostPutRequestDTO;
import com.ufcg.psoft.mercadofacil.model.Lote;

@FunctionalInterface
public interface LoteCriarService {
    Lote salvar(LotePostPutRequestDTO lotePostPutRequestDTO);
}
