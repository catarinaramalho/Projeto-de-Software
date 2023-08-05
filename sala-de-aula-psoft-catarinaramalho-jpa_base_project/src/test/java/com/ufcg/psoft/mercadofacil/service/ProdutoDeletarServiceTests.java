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
@DisplayName("Testes do Serviço de remoção do produto")
public class ProdutoDeletarServiceTests {

    @Autowired
    ProdutoDeletarService driver;

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
    @DisplayName("Quando um produto válido é deletado")
    void quandoProdutoValidoDeletado() {
        // Arrange
        Produto resultado = driver.deletar(produto.getId());

        // Assert
        assertEquals(produto.getId(), resultado.getId());
    }

    @Test
    @DisplayName("Quando um id nulo for fornecido para o produto")
    void quandoNovoProdutoNull() {
        // Arrange
        produto.setId(null);

        // Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.deletar(produto.getId())
        );

        // Assert
        assertEquals("Id inválido", thrown.getMessage());
    }
}
