package com.ufcg.psoft.pitsa.exception;

public class PitsAException extends RuntimeException {
    public PitsAException() {
        super("Erro inesperado no Pits A!");
    }

    public PitsAException(String message) {
        super(message);
    }
}
