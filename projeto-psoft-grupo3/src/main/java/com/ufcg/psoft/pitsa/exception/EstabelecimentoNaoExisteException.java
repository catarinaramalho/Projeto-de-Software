package com.ufcg.psoft.pitsa.exception;

public class EstabelecimentoNaoExisteException extends PitsAException {
    public EstabelecimentoNaoExisteException() {
        super("O estabelecimento consultado nao existe!");
    }
}
