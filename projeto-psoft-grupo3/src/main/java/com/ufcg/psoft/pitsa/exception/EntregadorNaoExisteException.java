package com.ufcg.psoft.pitsa.exception;


public class EntregadorNaoExisteException extends PitsAException {
    public EntregadorNaoExisteException() {
        super("O entregador consultado nao existe!");
    }
}
