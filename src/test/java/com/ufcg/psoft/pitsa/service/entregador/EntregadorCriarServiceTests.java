package com.ufcg.psoft.pitsa.service.entregador;

import com.ufcg.psoft.pitsa.dto.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.pitsa.model.Entregador;
import com.ufcg.psoft.pitsa.repository.EntregadorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Testes do Serviço de criação do entregador")
public class EntregadorCriarServiceTests {

    @Autowired
    EntregadorCriarPadraoService driver;

    @Autowired
    EntregadorRepository entregadorRepository;

    Entregador entregador;
    EntregadorPostPutRequestDTO entregadorPostPutRequestDTO;

    @BeforeEach
    void setUp() {
        entregador = Entregador.builder()
                .nome("João")
                .placaVeiculo("ABC-1234")
                .corVeiculo("Azul")
                .tipoVeiculo("Moto")
                .codigoAcesso("123456")
                .build();
        entregadorPostPutRequestDTO = EntregadorPostPutRequestDTO.builder()
                .nome("João")
                .placaVeiculo("ABC-1234")
                .corVeiculo("Azul")
                .tipoVeiculo("Moto")
                .codigoAcesso("123456")
                .build();
    }

    @AfterEach
    void tearDown() {
        entregadorRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName("Testa criação de entregador com dados válidos")
    void testCriarPrimeiroEntregadorComDadosValidos() {
        // Arrange
        // Nada mais necessário que o setup

        // Act
        Entregador resultado = driver.criar(entregadorPostPutRequestDTO);

        // Assert
        assertEquals(1, entregadorRepository.count());
    }

    @Test
    @DisplayName("Testa criação de entregador")
    void testCriarEntregadorComDadosValidos() {
        // Arrange
        entregadorRepository.save(entregador);

        // Act
        Entregador resultado = driver.criar(entregadorPostPutRequestDTO);

        // Assert
        assertAll(
                () -> assertFalse(entregador.isStatusAprovacao()),
                () -> assertFalse(entregador.isDisponibilidade()),
                () -> assertNotNull(resultado.getId()),
                () -> assertEquals(2, entregadorRepository.count()),
                () -> assertEquals(entregador.getCodigoAcesso(), resultado.getCodigoAcesso()),
                () -> assertEquals(entregador.getNome(), resultado.getNome()),
                () -> assertEquals(entregador.getPlacaVeiculo(), resultado.getPlacaVeiculo()),
                () -> assertEquals(entregador.getCorVeiculo(), resultado.getCorVeiculo()),
                () -> assertEquals(entregador.getTipoVeiculo(), resultado.getTipoVeiculo())
        );
    }
}
