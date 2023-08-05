package com.ufcg.psoft.mercadofacil.repository;

import com.ufcg.psoft.mercadofacil.model.Lote;
import com.ufcg.psoft.mercadofacil.model.Produto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Testes do repositório de Lotes")
class LoteRepositoryTests {

    @Autowired
    LoteRepository driver;

    Lote lote;
    Produto produto;

    @BeforeEach
    void setUp() {
        produto = Produto.builder()
                .nome("Produto Base")
                .codigoDeBarras("123456789")
                .fabricante("Fabricante Base")
                .preco(100.00)
                .build();
        lote = Lote.builder()
                .produto(produto)
                .numeroDeItens(100)
                .build();
    }

    @AfterEach
    void tearDown() {
        driver.deleteAll();
    }

    @Test
    @DisplayName("Inserir o primeiro lote de produtos no banco de dados")
    void inserirPrimeiroLoteNoBD() {
        // Arrange
        driver.deleteAll();
        // Act
        Lote resultado = driver.save(lote);
        // Assert
        assertNotNull(resultado);
        assertEquals(1, driver.findAll().size());
        assertEquals(produto, resultado.getProduto());
    }

    @Test
    @DisplayName("Inserir o segudo ou posterior lote de produtos no banco")
    void inserirSegundoOuPosteriorLoteDeProdutosNoBanco() {
        // Arrange
        driver.deleteAll();
        Produto produto2 = Produto.builder()
                .nome("Produto Dois")
                .codigoDeBarras("987654321")
                .fabricante("Fabricante Dois")
                .preco(200.00)
                .build();
        Lote lote2 = Lote.builder()
                .produto(produto2)
                .numeroDeItens(200)
                .build();
        driver.save(lote);

        // Act
        Lote resultado = driver.save(lote2);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, driver.findAll().size());
        assertEquals(produto2, resultado.getProduto());

    }

}