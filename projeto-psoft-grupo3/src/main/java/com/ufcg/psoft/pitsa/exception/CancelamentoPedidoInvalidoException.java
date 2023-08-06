package com.ufcg.psoft.pitsa.exception;

public class CancelamentoPedidoInvalidoException extends PitsAException {
    public CancelamentoPedidoInvalidoException() {
        super("Pedidos que ja estao prontos nao podem ser cancelados!");
    }
}