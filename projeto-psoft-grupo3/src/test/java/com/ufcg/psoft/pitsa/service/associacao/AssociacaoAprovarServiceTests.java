package com.ufcg.psoft.pitsa.service.associacao;

import com.ufcg.psoft.pitsa.exception.AssociacaoNaoExisteException;
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
public class AssociacaoAprovarServiceTests {

    @Autowired
    AssociacaoAprovarService driver;

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

        associacaoRepository.save(Associacao.builder()
                .entregadorId(entregador.getId())
                .estabelecimentoId(estabelecimento.getId())
                .status(false)
                .build());
    }

    @AfterEach
    void tearDown() {
        entregadorRepository.deleteAll();
        estabelecimentoRepository.deleteAll();
        associacaoRepository.deleteAll();
    }

    @Test
    @DisplayName("Quando aprovamos um associação")
    void quandoAprovamosUmaAssociacao() {
        // Arrange
        // nenhuma necessidade além do setup

        //Act
        Associacao associacao = driver.aprovar(entregador.getId(), estabelecimento.getId(), estabelecimento.getCodigoAcesso());

        //Assert
        assertAll(
                () -> assertTrue(associacao.isStatus())
        );
    }

    @Test
    @DisplayName("Quando aprovamos um associação com código de acesso inválido")
    void quandoAprovamosUmaAssociacaoComCodigoDeAcessoInvalido() {
        // Arrange
        // nenhuma necessidade além do setup

        //Act
        CodigoDeAcessoInvalidoException thrown = assertThrows(
                CodigoDeAcessoInvalidoException.class,
                () -> driver.aprovar(entregador.getId(), estabelecimento.getId(), "999999")
        );

        //Assert
        assertAll(
                () -> assertEquals("Codigo de acesso invalido!", thrown.getMessage())
        );
    }

    @Test
    @DisplayName("Quando aprovamos um associação com estabelecimento inexistente")
    void quandoAprovamosUmaAssociacaoComEstabelecimentoInexistente() {
        // Arrange
        // nenhuma necessidade além do setup

        //Act
        EstabelecimentoNaoExisteException thrown = assertThrows(
                EstabelecimentoNaoExisteException.class,
                () -> driver.aprovar(entregador.getId(), 9999L, estabelecimento.getCodigoAcesso())
        );

        //Assert
        assertAll(
                () -> assertEquals("O estabelecimento consultado nao existe!", thrown.getMessage())
        );
    }

    @Test
    @DisplayName("Quando aprovamos um associação com entregador inexistente")
    void quandoAprovamosUmaAssociacaoComEntregadorInexistente() {
        // Arrange
        // nenhuma necessidade além do setup

        //Act
        EntregadorNaoExisteException thrown = assertThrows(
                EntregadorNaoExisteException.class,
                () -> driver.aprovar(9999L, estabelecimento.getId(), estabelecimento.getCodigoAcesso())
        );

        //Assert
        assertAll(
                () -> assertEquals("O entregador consultado nao existe!", thrown.getMessage())
        );
    }

    @Test
    @DisplayName("Quando aprovamos um associação inexistente")
    void quandoAprovamosUmaAssociacaoInexistente() {
        // Arrange
        Entregador entregador1 = entregadorRepository.save(Entregador.builder()
                .nome("Entregador Dois")
                .placaVeiculo("CBA-4321")
                .tipoVeiculo("Motocicleta")
                .corVeiculo("Preto")
                .codigoAcesso("654321")
                .build()
        );
        Estabelecimento estabelecimento1 = estabelecimentoRepository.save(Estabelecimento.builder()
                .codigoAcesso("654321")
                .build()
        );

        //Act
        AssociacaoNaoExisteException thrown = assertThrows(
                AssociacaoNaoExisteException.class,
                () -> driver.aprovar(entregador1.getId(), estabelecimento1.getId(), estabelecimento1.getCodigoAcesso())
        );

        //Assert
        assertAll(
                () -> assertEquals("A associacao nao existe!", thrown.getMessage())
        );
    }
}
