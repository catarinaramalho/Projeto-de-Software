package com.ufcg.psoft.pitsa.service.entregador;

import com.ufcg.psoft.pitsa.dto.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.pitsa.dto.EstabelecimentoPostPutRequestDTO;

@FunctionalInterface
public interface EstabelecimentoAprovarEntregadorService {
    void aprovar(Long entregadorId, EntregadorPostPutRequestDTO entregadorPostPutRequestDTO, String codigoAcessoEstabelecimento, Long estabelecimentoId);
}
