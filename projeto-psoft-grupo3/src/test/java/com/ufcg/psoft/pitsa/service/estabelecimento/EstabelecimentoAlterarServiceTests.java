package com.ufcg.psoft.pitsa.service.estabelecimento;

import com.ufcg.psoft.pitsa.dto.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.pitsa.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.pitsa.exception.EstabelecimentoNaoExisteException;
import com.ufcg.psoft.pitsa.model.Estabelecimento;
import com.ufcg.psoft.pitsa.repository.EstabelecimentoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Testes do Serviço de alteração de estabelecimentos")
public class EstabelecimentoAlterarServiceTests {

    @Autowired
    EstabelecimentoAlterarService driver;

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    Estabelecimento estabelecimento;

    EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO;

    @BeforeEach
    void setup() {
        estabelecimento = Estabelecimento.builder()
                .codigoAcesso("123456")
                .build();
        estabelecimentoPostPutRequestDTO = EstabelecimentoPostPutRequestDTO.builder()
                .codigoAcesso("123456")
                .build();
        estabelecimentoRepository.save(estabelecimento);
    }

    @AfterEach
    void tearDown() {
        estabelecimentoRepository.deleteAll();
    }

    @Test
    @DisplayName("Quando alteramos estabelecimento com dados válidos")
    void quandoAlteramosEstabelecimentoComDadosValidos() {
        // Arrange
        estabelecimentoPostPutRequestDTO.setCodigoAcesso("123123");

        // Act
        Estabelecimento resultado = driver.alterar(estabelecimento.getId(), estabelecimento.getCodigoAcesso(), estabelecimentoPostPutRequestDTO);

        // Assert
        assertAll(
                () -> assertEquals(estabelecimento.getId(), resultado.getId()),
                () -> assertEquals(estabelecimentoPostPutRequestDTO.getCodigoAcesso(), resultado.getCodigoAcesso()),
                () -> assertEquals(1, estabelecimentoRepository.count())
        );
    }

    @Test
    @DisplayName("Quando alteramos estabelecimento com id inexistente")
    void quandoAlteramosEstabelecimentoComIdInexistente() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        EstabelecimentoNaoExisteException thrown = assertThrows(
                EstabelecimentoNaoExisteException.class,
                () -> driver.alterar(7L, estabelecimento.getCodigoAcesso(), estabelecimentoPostPutRequestDTO)
        );

        // Assert
        assertAll(
                () -> assertEquals("O estabelecimento consultado nao existe!", thrown.getMessage()),
                () -> assertEquals(1, estabelecimentoRepository.count())
        );
    }

    @Test
    @DisplayName("Quando alteramos estabelecimento com código de acesso inválido")
    void quandoAlteramosEstabelecimentoComCodigoDeAcessoInvalido() {
        // Arrange
        String codigoAcesso = "654321";

        // Act
        CodigoDeAcessoInvalidoException thrown = assertThrows(
                CodigoDeAcessoInvalidoException.class,
                () -> driver.alterar(estabelecimento.getId(), codigoAcesso, estabelecimentoPostPutRequestDTO)
        );

        // Assert
        assertAll(
                () -> assertEquals("Codigo de acesso invalido!", thrown.getMessage()),
                () -> assertEquals(1, estabelecimentoRepository.count())
        );
    }
}

