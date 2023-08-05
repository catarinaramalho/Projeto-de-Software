package com.ufcg.psoft.pitsa.service.estabelecimento;

import com.ufcg.psoft.pitsa.dto.EstabelecimentoPostPutRequestDTO;
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
@DisplayName("Testa criação de estabelecimento")
public class EstabelecimentoCriarServiceTests {

    @Autowired
    EstabelecimentoCriarService driver;
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
    }

    @AfterEach
    void tearDown() {
        estabelecimentoRepository.deleteAll();
    }

    @Test
    @DisplayName("Testa criação de primeiro estabelecimento com dados válidos")
    void testCriarEstabelecimentoComDadosValidos() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        Estabelecimento resultado = driver.salvar(estabelecimentoPostPutRequestDTO);

        // Assert
        assertAll(
                () -> assertNotNull(resultado.getId()),
                () -> assertEquals(1, estabelecimentoRepository.count()),
                () -> assertEquals(estabelecimento.getCodigoAcesso(), resultado.getCodigoAcesso())
        );
    }

    @Test
    @DisplayName("Testa criação de primeiro e segundo estabelecimento com dados válidos")
    void testCriarEstabelecimentoComDadosValidos2() {
        // Arrange
        estabelecimentoRepository.save(estabelecimento);

        // Act
        Estabelecimento resultado = driver.salvar(estabelecimentoPostPutRequestDTO);

        // Assert
        assertAll(
                () -> assertNotNull(resultado.getId()),
                () -> assertEquals(2, estabelecimentoRepository.count()),
                () -> assertEquals(estabelecimento.getCodigoAcesso(), resultado.getCodigoAcesso())
        );
    }
}

