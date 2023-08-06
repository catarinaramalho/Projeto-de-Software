package com.ufcg.psoft.pitsa.controller;

import com.ufcg.psoft.pitsa.dto.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.pitsa.service.estabelecimento.EstabelecimentoAlterarService;
import com.ufcg.psoft.pitsa.service.estabelecimento.EstabelecimentoCriarService;
import com.ufcg.psoft.pitsa.service.estabelecimento.EstabelecimentoExcluirService;
import com.ufcg.psoft.pitsa.service.estabelecimento.EstabelecimentoListarCardapioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/v1/estabelecimentos",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class EstabelecimentoV1Controller {

    @Autowired
    EstabelecimentoCriarService estabelecimentoCriarService;
    @Autowired
    EstabelecimentoAlterarService estabelecimentoAlterarService;
    @Autowired
    EstabelecimentoExcluirService estabelecimentoExcluirService;
    @Autowired
    EstabelecimentoListarCardapioService estabelecimentoListarCardapioService;

    @PostMapping()
    public ResponseEntity<?> criarEstabelecimento(
            @RequestBody @Valid EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(estabelecimentoCriarService.salvar(estabelecimentoPostPutRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> alterarEstabelecimento(
            @PathVariable Long id,
            @RequestParam String codigoAcesso,
            @RequestBody @Valid EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoAlterarService.alterar(id, codigoAcesso, estabelecimentoPostPutRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirEstabelecimento(
            @PathVariable Long id,
            @RequestParam String codigoAcesso) {
        estabelecimentoExcluirService.excluir(id, codigoAcesso);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }

    @GetMapping("/{id}/sabores")
    public ResponseEntity<?> buscarCardapio(
            @PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoListarCardapioService.listarCardapio(id, null));
    }

    @GetMapping("/{id}/sabores/tipo")
    public ResponseEntity<?> buscarCardapioPorTipo(
            @PathVariable Long id,
            @RequestParam String tipo) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoListarCardapioService.listarCardapio(id, tipo));
    }
}