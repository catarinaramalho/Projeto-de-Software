package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.model.Lote;
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
@DisplayName("Testes do Serviço de alteração do lote")
public class LoteAlterarServiceTests {

    @Autowired
    LoteAlterarService driver;

    @Autowired
    LoteRepository loteRepository;

    Produto produto;
    Lote lote;

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
    void quandoLoteNull() {
        // Arrange
        lote = null;

        // Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(lote)
        );

        // Assert
        assertEquals("Lote inválido", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando um novo produto nulo for fornecido para o lote")
    void quandoProdutoNull() {
        // Arrange
        lote.setProduto(null);

        // Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(lote)
        );

        // Assert
        assertEquals("Produto inválido", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando um novo id nulo for fornecido para o lote")
    void quandoNovoIdNull() {
        // Arrange
        lote.setId(null);

        // Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(lote)
        );

        // Assert
        assertEquals("Id inválido", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando o número de itens é menor que zero")
    void quandoNumeroDeItensMenorZero() {
        // Arrange
        lote.setNumeroDeItens(-1);

        // Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(lote)
        );

        // Assert
        assertEquals("Número de itens inválido", thrown.getMessage());
    }

}
