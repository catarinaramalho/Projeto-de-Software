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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("Testes do Serviço de listagem de sabor")
class SaborListarServiceTests {

    @Autowired
    SaborListarService driver;

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
    @DisplayName("Quando listamos todos os sabores salvos primeiro")
    void quandoListamosTodosOsSaboresSalvosPrimeiro() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        List<Sabor> resultado = driver.listar(null, estabelecimento.getId(), estabelecimento.getCodigoAcesso());

        // Assert
        assertAll(
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals(sabor, resultado.get(0))
        );
    }

    @Test
    @DisplayName("Quando listamos todos os sabores salvos segundo ou posterior")
    void quandoListamosTodosOsSaboresSalvosSegundoOuPosterior() {
        // Arrange
        Sabor sabor1 = saborRepository.save(Sabor.builder()
                .nome("Sabor Dois")
                .tipo("doce")
                .precoM(15.0)
                .precoG(25.0)
                .disponivel(true)
                .build()
        );

        // Act
        List<Sabor> resultado = driver.listar(null, estabelecimento.getId(), estabelecimento.getCodigoAcesso());

        // Assert
        assertAll(
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals(sabor, resultado.get(0)),
                () -> assertEquals(sabor1, resultado.get(1))
        );
    }

    @Test
    @DisplayName("Quando listamos um sabor salvo pelo id primeiro")
    void quandoListamosUmSaborSalvoPeloIdPrimeiro() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        List<Sabor> resultado = driver.listar(sabor.getId(), estabelecimento.getId(), estabelecimento.getCodigoAcesso());

        // Assert
        assertAll(
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals(sabor, resultado.get(0))
        );
    }

    @Test
    @DisplayName("Quando listamos um sabor salvo pelo id segundo ou posterior")
    void quandoListamosUmSaborSalvoPeloIdSegundoOuPosterior() {
        // Arrange
        Sabor sabor1 = saborRepository.save(Sabor.builder()
                .nome("Sabor Dois")
                .tipo("doce")
                .precoM(15.0)
                .precoG(25.0)
                .disponivel(true)
                .build()
        );

        // Act
        List<Sabor> resultado = driver.listar(sabor1.getId(), estabelecimento.getId(), estabelecimento.getCodigoAcesso());

        // Assert
        assertAll(
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals(sabor1, resultado.get(0))
        );
    }

    @Test
    @DisplayName("Quando listamos um sabor pelo id inexistente")
    void quandoListamosUmSaborPeloIdInexistente() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        SaborNaoExisteException thrown = assertThrows(
                SaborNaoExisteException.class,
                () -> driver.listar(999L, estabelecimento.getId(), estabelecimento.getCodigoAcesso())
        );

        // Assert
        assertEquals("O sabor consultado nao existe!", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando listamos um sabor passando codigo estabelecimento invalido")
    void quandoListamosUmSaborPassandoCodigoEstabelecimentoInvalido() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        CodigoDeAcessoInvalidoException thrown = assertThrows(
                CodigoDeAcessoInvalidoException.class,
                () -> driver.listar(sabor.getId(), estabelecimento.getId(), "999999")
        );

        // Assert
        assertEquals("Codigo de acesso invalido!", thrown.getMessage());
    }
}
