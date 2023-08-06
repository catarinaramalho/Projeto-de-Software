package com.ufcg.psoft.pitsa.controller;

import com.ufcg.psoft.pitsa.dto.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.pitsa.service.entregador.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/v1/entregadores",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class EntregadorV1Controller {

    @Autowired
    EntregadorListarService entregadorListarService;
    @Autowired
    EntregadorCriarService entregadorCriarService;
    @Autowired
    EntregadorAlterarService entregadorAlterarService;
    @Autowired
    EntregadorExcluirService entregadorDeletarService;
    @Autowired
    EntregadorDefinirDisponibilidadeService entregadorDefinirDisponibilidadeService;

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarUmEntregador(
            @PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(entregadorListarService.listar(id));
    }

    @GetMapping("")
    public ResponseEntity<?> buscarTodosEntregadores() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(entregadorListarService.listar(null));
    }

    @PostMapping()
    public ResponseEntity<?> salvarEntregador(
            @RequestBody @Valid EntregadorPostPutRequestDTO entregadorPostPutRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(entregadorCriarService.criar(entregadorPostPutRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarEntregador(
            @PathVariable Long id,
            @RequestParam String codigoAcesso,
            @RequestBody @Valid EntregadorPostPutRequestDTO entregadorPostPutRequestDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(entregadorAlterarService.alterar(id, entregadorPostPutRequestDto, codigoAcesso));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirEntregador(
            @PathVariable Long id,
            @RequestParam String codigoAcesso
    ) {
        entregadorDeletarService.excluir(id, codigoAcesso);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }

    @PutMapping("/{id}/disponibilidade")
    public ResponseEntity<?> definirDisponibilidade(
            @PathVariable Long id,
            @RequestParam String codigoAcesso,
            @RequestParam boolean disponibilidade,
            @RequestBody @Valid EntregadorPostPutRequestDTO entregadorPostPutRequestDto
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(entregadorDefinirDisponibilidadeService.definirDisponibilidade(id, entregadorPostPutRequestDto, codigoAcesso, disponibilidade));
    }
}
