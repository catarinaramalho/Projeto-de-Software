package com.ufcg.psoft.mercadofacil.controller;

import com.ufcg.psoft.mercadofacil.dto.ProdutoNomePatchRequestDTO;
import com.ufcg.psoft.mercadofacil.dto.ProdutoPostPutRequestDTO;
import com.ufcg.psoft.mercadofacil.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/v1/produtos",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class ProdutoV1Controller {

    @Autowired
    ProdutoListarService produtoListarService;
    @Autowired
    ProdutoCriarService produtoCriarService;
    @Autowired
    ProdutoAlterarService produtoAlterarService;
    @Autowired
    ProdutoAlterarNomeService produtoAlterarNomeService;
    @Autowired
    ProdutoExcluirService produtoExcluirService;

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarUmProduto(
            @PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(produtoListarService.listar(id));
    }

    @GetMapping("")
    public ResponseEntity<?> buscarTodosProdutos() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(produtoListarService.listar(null));
    }

    @PostMapping()
    public ResponseEntity<?> salvarProduto(
            @RequestBody @Valid ProdutoPostPutRequestDTO produtoPostPutRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(produtoCriarService.salvar(produtoPostPutRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarProduto(
            @PathVariable Long id,
            @RequestBody @Valid ProdutoPostPutRequestDTO produtoPostPutRequestDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(produtoAlterarService.alterar(id, produtoPostPutRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirProduto(
            @PathVariable Long id
    ) {
        produtoExcluirService.excluir(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> atualizarParcialmenteProduto(
            @PathVariable Long id,
            @RequestBody @Valid ProdutoNomePatchRequestDTO produtoNomePatchRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(produtoAlterarNomeService.alterarParcialmente(id, produtoNomePatchRequestDTO));
    }

}
