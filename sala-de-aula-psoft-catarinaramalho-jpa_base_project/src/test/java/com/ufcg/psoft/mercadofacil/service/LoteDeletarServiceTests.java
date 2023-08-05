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
@DisplayName("Testes do Serviço de remoção do produto")
public class LoteDeletarServiceTests {

    @Autowired
    LoteDeletarService driver;

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
    @DisplayName("Quando um id nulo for fornecido para o lote")
    void quandoIdNull() {
        // Arrange
        lote.setId(null);

        // Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.deletar(lote.getId())
        );

        // Assert
        assertEquals("Id inválido", thrown.getMessage());
    }
}
