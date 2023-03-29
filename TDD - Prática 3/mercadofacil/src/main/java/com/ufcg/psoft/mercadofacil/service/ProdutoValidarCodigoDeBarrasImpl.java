package com.ufcg.psoft.mercadofacil.service;

public class ProdutoValidarCodigoDeBarrasImpl implements ProdutoValidarCodigoDeBarras {
    @Override
    public Boolean validar(String codigo) {
        if (!codigo.matches("^[0-9]{13}$") || !codigo.substring(0, 7).equals("78991375") || codigo.substring(8,11).equals("0000") || codigo.length() > 13 ) {
            return false;
        }
        int[] numeros = codigo.chars().map(Character::getNumericValue).toArray();
        int somaPares = numeros[1] + numeros[3] + numeros[5] + numeros[7] + numeros[9] + numeros[11];
        int somaImpares = numeros[0] + numeros[2] + numeros[4] + numeros[6] + numeros[8] + numeros[10];
        int resultado = somaImpares + somaPares * 3;
        int digitoVerificador = 10 - resultado % 10;
        return digitoVerificador == numeros[12];
    }
}