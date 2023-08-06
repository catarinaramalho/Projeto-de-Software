package com.ufcg.psoft.pitsa.exception;

public class AssociacaoNaoExisteException extends RuntimeException {
    public AssociacaoNaoExisteException() {
        super("A associacao nao existe!");
    }
}
