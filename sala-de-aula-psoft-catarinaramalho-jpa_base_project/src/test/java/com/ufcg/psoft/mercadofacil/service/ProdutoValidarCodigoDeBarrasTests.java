package com.ufcg.psoft.mercadofacil.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DisplayName("Testes do Serviço de validação do código de barras")
public class ProdutoValidarCodigoDeBarrasTests {

    ProdutoValidarCodigoDeBarras driver;

    @BeforeEach
    void setup() {
        driver = new ProdutoValidarCodigoDeBarrasImpl();
    }

    @Test
    @DisplayName("Validação de código de barras")
    void validarCodigo() {
        assertEquals(driver.validar("7899137500100"), true);
    }

    @Test
    @DisplayName("Validação de código de barras inválido")
    void validarCodigoInvalido() {
        assertEquals(driver.validar("7199137500105"), false);
    }

    @Test
    @DisplayName("Validação de código de barras nulo")
    void validarCodigoNulo() {
        assertEquals(driver.validar(null), false);
    }

    @Test
    @DisplayName("Validação de código de barras com menos de 13 dígitos")
    void validarCodigoMenosDe13Digitos() {
        assertEquals(driver.validar("78991375010"), false);
    }

    @Test
    @DisplayName("Validação de código de barras com mais de 13 dígitos")
    void validarCodigoMaisDe13Digitos() {
        assertEquals(driver.validar("78991375001045"), false);
    }

    @Test
    @DisplayName("Validação de código de barras com dígito verificador inválido")
    void validarCodigoDigitoVerificadorInvalido() {
        assertEquals(driver.validar("7899137500105"), false);
    }
}
