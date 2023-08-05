package com.ufcg.psoft.pitsa.service.associacao;

import com.ufcg.psoft.pitsa.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.pitsa.exception.EntregadorNaoExisteException;
import com.ufcg.psoft.pitsa.exception.EstabelecimentoNaoExisteException;
import com.ufcg.psoft.pitsa.model.Associacao;
import com.ufcg.psoft.pitsa.model.Entregador;
import com.ufcg.psoft.pitsa.model.Estabelecimento;
import com.ufcg.psoft.pitsa.repository.AssociacaoRepository;
import com.ufcg.psoft.pitsa.repository.EntregadorRepository;
import com.ufcg.psoft.pitsa.repository.EstabelecimentoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Testes do Serviço de Associação e Aprovação de Entregador")
public class AssociacaoCriarServiceTests {

    @Autowired
    AssociacaoCriarService driver;

    @Autowired
    AssociacaoRepository associacaoRepository;

    @Autowired
    EntregadorRepository entregadorRepository;

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    Entregador entregador;
    Estabelecimento estabelecimento;

    @BeforeEach
    void setup() {
        entregador = entregadorRepository.save(Entregador.builder()
                .nome("Entregador Um")
                .placaVeiculo("ABC-1234")
                .tipoVeiculo("Carro")
                .corVeiculo("Branco")
                .codigoAcesso("123456")
                .build()
        );

        estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                .codigoAcesso("654321")
                .build()
        );
    }

    @AfterEach
    void tearDown() {
        entregadorRepository.deleteAll();
        estabelecimentoRepository.deleteAll();
        associacaoRepository.deleteAll();
    }

    @Test
    @DisplayName("Quando associamos um entregador a um estabelecimento valido")
    void quandoAssociamosEntregadorAEstabelecimentoValido() {
        // Arrange
        // nenhuma necessidade além do setup

        //Act
        Associacao associacao = driver.associar(entregador.getId(), entregador.getCodigoAcesso(), estabelecimento.getId());

        //Assert
        assertAll(
                () -> assertEquals(1, associacaoRepository.count()),
                () -> assertEquals(entregador.getId(), associacao.getEntregadorId()),
                () -> assertEquals(estabelecimento.getId(), associacao.getEstabelecimentoId()),
                () -> assertFalse(associacao.isStatus())
        );
    }

    @Test
    @DisplayName("Verificação que uma associação incia false")
    void verificacaoQueUmaAssociacaoIniciaFalse() {
        // Arrange
        // nenhuma necessidade além do setup

        //Act
        Associacao associacao = driver.associar(entregador.getId(), entregador.getCodigoAcesso(), estabelecimento.getId());

        //Assert
        assertAll(
                () -> assertEquals(1, associacaoRepository.count()),
                () -> assertFalse(associacao.isStatus())
        );
    }

    @Test
    @DisplayName("Quando associamos um entregador inexistente a um estabelecimento")
    void quandoAssociamosEntregadorInexistenteAEstabelecimento() {
        // Arrange
        // nenhuma necessidade além do setup

        //Act
        EntregadorNaoExisteException thrown = assertThrows(
                EntregadorNaoExisteException.class,
                () -> driver.associar(9999L, entregador.getCodigoAcesso(), estabelecimento.getId())
        );

        //Assert
        assertAll(
                () -> assertEquals(0, associacaoRepository.count()),
                () -> assertEquals("O entregador consultado nao existe!", thrown.getMessage())
        );
    }

    @Test
    @DisplayName("Quando associamos um entregador a um estabelecimento inexistente")
    void quandoAssociamosEntregadorAEstabelecimentoInexistente() {
        // Arrange
        // nenhuma necessidade além do setup

        //Act
        EstabelecimentoNaoExisteException thrown = assertThrows(
                EstabelecimentoNaoExisteException.class,
                () -> driver.associar(entregador.getId(), entregador.getCodigoAcesso(), 9999L)
        );

        //Assert
        assertAll(
                () -> assertEquals(0, associacaoRepository.count()),
                () -> assertEquals("O estabelecimento consultado nao existe!", thrown.getMessage())
        );
    }

    @Test
    @DisplayName("Quando associamos um entregador a um estabelecimento com codigo de acesso invalido")
    void quandoAssociamosEntregadorAEstabelecimentoComCodigoDeAcessoInvalido() {
        // Arrange
        // nenhuma necessidade além do setup

        //Act
        CodigoDeAcessoInvalidoException thrown = assertThrows(
                CodigoDeAcessoInvalidoException.class,
                () -> driver.associar(entregador.getId(), "999999", estabelecimento.getId())
        );

        //Assert
        assertAll(
                () -> assertEquals(0, associacaoRepository.count()),
                () -> assertEquals("Codigo de acesso invalido!", thrown.getMessage())
        );
    }
}
