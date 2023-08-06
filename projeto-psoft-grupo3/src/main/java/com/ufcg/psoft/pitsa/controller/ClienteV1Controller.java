package com.ufcg.psoft.pitsa.controller;

import com.ufcg.psoft.pitsa.dto.ClientePostPutRequestDTO;
import com.ufcg.psoft.pitsa.service.cliente.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/v1/clientes",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class ClienteV1Controller {

    @Autowired
    ClienteListarService clienteListarService;
    @Autowired
    ClienteCriarService clienteCriarService;
    @Autowired
    ClienteAlterarService clienteAlterarService;
    @Autowired
    ClienteExcluirService clienteExcluirService;
    @Autowired
    ClienteDemonstrarInteresseService clienteDemonstrarInteresseService;

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarUmCliente(
            @PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteListarService.listar(id));
    }

    @GetMapping("")
    public ResponseEntity<?> buscarTodosClientes() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteListarService.listar(null));
    }

    @PostMapping()
    public ResponseEntity<?> salvarCliente(
            @RequestBody @Valid ClientePostPutRequestDTO clientePostPutRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clienteCriarService.salvar(clientePostPutRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarCliente(
            @PathVariable Long id,
            @RequestParam String codigoAcesso,
            @RequestBody @Valid ClientePostPutRequestDTO clientePostPutRequestDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteAlterarService.alterar(id, codigoAcesso, clientePostPutRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirCliente(
            @PathVariable Long id,
            @RequestParam String codigoAcesso) {
        clienteExcluirService.excluir(id, codigoAcesso);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }

    @PutMapping("/{id}/demonstrarInteresse")
    public ResponseEntity<?> demonstrarInteresse(
            @PathVariable Long id,
            @RequestParam String codigoAcesso,
            @RequestParam Long saborId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteDemonstrarInteresseService.demonstrarInteresse(id, codigoAcesso, saborId));
    }
}
