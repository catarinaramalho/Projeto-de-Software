package com.ufcg.psoft.pitsa.exception;

public class PedidoNaoExisteException extends PitsAException {
    public PedidoNaoExisteException() {
        super("O pedido consultado nao existe!");
    }

}
