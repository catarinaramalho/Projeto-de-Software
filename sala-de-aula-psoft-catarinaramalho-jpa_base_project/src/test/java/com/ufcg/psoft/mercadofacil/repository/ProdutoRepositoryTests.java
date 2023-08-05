package com.ufcg.psoft.mercadofacil.repository;

import com.ufcg.psoft.mercadofacil.model.Produto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@DisplayName("Testes do repositório de Produtos")
public class ProdutoRepositoryTests {

    @Autowired
    ProdutoRepository driver;

    Produto produto;

    @BeforeEach
    void setup() {
        produto = Produto.builder()
                .nome("Produto Dez")
                .fabricante("Fabricante Dez")
                .codigoDeBarras("1234567890123")
                .preco(100.00)
                .build();
    }

    @AfterEach
    void tearDown() {
        driver.deleteAll();
    }

    @Test
    @DisplayName("Quando criar um novo produto com dados válidos")
    void testQuandoCriarProduto() {
        //Arrange
        //Act
        Produto resultado = driver.save(produto);
        //Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals("Produto Dez", resultado.getNome());
        assertEquals("Fabricante Dez", resultado.getFabricante());
        assertEquals("1234567890123", resultado.getCodigoDeBarras());
        assertEquals(100.00, resultado.getPreco());
    }

}
