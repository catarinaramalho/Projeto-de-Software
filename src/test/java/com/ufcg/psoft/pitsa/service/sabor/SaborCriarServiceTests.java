package com.ufcg.psoft.pitsa.service.sabor;

import com.ufcg.psoft.pitsa.dto.SaborPostPutRequestDTO;
import com.ufcg.psoft.pitsa.exception.CodigoDeAcessoInvalidoException;
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
@DisplayName("Testes do Serviço de criação de sabor")
class SaborCriarServiceTests {

    @Autowired
    SaborCriarService driver;

    @Autowired
    SaborRepository saborRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    Sabor sabor;
    Estabelecimento estabelecimento;
    SaborPostPutRequestDTO saborPostPutRequestDTO;

    @BeforeEach
    void setup() {
        sabor = Sabor.builder()
                .nome("Sabor Um")
                .tipo("salgado")
                .precoM(10.0)
                .precoG(20.0)
                .disponivel(true)
                .build();
        saborPostPutRequestDTO = SaborPostPutRequestDTO.builder()
                .nome("Sabor Um")
                .tipo("salgado")
                .precoM(10.0)
                .precoG(20.0)
                .disponivel(true)
                .build();
        estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                .codigoAcesso("654321")
                .build()
        );
    }

    @AfterEach
    void tearDown() {
        saborRepository.deleteAll();
        estabelecimentoRepository.deleteAll();
    }

    @Test
    @DisplayName("Quando criamos um sabor primeiro")
    void quandoCriamosUmSaborPrimeiro() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        Sabor resultado = driver.salvar(saborPostPutRequestDTO, estabelecimento.getId(), estabelecimento.getCodigoAcesso());

        // Assert
        assertAll(
                () -> assertEquals(1, saborRepository.count()),
                () -> assertEquals(sabor.getNome(), resultado.getNome()),
                () -> assertEquals(sabor.getTipo(), resultado.getTipo()),
                () -> assertEquals(sabor.getPrecoM(), resultado.getPrecoM()),
                () -> assertEquals(sabor.getPrecoG(), resultado.getPrecoG()),
                () -> assertEquals(sabor.isDisponivel(), resultado.isDisponivel())
        );
    }

    @Test
    @DisplayName("Quando criamos um sabor segundo ou posterior")
    void quandoCriamosUmSaborSegundoOuPosterior() {
        // Arrange
        SaborPostPutRequestDTO saborPostPutRequestDTO1 = SaborPostPutRequestDTO.builder()
                .nome("Sabor Dois")
                .tipo("doce")
                .precoM(10.0)
                .precoG(20.0)
                .disponivel(true)
                .build();
        driver.salvar(saborPostPutRequestDTO1, estabelecimento.getId(), estabelecimento.getCodigoAcesso());

        // Act
        Sabor resultado = driver.salvar(saborPostPutRequestDTO, estabelecimento.getId(), estabelecimento.getCodigoAcesso());

        // Assert
        assertAll(
                () -> assertEquals(2, saborRepository.count()),
                () -> assertEquals(sabor.getNome(), resultado.getNome()),
                () -> assertEquals(sabor.getTipo(), resultado.getTipo()),
                () -> assertEquals(sabor.getPrecoM(), resultado.getPrecoM()),
                () -> assertEquals(sabor.getPrecoG(), resultado.getPrecoG()),
                () -> assertEquals(sabor.isDisponivel(), resultado.isDisponivel())
        );
    }

    @Test
    @DisplayName("Quando criamos um sabor passando codigo de acesso invalido")
    void quandoCriamosUmSaborPassandoCodigoDeAcessoInvalido() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        CodigoDeAcessoInvalidoException thrown = assertThrows(
                CodigoDeAcessoInvalidoException.class,
                () -> driver.salvar(saborPostPutRequestDTO, estabelecimento.getId(), "999999")
        );

        // Assert
        assertAll(
                () -> assertEquals(0, saborRepository.count()),
                () -> assertEquals("Codigo de acesso invalido!", thrown.getMessage())
        );
    }
}
