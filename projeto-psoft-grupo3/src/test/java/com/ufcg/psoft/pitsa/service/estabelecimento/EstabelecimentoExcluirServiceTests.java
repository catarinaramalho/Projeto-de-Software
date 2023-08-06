package com.ufcg.psoft.pitsa.service.estabelecimento;

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
@DisplayName("Testes do serviço de exclusão de estabelecimento")
public class EstabelecimentoExcluirServiceTests {

    @Autowired
    EstabelecimentoExcluirService driver;

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    Estabelecimento estabelecimento;

    @BeforeEach
    void setup() {
        estabelecimento = Estabelecimento.builder()
                .codigoAcesso("123456")
                .build();
        estabelecimentoRepository.save(estabelecimento);
    }

    @AfterEach
    void tearDown() {
        estabelecimentoRepository.deleteAll();
    }

    @Test
    @DisplayName("Quando excluímos um estabelecimento único salvo pelo id")
    void quandoExcluimosUmEstabelecimentoSalvoPeloIdPrimeiro() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        driver.excluir(estabelecimento.getId(), estabelecimento.getCodigoAcesso());

        // Assert
        assertEquals(0, estabelecimentoRepository.count());
    }

    @Test
    @DisplayName("Quando excluímos um estabelecimento que não é único pelo id")
    void quandoExcluimosUmEstabelecimentoSalvoPeloIdSegundoOuPosterior() {
        // Arrange
        Estabelecimento estabelecimento1 = estabelecimentoRepository.save(Estabelecimento.builder()
                .codigoAcesso("224545")
                .build());

        // Act
        driver.excluir(estabelecimento.getId(), estabelecimento.getCodigoAcesso());

        // Assert
        assertAll(
                () -> assertEquals(1, estabelecimentoRepository.count()),
                () -> assertEquals(estabelecimento1, estabelecimentoRepository.findAll().get(0))
        );
    }

    @Test
    @DisplayName("Quando excluímos um estabelecimento por um id inexistente")
    void quandoExcluimosUmEstabelecimentoPorUmIdInexistente() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        EstabelecimentoNaoExisteException thrown = assertThrows(
                EstabelecimentoNaoExisteException.class,
                () -> driver.excluir(2L, "010101")
        );

        // Assert
        assertAll(
                () -> assertEquals("O estabelecimento consultado nao existe!", thrown.getMessage()),
                () -> assertEquals(1, estabelecimentoRepository.count())
        );
    }

    @Test
    @DisplayName("Quando excluímos um estabelecimento pelo id com um código de acesso inválido")
    void quandoExcluimosUmEstabelecimentoPeloIdComUmCodigoDeAcessoInvalido() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        CodigoDeAcessoInvalidoException thrown = assertThrows(
                CodigoDeAcessoInvalidoException.class,
                () -> driver.excluir(estabelecimento.getId(), "010101")
        );

        // Assert
        assertAll(
                () -> assertEquals("Codigo de acesso invalido!", thrown.getMessage()),
                () -> assertEquals(1, estabelecimentoRepository.count())
        );
    }
}
