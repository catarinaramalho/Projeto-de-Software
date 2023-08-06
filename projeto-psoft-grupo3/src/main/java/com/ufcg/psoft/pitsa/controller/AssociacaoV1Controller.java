package com.ufcg.psoft.pitsa.controller;

import com.ufcg.psoft.pitsa.service.associacao.AssociacaoAprovarService;
import com.ufcg.psoft.pitsa.service.associacao.AssociacaoCriarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/v1/associacao",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class AssociacaoV1Controller {

    @Autowired
    AssociacaoCriarService associacaoCriarService;
    @Autowired
    AssociacaoAprovarService associacaoAprovarService;

    @PostMapping()
    public ResponseEntity<?> salvarAssociacao(
            @RequestParam Long entregadorId,
            @RequestParam String codigoAcesso,
            @RequestParam Long estabelecimentoId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(associacaoCriarService.associar(entregadorId, codigoAcesso, estabelecimentoId));
    }

    @PutMapping()
    public ResponseEntity<?> aprovarAssociacao(
            @RequestParam Long entregadorId,
            @RequestParam Long estabelecimentoId,
            @RequestParam String codigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(associacaoAprovarService.aprovar(entregadorId, estabelecimentoId, codigoAcesso));
    }
}
