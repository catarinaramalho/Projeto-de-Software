package com.ufcg.psoft.pitsa.service.sabor;

import com.ufcg.psoft.pitsa.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.pitsa.exception.SaborNaoExisteException;
import com.ufcg.psoft.pitsa.model.Estabelecimento;
import com.ufcg.psoft.pitsa.model.Sabor;
import com.ufcg.psoft.pitsa.repository.EstabelecimentoRepository;
import com.ufcg.psoft.pitsa.repository.SaborRepository;
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
@DisplayName("Testes do Serviço de exclusão de sabor")
public class SaborExcluirServiceTests {

    @Autowired
    SaborExcluirService driver;

    @Autowired
    SaborRepository saborRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    Sabor sabor;
    Estabelecimento estabelecimento;

    @BeforeEach
    void setup() {
        estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                .codigoAcesso("654321")
                .build()
        );
        sabor = saborRepository.save(Sabor.builder()
                .nome("Sabor Um")
                .tipo("salgado")
                .precoM(10.0)
                .precoG(20.0)
                .disponivel(true)
                .build());
    }

    @AfterEach
    void tearDown() {
        saborRepository.deleteAll();
        estabelecimentoRepository.deleteAll();
    }

    @Test
    @DisplayName("Quando excluímos um sabor salvo pelo id primeiro")
    void quandoExcluimosUmSaborSalvoPeloIdPrimeiro() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        driver.excluir(sabor.getId(), estabelecimento.getId(), estabelecimento.getCodigoAcesso());

        // Assert
        assertEquals(0, saborRepository.count());
    }

    @Test
    @DisplayName("Quando excluímos um sabor salvo pelo id segundo ou posterior")
    void quandoExcluimosUmSaborSalvoPeloIdSegundoOuPosterior() {
        // Arrange
        Sabor sabor1 = saborRepository.save(Sabor.builder()
                .nome("Sabor Dois")
                .tipo("doce")
                .precoM(20.0)
                .precoG(40.0)
                .disponivel(true)
                .build());

        // Act
        driver.excluir(sabor.getId(), estabelecimento.getId(), estabelecimento.getCodigoAcesso());

        // Assert
        assertAll(
                () -> assertEquals(1, saborRepository.count()),
                () -> assertEquals(sabor1, saborRepository.findAll().get(0))
        );
    }

    @Test
    @DisplayName("Quando excluímos um sabor salvo pelo id inexistente")
    void quandoExcluimosUmSaborSalvoPeloIdInexistente() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        SaborNaoExisteException thrown = assertThrows(
                SaborNaoExisteException.class,
                () -> driver.excluir(999L, estabelecimento.getId(), estabelecimento.getCodigoAcesso())
        );

        // Assert
        assertAll(
                () -> assertEquals("O sabor consultado nao existe!", thrown.getMessage()),
                () -> assertEquals(1, saborRepository.count())
        );
    }

    @Test
    @DisplayName("Quando excluímos um sabor salvo pelo id com código de nulo")
    void quandoExcluimosUmSaborSalvoPeloIdComCodigoDeNulo() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        CodigoDeAcessoInvalidoException thrown = assertThrows(
                CodigoDeAcessoInvalidoException.class,
                () -> driver.excluir(sabor.getId(), estabelecimento.getId(), null)
        );

        // Assert
        assertAll(
                () -> assertEquals("Codigo de acesso invalido!", thrown.getMessage()),
                () -> assertEquals(1, saborRepository.count())
        );
    }

    @Test
    @DisplayName("Quando excluímos um sabor pelo id com código de acesso inválido")
    void quandoExcluimosUmSaborPeloIdComCodigoDeAcessoInvalido() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        CodigoDeAcessoInvalidoException thrown = assertThrows(
                CodigoDeAcessoInvalidoException.class,
                () -> driver.excluir(sabor.getId(), estabelecimento.getId(), "999999")
        );

        // Assert
        assertAll(
                () -> assertEquals("Codigo de acesso invalido!", thrown.getMessage()),
                () -> assertEquals(1, saborRepository.count())
        );
    }
}
