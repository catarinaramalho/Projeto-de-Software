package com.ufcg.psoft.pitsa.exception;

public class ClienteNaoExisteException extends PitsAException {
    public ClienteNaoExisteException() {
        super("O cliente consultado nao existe!");
    }
}
