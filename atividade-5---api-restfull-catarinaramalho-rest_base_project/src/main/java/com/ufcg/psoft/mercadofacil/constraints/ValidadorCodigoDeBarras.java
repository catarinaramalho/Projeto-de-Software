package com.ufcg.psoft.mercadofacil.constraints;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidadorCodigoDeBarras implements ConstraintValidator<CodigoDeBarras, String> {

    @Override
    public boolean isValid(String codigo, ConstraintValidatorContext context) {
        if (codigo == null || codigo.length() != 13 || !codigo.substring(0, 8).equals("78991375")) {
            return false;
        }
        int[] numeros = codigo.chars().map(Character::getNumericValue).toArray();
        int somaPares = numeros[1] + numeros[3] + numeros[5] + numeros[7] + numeros[9] + numeros[11];
        int somaImpares = numeros[0] + numeros[2] + numeros[4] + numeros[6] + numeros[8] + numeros[10];
        int resultado = somaImpares + somaPares * 3;
        int digitoVerificador = (10 - resultado % 10) % 10;
        return digitoVerificador == numeros[12];
    }

}
