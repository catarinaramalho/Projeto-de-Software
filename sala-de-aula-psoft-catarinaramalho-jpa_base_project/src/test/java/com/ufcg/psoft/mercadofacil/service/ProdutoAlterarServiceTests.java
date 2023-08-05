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
@DisplayName("Testes do Serviço de alteração do produto")
public class ProdutoAlterarServiceTests {

    @Autowired
    ProdutoAlterarService driver;

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
    @DisplayName("Quando um novo nome válido for fornecido para o produto")
    void quandoNovoNomeValido() {
        // Arrange
        produto.setNome("Produto Dez Atualizado");

        // Act
        Produto resultado = driver.alterar(produto);

        // Assert
        assertEquals("Produto Dez Atualizado", resultado.getNome());
    }

    @Test
    @DisplayName("Quando um novo nome nulo for fornecido para o produto")
    void quandoNovoNomeNull() {
        // Arrange
        produto.setNome(null);

        // Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(produto)
        );

        // Assert
        assertEquals("Nome inválido", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando um novo nome vazio for fornecido para o produto")
    void quandoNovoNomeVazio() {
        // Arrange
        produto.setNome(" ");

        // Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(produto)
        );

        // Assert
        assertEquals("Nome inválido", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando um novo id nulo for fornecido para o produto")
    void quandoNovoIdNull() {
        // Arrange
        produto.setId(null);

        // Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(produto)
        );

        // Assert
        assertEquals("Id inválido", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando o preço é igual a zero")
    void quandoPrecoIgualZero() {
        // Arrange
        produto.setPreco(0.0);

        // Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(produto)
        );

        // Assert
        assertEquals("Preço inválido", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando o preço é menor que zero")
    void quandoPrecoMenorZero() {
        // Arrange
        produto.setPreco(-1.0);

        // Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(produto)
        );

        // Assert
        assertEquals("Preço inválido", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando um novo fabricante nulo for fornecido para o produto")
    void quandoNovoFabricanteNull() {
        // Arrange
        produto.setFabricante(null);

        // Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(produto)
        );

        // Assert
        assertEquals("Fabricante inválido", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando um novo fabricante vazio for fornecido para o produto")
    void quandoNovoFabricanteVazio() {
        // Arrange
        produto.setFabricante(" ");

        // Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(produto)
        );

        // Assert
        assertEquals("Fabricante inválido", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando um novo código de barras válido for fornecido para o produto")
    void quandoNovoCodigoBarrasValido() {
        // Arrange
        produto.setCodigoDeBarras("7899137500100");

        // Act
        Produto resultado = driver.alterar(produto);

        // Assert
        assertEquals("7899137500100", resultado.getCodigoDeBarras());
    }

    @Test
    @DisplayName("Quando um novo código de barras inválido for fornecido para o produto")
    void quandoNovoCodigoBarrasInvalido() {
        // Arrange
        produto.setCodigoDeBarras("7899137500019");

        // Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(produto)
        );

        // Assert
        assertEquals("Código de barras inválido", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando um novo produto nulo for fornecido para o produto")
    void quandoNovoProdutoNull() {
        // Arrange
        produto = null;

        // Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(produto)
        );

        // Assert
        assertEquals("Produto inválido", thrown.getMessage());
    }
}
