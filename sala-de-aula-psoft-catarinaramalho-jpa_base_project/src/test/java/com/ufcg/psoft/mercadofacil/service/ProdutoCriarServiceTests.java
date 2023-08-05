package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.ProdutoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("Testes do Serviço de criação do produto")
public class ProdutoCriarServiceTests {

    @Autowired
    ProdutoCriarService driver;

    @Autowired
    ProdutoRepository produtoRepository;

    Produto produto;

    @BeforeEach
    void setup() {
        produto = produtoRepository.save(Produto.builder()
                .codigoDeBarras("7899137500100")
                .nome("Produto Dez")
                .fabricante("Empresa Dez")
                .preco(450.00)
                .build()
        );
    }

    @AfterEach
    void tearDown() {
        produtoRepository.deleteAll();
    }

    @Test
    @DisplayName("Quando um produto válido é criado")
    void quandoNovoProduto() {
        // Arrange
        produto = produtoRepository.save(Produto.builder()
                .codigoDeBarras("7899137500100")
                .nome("Produto Onze")
                .fabricante("Empresa Onze")
                .preco(450.00)
                .build()
        );

        Produto resultado = driver.criar(produto);

        // Assert
        assertEquals("Produto Onze", resultado.getNome());
    }

    @Test
    @DisplayName("Quando um novo produto nulo for fornecido para o produto")
    void quandoNovoProdutoNull() {
        // Arrange
        produto = null;

        // Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.criar(produto)
        );

        // Assert
        assertEquals("Produto inválido", thrown.getMessage());
    }
}
