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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Testes do Serviço de alteração de entregadores")
public class EntregadorAlterarServiceTests {

    @Autowired
    EntregadorAlterarService driver;
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
    @DisplayName("Quando alteramos entregador com dados válidos")
    void quandoAlteramosEntregadorComDadosValidos() {
        // Arrange
        entregadorPostPutRequestDTO.setNome("Taylor Swift");
        entregadorPostPutRequestDTO.setCodigoAcesso("131289");

        // Act
        Entregador resultado = driver.alterar(entregador.getId(), entregadorPostPutRequestDTO, entregador.getCodigoAcesso());

        // Assert
        assertAll(
                () -> assertEquals(entregador.getId(), resultado.getId()),
                () -> assertEquals(entregadorPostPutRequestDTO.getNome(), resultado.getNome()),
                () -> assertEquals(entregadorPostPutRequestDTO.getCodigoAcesso(), resultado.getCodigoAcesso()),
                () -> assertEquals(entregadorPostPutRequestDTO.getPlacaVeiculo(), resultado.getPlacaVeiculo()),
                () -> assertEquals(entregadorPostPutRequestDTO.getCorVeiculo(), resultado.getCorVeiculo()),
                () -> assertEquals(entregadorPostPutRequestDTO.getTipoVeiculo(), resultado.getTipoVeiculo()),
                () -> assertEquals(1, entregadorRepository.count())
        );
    }


    @Test
    @DisplayName("Quando alteramos entregador com id inexistente")
    void quandoAlteramosEntregadorComIdInexistente() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        EntregadorNaoExisteException thrown = assertThrows(
                EntregadorNaoExisteException.class,
                () -> driver.alterar(7L, entregadorPostPutRequestDTO, entregador.getCodigoAcesso())
        );

        // Assert
        assertAll(
                () -> assertEquals("O entregador consultado nao existe!", thrown.getMessage()),
                () -> assertEquals(1, entregadorRepository.count())
        );
    }

    @Test
    @DisplayName("Quando alteramos entregador com código de acesso inválido")
    void quandoAlteramosEntregadorComCodigoDeAcessoInvalido() {
        // Arrange
        String codigoAcesso = "654321";

        // Act
        CodigoDeAcessoInvalidoException thrown = assertThrows(
                CodigoDeAcessoInvalidoException.class,
                () -> driver.alterar(entregador.getId(), entregadorPostPutRequestDTO, codigoAcesso)
        );

        // Assert
        assertAll(
                () -> assertEquals("Codigo de acesso invalido!", thrown.getMessage()),
                () -> assertEquals(1, entregadorRepository.count())
        );
    }
}

