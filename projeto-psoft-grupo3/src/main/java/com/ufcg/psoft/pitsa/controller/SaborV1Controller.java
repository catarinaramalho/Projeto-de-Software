package com.ufcg.psoft.pitsa.controller;


import com.ufcg.psoft.pitsa.dto.SaborPostPutRequestDTO;
import com.ufcg.psoft.pitsa.service.sabor.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/v1/sabores",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class SaborV1Controller {
    @Autowired
    SaborCriarService saborCriarService;
    @Autowired
    SaborAlterarService saborAlterarService;
    @Autowired
    SaborExcluirService saborExcluirService;
    @Autowired
    SaborListarService saborListarService;
    @Autowired
    SaborAlterarDisponibilidadeService saborAlterarDisponibilidadeService;

    @PostMapping()
    public ResponseEntity<?> criarSabor(
            @RequestBody @Valid SaborPostPutRequestDTO saborPostPutRequestDto,
            @RequestParam Long estabelecimentoId,
            @RequestParam String estabelecimentoCodigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saborCriarService.salvar(saborPostPutRequestDto, estabelecimentoId, estabelecimentoCodigoAcesso));
    }

    @PutMapping()
    public ResponseEntity<?> atualizarSabor(
            @RequestParam Long saborId,
            @RequestBody @Valid SaborPostPutRequestDTO saborPostPutRequestDto,
            @RequestParam Long estabelecimentoId,
            @RequestParam String estabelecimentoCodigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(saborAlterarService.alterar(saborId, saborPostPutRequestDto, estabelecimentoId, estabelecimentoCodigoAcesso));
    }

    @DeleteMapping()
    public ResponseEntity<?> excluirSabor(
            @RequestParam Long saborId,
            @RequestParam Long estabelecimentoId,
            @RequestParam String estabelecimentoCodigoAcesso) {
        saborExcluirService.excluir(saborId, estabelecimentoId, estabelecimentoCodigoAcesso);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }

    @GetMapping("/{saborId}")
    public ResponseEntity<?> buscarUmSabor(
            @PathVariable Long saborId,
            @RequestParam Long estabelecimentoId,
            @RequestParam String estabelecimentoCodigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(saborListarService.listar(saborId, estabelecimentoId, estabelecimentoCodigoAcesso));
    }

    @GetMapping()
    public ResponseEntity<?> buscarTodosSabores(
            @RequestParam Long estabelecimentoId,
            @RequestParam String estabelecimentoCodigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(saborListarService.listar(null, estabelecimentoId, estabelecimentoCodigoAcesso));
    }

    @PutMapping("/{saborId}/{disponibilidade}")
    public ResponseEntity<?> atualizarDisponibilidadeSabor(
            @PathVariable Long saborId,
            @PathVariable boolean disponibilidade,
            @RequestParam Long estabelecimentoId,
            @RequestParam String estabelecimentoCodigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(saborAlterarDisponibilidadeService.alterarDisponibilidade(saborId, estabelecimentoId, estabelecimentoCodigoAcesso, disponibilidade));
    }
}
