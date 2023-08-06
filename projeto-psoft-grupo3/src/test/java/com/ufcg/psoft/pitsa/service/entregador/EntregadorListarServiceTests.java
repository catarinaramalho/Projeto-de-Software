package com.ufcg.psoft.pitsa.service.entregador;

import com.ufcg.psoft.pitsa.dto.EntregadorGetRequestDTO;
import com.ufcg.psoft.pitsa.exception.EntregadorNaoExisteException;
import com.ufcg.psoft.pitsa.model.Entregador;
import com.ufcg.psoft.pitsa.repository.EntregadorRepository;
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
@DisplayName("Testes do Serviço de listagem de entregador")
class EntregadorListarServiceTests {

    @Autowired
    EntregadorListarService driver;

    @Autowired
    EntregadorRepository entregadorRepository;

    Entregador entregador;

    EntregadorGetRequestDTO entregadorDTO;

    @BeforeEach
    void setUp() {
        entregador = Entregador.builder()
                .nome("João")
                .placaVeiculo("ABC-1234")
                .corVeiculo("Azul")
                .tipoVeiculo("Moto")
                .codigoAcesso("123456")
                .build();
        entregadorRepository.save(entregador);

        entregadorDTO = new EntregadorGetRequestDTO(entregador);
    }

    @AfterEach
    void tearDown() {
        entregadorRepository.deleteAll();
    }

    @Test
    @DisplayName("Quando listamos todos os entregador salvos primeiro")
    void quandoListamosTodosOsEntregadorSalvosPrimeiro() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        List<EntregadorGetRequestDTO> resultado = driver.listar(null);

        // Assert
        assertAll(
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals(entregadorDTO, resultado.get(0))
        );
    }

    @Test
    @DisplayName("Quando listamos todos os entregador salvos segundo ou posterior")
    void quandoListamosTodosOsEntregadorSalvosSegundoOuPosterior() {
        // Arrange
        EntregadorGetRequestDTO entregadorDTO1 = new EntregadorGetRequestDTO(entregadorRepository.save(Entregador.builder()
                .nome("Lana Del Rey 2 dos Santos")
                .placaVeiculo("ABC-1234")
                .corVeiculo("Azul")
                .tipoVeiculo("Moto")
                .codigoAcesso("654321")
                .build()
        ));

        EntregadorGetRequestDTO entregadorDTO2 = new EntregadorGetRequestDTO(entregador);

        // Act
        List<EntregadorGetRequestDTO> resultado = driver.listar(null);

        // Assert
        assertAll(
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals(entregadorDTO2, resultado.get(0)),
                () -> assertEquals(entregadorDTO1, resultado.get(1))
        );
    }

    @Test
    @DisplayName("Quando listamos um entregador salvo pelo id primeiro")
    void quandoListamosUmEntregadorSalvoPeloIdPrimeiro() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        List<EntregadorGetRequestDTO> resultado = driver.listar(entregador.getId());

        // Assert
        assertAll(
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals(entregadorDTO, resultado.get(0))
        );
    }

    @Test
    @DisplayName("Quando listamos um entregador salvo pelo id segundo ou posterior")
    void quandoListamosUmEntregadorSalvoPeloIdSegundoOuPosterior() {
        // Arrange
        EntregadorGetRequestDTO entregadorDTO1 = new EntregadorGetRequestDTO(entregadorRepository.save(Entregador.builder()
                .nome("Lana Del Rey 2 dos Santos")
                .placaVeiculo("ABC-1234")
                .corVeiculo("Azul")
                .tipoVeiculo("Moto")
                .codigoAcesso("654321")
                .build()
        ));

        // Act
        List<EntregadorGetRequestDTO> resultado = driver.listar(entregadorDTO1.getId());

        // Assert
        assertAll(
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals(entregadorDTO1, resultado.get(0))
        );
    }

    @Test
    @DisplayName("Quando listamos um entregador pelo id inexistente")
    void quandoListamosUmEntregadorPeloIdInexistente() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        EntregadorNaoExisteException thrown = assertThrows(
                EntregadorNaoExisteException.class,
                () -> driver.listar(999L)
        );

        // Assert
        assertEquals("O entregador consultado nao existe!", thrown.getMessage());
    }
}
