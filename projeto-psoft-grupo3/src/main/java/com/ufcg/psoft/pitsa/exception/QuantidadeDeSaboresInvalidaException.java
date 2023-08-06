package com.ufcg.psoft.pitsa.exception;

public class QuantidadeDeSaboresInvalidaException extends PitsAException {
    public QuantidadeDeSaboresInvalidaException() {
        super("Quantidade de sabores invalida, pizzas medias so podem ter 1 sabor!");
    }
}
