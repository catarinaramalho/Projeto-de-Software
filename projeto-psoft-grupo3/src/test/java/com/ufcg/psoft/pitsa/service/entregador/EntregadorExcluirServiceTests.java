package com.ufcg.psoft.pitsa.service.entregador;

import com.ufcg.psoft.pitsa.dto.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.pitsa.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.pitsa.exception.EntregadorNaoExisteException;
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
@Transactional
@DisplayName("Testes do Serviço de exclusão do entregador")
public class EntregadorExcluirServiceTests {

    @Autowired
    EntregadorExcluirService driver;
    @Autowired
    EntregadorRepository entregadorRepository;

    Entregador entregador;
    EntregadorPostPutRequestDTO entregadorPostPutRequestDTO;

    @BeforeEach
    void setup() {
        entregador = Entregador.builder()
                .nome("Joe Alwyn")
                .placaVeiculo("ABC-1234")
                .corVeiculo("Azul")
                .tipoVeiculo("Moto")
                .codigoAcesso("123456")
                .build();
        entregadorPostPutRequestDTO = EntregadorPostPutRequestDTO.builder()
                .nome("Joe Alwyn")
                .placaVeiculo("ABC-1234")
                .corVeiculo("Azul")
                .tipoVeiculo("Moto")
                .codigoAcesso("123456")
                .build();
        entregadorRepository.save(entregador);
    }

    @AfterEach
    void tearDown() {
        entregadorRepository.deleteAll();
    }

    @Test
    @DisplayName("Quando excluímos um entregador único salvo pelo id")
    void quandoExcluimosUmEntregadorSalvoPeloIdPrimeiro() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        driver.excluir(entregador.getId(), entregador.getCodigoAcesso());

        // Assert
        assertEquals(0, entregadorRepository.count());
    }

    @Test
    @DisplayName("Quando excluímos um entregador que não é único pelo id")
    void quandoExcluimosUmEntregadorSalvoPeloIdSegundoOuPosterior() {
        // Arrange
        Entregador entregador1 = entregadorRepository.save(Entregador.builder()
                .nome("Maria")
                .placaVeiculo("DEF-1234")
                .corVeiculo("Vermelho")
                .tipoVeiculo("Carro")
                .codigoAcesso("123123")
                .build());

        // Act
        driver.excluir(entregador.getId(), entregador.getCodigoAcesso());

        // Assert
        assertAll(
                () -> assertEquals(1, entregadorRepository.count()),
                () -> assertEquals(entregador1, entregadorRepository.findAll().get(0))
        );
    }

    @Test
    @DisplayName("Quando excluímos um entregador por um id inexistente")
    void quandoExcluimosUmEntregadorPorUmIdInexistente() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        EntregadorNaoExisteException thrown = assertThrows(
                EntregadorNaoExisteException.class,
                () -> driver.excluir(2L, "123456")
        );

        // Assert
        assertAll(
                () -> assertEquals("O entregador consultado nao existe!", thrown.getMessage()),
                () -> assertEquals(1, entregadorRepository.count())
        );
    }

    @Test
    @DisplayName("Quando excluímos um entregador pelo id com um código de acesso inválido")
    void quandoExcluimosUmEntregadorPeloIdComUmCodigoDeAcessoInvalido() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        CodigoDeAcessoInvalidoException thrown = assertThrows(
                CodigoDeAcessoInvalidoException.class,
                () -> driver.excluir(entregador.getId(), "010101")
        );

        // Assert
        assertAll(
                () -> assertEquals("Codigo de acesso invalido!", thrown.getMessage()),
                () -> assertEquals(1, entregadorRepository.count())
        );
    }
}
