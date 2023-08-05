package com.ufcg.psoft.mercadofacil.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ValidadorCodigoDeBarras.class)
public @interface CodigoDeBarras{

    String message() default "Codigo de Barras invalido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}
