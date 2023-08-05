package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.Lote;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.LoteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("Testes do Serviço de criação do lote")
public class LoteCriarServiceTests {

    @Autowired
    LoteCriarService driver;

    @Autowired
    LoteRepository loteRepository;

    Lote lote;
    Produto produto;

    @BeforeEach
    void setup() {
        produto = Produto.builder()
                .codigoDeBarras("7899137500100")
                .nome("Produto Dez")
                .fabricante("Empresa Dez")
                .preco(450.00)
                .build();
        lote = loteRepository.save(Lote.builder().produto(produto).numeroDeItens(400).build());
    }

    @AfterEach
    void tearDown() {
        loteRepository.deleteAll();
    }

    @Test
    @DisplayName("Quando um novo lote nulo for fornecido")
    void quandoNovoLoteNull() {
        // Arrange
        lote = null;

        // Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.criar(lote)
        );

        // Assert
        assertEquals("Lote inválido", thrown.getMessage());
    }
}
