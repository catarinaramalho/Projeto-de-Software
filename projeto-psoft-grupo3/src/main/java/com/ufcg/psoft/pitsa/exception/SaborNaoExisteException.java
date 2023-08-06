package com.ufcg.psoft.pitsa.exception;

public class SaborNaoExisteException extends PitsAException {
    public SaborNaoExisteException() {
        super("O sabor consultado nao existe!");
    }
}
