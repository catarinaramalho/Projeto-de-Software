package com.ufcg.psoft.pitsa.service.sabor;

import com.ufcg.psoft.pitsa.dto.SaborPostPutRequestDTO;
import com.ufcg.psoft.pitsa.exception.*;
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
@DisplayName("Testes do Serviço de alterar disponibilidade de sabor")
public class SaborAlterarDisponibilidadeServiceTests {

    @Autowired
    SaborAlterarDisponibilidadeService driver;

    @Autowired
    SaborRepository saborRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    Sabor sabor;
    Estabelecimento estabelecimento;
    SaborPostPutRequestDTO saborPostPutRequestDTO;

    @BeforeEach
    void setup() {
        estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                .codigoAcesso("654321")
                .build()
        );
        sabor = saborRepository.save(Sabor.builder()
                .nome("Frango")
                .tipo("salgado")
                .precoM(10.0)
                .precoG(20.0)
                .disponivel(true)
                .build());
        saborPostPutRequestDTO = SaborPostPutRequestDTO.builder()
                .nome("Frango")
                .tipo("salgado")
                .precoM(10.0)
                .precoG(20.0)
                .disponivel(true)
                .build();
    }

    @AfterEach
    void tearDown() {
        saborRepository.deleteAll();
        estabelecimentoRepository.deleteAll();
    }

    @Test
    @DisplayName("Alterar disponibilidade de sabor para indisponível")
    void testAlterarDisponibilidadeSaborParaIndisponivel() {
        // Arrange
        // Act
        Sabor resultado = driver.alterarDisponibilidade(sabor.getId(), estabelecimento.getId(), estabelecimento.getCodigoAcesso(), false);
        // Assert
        assertAll(
                () -> assertEquals(sabor.getId(), resultado.getId()),
                () -> assertFalse(resultado.isDisponivel())
        );
    }

    @Test
    @DisplayName("Alterar disponibilidade de sabor para disponível")
    void testAlterarDisponibilidadeSaborParaDisponivel() {
        // Arrange
        Sabor sabor2 = saborRepository.save(Sabor.builder()
                .nome("Calabresa")
                .tipo("salgado")
                .precoM(10.0)
                .precoG(20.0)
                .disponivel(false)
                .build());
        // Act
        Sabor resultado = driver.alterarDisponibilidade(sabor2.getId(), estabelecimento.getId(), estabelecimento.getCodigoAcesso(), true);
        // Assert
        assertAll(
                () -> assertEquals(sabor2.getId(), resultado.getId()),
                () -> assertTrue(resultado.isDisponivel())
        );
    }

    @Test
    @DisplayName("Alterar disponibilidade de sabor para disponível com código de acesso inválido")
    void testAlterarDisponibilidadeCodigoAcessoInvalido() {
        // Arrange
        // Act
        CodigoDeAcessoInvalidoException thrown = assertThrows(
                CodigoDeAcessoInvalidoException.class,
                () -> driver.alterarDisponibilidade(sabor.getId(), estabelecimento.getId(), "123456", true)
        );
        // Assert
        assertAll(
                () -> assertEquals("Codigo de acesso invalido!", thrown.getMessage()));
    }

    @Test
    @DisplayName("Alterar disponibilidade de sabor já disponivel para disponível")
    void testAlterarDisponibilidadeSaborJaDisponivelParaDisponivel() {
        // Arrange
        // Act
        SaborJaDisponivelException thrown = assertThrows(
                SaborJaDisponivelException.class,
                () -> driver.alterarDisponibilidade(sabor.getId(), estabelecimento.getId(), estabelecimento.getCodigoAcesso(), true)
        );
        // Assert
        assertAll(
                () -> assertEquals("O sabor consultado ja esta disponivel!", thrown.getMessage())
        );
    }

    @Test
    @DisplayName("Alterar disponibilidade de sabor já indisponivel para indisponível")
    void testAlterarDisponibilidadeSaborJaIndisponivelParaIndisponivel() {
        // Arrange
        Sabor sabor2 = saborRepository.save(Sabor.builder()
                .nome("Calabresa")
                .tipo("salgado")
                .precoM(10.0)
                .precoG(20.0)
                .disponivel(false)
                .build());
        // Act
        SaborJaIndisponivelException thrown = assertThrows(
                SaborJaIndisponivelException.class,
                () -> driver.alterarDisponibilidade(sabor2.getId(), estabelecimento.getId(), estabelecimento.getCodigoAcesso(), false)
        );
        // Assert
        assertAll(
                () -> assertEquals("O sabor consultado ja esta indisponivel!", thrown.getMessage())
        );
    }

    @Test
    @DisplayName("Alterar disponibilidade de sabor não existente")
    void testAlterarDisponibilidadeSaborNaoExistente() {
        // Arrange
        // Act
        SaborNaoExisteException thrown = assertThrows(
                SaborNaoExisteException.class,
                () -> driver.alterarDisponibilidade(100L, estabelecimento.getId(), estabelecimento.getCodigoAcesso(), false)
        );
        // Assert
        assertAll(
                () -> assertEquals("O sabor consultado nao existe!", thrown.getMessage())
        );
    }

    @Test
    @DisplayName("Alterar disponibilidade de sabor com estabelecimento não existente")
    void testAlterarDisponibilidadeSaborComEstabelecimentoNaoExistente() {
        // Arrange
        // Act
        EstabelecimentoNaoExisteException thrown = assertThrows(
                EstabelecimentoNaoExisteException.class,
                () -> driver.alterarDisponibilidade(sabor.getId(), 100L, estabelecimento.getCodigoAcesso(), false)
        );
        // Assert
        assertAll(
                () -> assertEquals("O estabelecimento consultado nao existe!", thrown.getMessage())
        );
    }
}
