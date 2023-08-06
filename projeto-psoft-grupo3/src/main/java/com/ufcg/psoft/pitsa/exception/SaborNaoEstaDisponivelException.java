package com.ufcg.psoft.pitsa.exception;

public class SaborNaoEstaDisponivelException extends PitsAException {
    public SaborNaoEstaDisponivelException() {
        super("O sabor consultado nao esta disponivel!");
    }
}
