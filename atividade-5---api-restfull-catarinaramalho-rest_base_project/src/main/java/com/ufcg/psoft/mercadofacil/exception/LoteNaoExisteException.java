package com.ufcg.psoft.mercadofacil.exception;

public class LoteNaoExisteException extends MercadoFacilException {
    public LoteNaoExisteException() {

        super("O Lote consultado nao existe!");
    }
}