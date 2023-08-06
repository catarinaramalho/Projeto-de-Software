package com.ufcg.psoft.pitsa.controller;

import com.ufcg.psoft.pitsa.dto.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.pitsa.dto.PedidoPostPutRequestDTO;
import com.ufcg.psoft.pitsa.service.pedido.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/v1/pedidos",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class PedidoV1Controller {

    @Autowired
    PedidoCriarService pedidoCriarService;

    @Autowired
    PedidoAlterarService pedidoAlterarService;

    @Autowired
    ClienteExcluirPedidoService clienteExcluirPedidoService;

    @Autowired
    EstabelecimentoExcluirPedidoService estabelecimentoExcluirPedidoService;

    @Autowired
    ClienteListarPedidoService clienteListarPedidoService;

    @Autowired
    EstabelecimentoListarPedidoService estabelecimentoListarPedidoService;

    @Autowired
    EstabelecimentoPrepararPedidoService estabelecimentoPrepararPedidoService;

    @Autowired
    ClienteConfirmarPedidoService clienteConfirmarPedidoService;

    @Autowired
    PedidoConfirmarPagamentoService pedidoConfirmarPagamentoService;

    @Autowired
    EstabelecimentoAssociarPedidoEntregadorService estabelecimentoAssociarPedidoEntregadorService;

    @Autowired
    PedidoCancelarService pedidoCancelarService;

    @Autowired
    ClienteListarPedidoEstabelecimentoService clienteListarPedidoEstabelecimentoService;

    @PostMapping()
    public ResponseEntity<?> criarPedido(
            @RequestParam Long clienteId,
            @RequestParam String clienteCodigoAcesso,
            @RequestParam Long estabelecimentoId,
            @RequestBody @Valid PedidoPostPutRequestDTO pedidoPostPutRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pedidoCriarService.salvar(clienteId, clienteCodigoAcesso, estabelecimentoId, pedidoPostPutRequestDto));
    }

    @PutMapping()
    public ResponseEntity<?> atualizarPedido(
            @RequestParam Long pedidoId,
            @RequestParam String codigoAcesso,
            @RequestBody @Valid PedidoPostPutRequestDTO pedidoPostPutRequestDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pedidoAlterarService.alterar(pedidoId, codigoAcesso, pedidoPostPutRequestDto));
    }

    @DeleteMapping("/{pedidoId}/{clienteId}")
    public ResponseEntity<?> clienteExcluirPedido(
            @PathVariable Long pedidoId,
            @PathVariable Long clienteId) {
        clienteExcluirPedidoService.clienteExcluir(pedidoId, clienteId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }

    @DeleteMapping("")
    public ResponseEntity<?> clienteExcluirTodosPedidos(
            @RequestParam Long clienteId) {
        clienteExcluirPedidoService.clienteExcluir(null, clienteId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }

    @DeleteMapping("/{pedidoId}/{estabelecimentoId}/{estabelecimentoCodigoAcesso}")
    public ResponseEntity<?> estabelecimentoExcluirPedido(
            @PathVariable Long estabelecimentoId,
            @PathVariable Long pedidoId) {
        estabelecimentoExcluirPedidoService.estabelecimentoExcluir(pedidoId, estabelecimentoId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }

    @DeleteMapping("/{estabelecimentoId}")
    public ResponseEntity<?> estabelecimentoExcluirTodosPedidos(
            @PathVariable Long estabelecimentoId) {
        estabelecimentoExcluirPedidoService.estabelecimentoExcluir(null, estabelecimentoId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }

    @GetMapping("")
    public ResponseEntity<?> clienteListarTodosPedidos(
            @RequestParam Long clienteId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteListarPedidoService.clienteListar(null, clienteId));
    }

    @GetMapping("/{pedidoId}/{clienteId}")
    public ResponseEntity<?> clienteListarUmPedido(
            @PathVariable Long pedidoId,
            @PathVariable Long clienteId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteListarPedidoService.clienteListar(pedidoId, clienteId));
    }

    @GetMapping("/{estabelecimentoId}")
    public ResponseEntity<?> estabelecimentoListarTodosPedidos(
            @PathVariable Long estabelecimentoId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoListarPedidoService.estabelecimentoListar(null, estabelecimentoId));
    }

    @GetMapping("/{pedidoId}/{estabelecimentoId}/{estabelecimentoCodigoAcesso}")
    public ResponseEntity<?> estabelecimentoListarUmPedido(
            @PathVariable Long pedidoId,
            @PathVariable Long estabelecimentoId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoListarPedidoService.estabelecimentoListar(pedidoId, estabelecimentoId));
    }

    @PutMapping("/{id}/confirmar-pagamento")
    public ResponseEntity<?> confirmarPagamento(
            @PathVariable Long id,
            @RequestParam String codigoAcessoCliente,
            @RequestParam Long pedidoId,
            @RequestParam String metodoPagamento,
            @RequestBody @Valid PedidoPostPutRequestDTO pedidoPostPutRequestDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pedidoConfirmarPagamentoService.confirmarPagamento(id, pedidoId, metodoPagamento, codigoAcessoCliente, pedidoPostPutRequestDto));
    }

    @DeleteMapping("/{pedidoId}/cancelar-pedido")
    public ResponseEntity<?> cancelarPedido(
            @PathVariable Long pedidoId,
            @RequestParam String clienteCodigoAcesso) {
        pedidoCancelarService.cancelar(pedidoId, clienteCodigoAcesso);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }

    @GetMapping("/pedido-cliente-estabelecimento/{clienteId}/{estabelecimentoId}/{pedidoId}")
    public ResponseEntity<?> clienteListarPedidoEstabelecimento(
            @PathVariable Long pedidoId,
            @PathVariable Long clienteId,
            @PathVariable Long estabelecimentoId,
            @RequestParam String clienteCodigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteListarPedidoEstabelecimentoService.listar(pedidoId, clienteId, estabelecimentoId, clienteCodigoAcesso, null));
    }

    @GetMapping("/pedidos-cliente-estabelecimento/{clienteId}/{estabelecimentoId}")
    public ResponseEntity<?> clienteListarTodosPedidosEstabelecimento(
            @PathVariable Long clienteId,
            @PathVariable Long estabelecimentoId,
            @RequestParam String clienteCodigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteListarPedidoEstabelecimentoService.listar(null, clienteId, estabelecimentoId, clienteCodigoAcesso, null));
    }

    @GetMapping("/pedidos-cliente-estabelecimento/{clienteId}/{estabelecimentoId}/{status}")
    public ResponseEntity<?> clienteListarTodosPedidosEstabelecimento(
            @PathVariable Long clienteId,
            @PathVariable Long estabelecimentoId,
            @PathVariable String status,
            @RequestParam String clienteCodigoAcesso) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteListarPedidoEstabelecimentoService.listar(null, clienteId, estabelecimentoId, clienteCodigoAcesso, status));
    }

    @PutMapping("/{pedidoId}/preparar-pedido")
    public ResponseEntity<?> estabelecimentoPrepararPedido(
            @PathVariable Long pedidoId,
            @RequestParam Long estabelecimentoId,
            @RequestParam String estabelecimentoCodigoAcesso,
            @RequestBody @Valid PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoPrepararPedidoService.prepararPedido(pedidoId, estabelecimentoId, estabelecimentoCodigoAcesso, pedidoPostPutRequestDTO));
    }

    @PutMapping("/{pedidoId}/associar-pedido-entregador")
    public ResponseEntity<?> estabelecimentoAssociaPedidoEntregador(
            @PathVariable Long pedidoId,
            @RequestParam Long estabelecimentoId,
            @RequestParam String estabelecimentoCodigoAcesso,
            @RequestBody @Valid PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoAssociarPedidoEntregadorService.associar(pedidoId, estabelecimentoId, estabelecimentoCodigoAcesso, pedidoPostPutRequestDTO));
    }

    @PutMapping("/{pedidoId}/{clienteId}/cliente-confirmar-entrega")
    public ResponseEntity<?> clienteConfirmaEntrega(
            @PathVariable Long pedidoId,
            @PathVariable Long clienteId,
            @RequestParam String clienteCodigoAcesso,
            @RequestBody @Valid PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteConfirmarPedidoService.confirmar(pedidoId, clienteId, clienteCodigoAcesso, pedidoPostPutRequestDTO));
    }
}
