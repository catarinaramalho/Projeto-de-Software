package com.ufcg.psoft.pitsa.service.sabor;

import com.ufcg.psoft.pitsa.dto.SaborPostPutRequestDTO;
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
@DisplayName("Testes do Serviço de criação de sabor")
class SaborAlterarServiceTests {

    @Autowired
    SaborAlterarService driver;

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
    @DisplayName("Quando alteramos sabor com dados válidos")
    void quandoAlteramosSaborComDadosValidos() {
        // Arrange
        saborPostPutRequestDTO.setNome("Chocolate com morango");
        saborPostPutRequestDTO.setTipo("doce");
        saborPostPutRequestDTO.setPrecoM(25.0);
        saborPostPutRequestDTO.setPrecoG(35.0);

        // Act
        Sabor resultado = driver.alterar(sabor.getId(), saborPostPutRequestDTO, estabelecimento.getId(), estabelecimento.getCodigoAcesso());

        // Assert
        assertAll(
                () -> assertEquals(1, saborRepository.count()),
                () -> assertEquals(sabor.getId(), resultado.getId()),
                () -> assertEquals(saborPostPutRequestDTO.getNome(), resultado.getNome()),
                () -> assertEquals(saborPostPutRequestDTO.getTipo(), resultado.getTipo()),
                () -> assertEquals(saborPostPutRequestDTO.getPrecoM(), resultado.getPrecoM()),
                () -> assertEquals(saborPostPutRequestDTO.getPrecoG(), resultado.getPrecoG())
        );
    }

    @Test
    @DisplayName("Quando alteramos sabor com id inexistente")
    void quandoAlteramosSaborComIdInexistente() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        SaborNaoExisteException thrown = assertThrows(
                SaborNaoExisteException.class,
                () -> driver.alterar(2L, saborPostPutRequestDTO, estabelecimento.getId(), estabelecimento.getCodigoAcesso())
        );

        // Assert
        assertAll(
                () -> assertEquals("O sabor consultado nao existe!", thrown.getMessage()),
                () -> assertEquals(1, saborRepository.count())
        );
    }

    @Test
    @DisplayName("Quando alteramos sabor com código de acesso inválido")
    void quandoAlteramosSaborComCodigoDeAcessoInvalido() {
        // Arrange
        String codigoAcesso = "787878";

        // Act
        CodigoDeAcessoInvalidoException thrown = assertThrows(
                CodigoDeAcessoInvalidoException.class,
                () -> driver.alterar(sabor.getId(), saborPostPutRequestDTO, estabelecimento.getId(), codigoAcesso)
        );

        // Assert
        assertAll(
                () -> assertEquals("Codigo de acesso invalido!", thrown.getMessage()),
                () -> assertEquals(1, saborRepository.count())
        );
    }
}
