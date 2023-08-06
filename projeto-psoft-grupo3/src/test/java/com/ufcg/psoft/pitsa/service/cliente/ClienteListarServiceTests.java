package com.ufcg.psoft.pitsa.service.cliente;

import com.ufcg.psoft.pitsa.dto.ClienteGetRequestDTO;
import com.ufcg.psoft.pitsa.exception.ClienteNaoExisteException;
import com.ufcg.psoft.pitsa.model.Cliente;
import com.ufcg.psoft.pitsa.repository.ClienteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Testes do Serviço de listagem de cliente")
class ClienteListarServiceTests {

    @Autowired
    ClienteListarService driver;

    @Autowired
    ClienteRepository clienteRepository;

    Cliente cliente;

    ClienteGetRequestDTO clienteDTO;

    @BeforeEach
    void setup() {
        cliente = clienteRepository.save(Cliente.builder()
                .nome("Cliente Um da Silva")
                .endereco("Rua dos Testes, 123")
                .codigoAcesso("123456")
                .build()
        );

        clienteDTO = new ClienteGetRequestDTO(cliente);
    }

    @AfterEach
    void tearDown() {
        clienteRepository.deleteAll();
    }

    @Test
    @DisplayName("Quando listamos todos os cliente salvos primeiro")
    void quandoListamosTodosOsClienteSalvosPrimeiro() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        List<ClienteGetRequestDTO> resultado = driver.listar(null);

        // Assert
        assertAll(
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals(clienteDTO, resultado.get(0))
        );
    }

    @Test
    @DisplayName("Quando listamos todos os cliente salvos segundo ou posterior")
    void quandoListamosTodosOsClienteSalvosSegundoOuPosterior() {
        // Arrange
        Cliente cliente1 = clienteRepository.save(Cliente.builder()
                .nome("Cliente Dois dos Santos")
                .endereco("Rua Testada, 321")
                .codigoAcesso("654321")
                .build()
        );

        ClienteGetRequestDTO clienteDTO1 = new ClienteGetRequestDTO(cliente1);

        // Act
        List<ClienteGetRequestDTO> resultado = driver.listar(null);

        // Assert
        assertAll(
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals(clienteDTO, resultado.get(0)),
                () -> assertEquals(clienteDTO1, resultado.get(1))
        );
    }

    @Test
    @DisplayName("Quando listamos um cliente salvo pelo id primeiro")
    void quandoListamosUmClienteSalvoPeloIdPrimeiro() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        List<ClienteGetRequestDTO> resultado = driver.listar(cliente.getId());

        // Assert
        assertAll(
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals(clienteDTO, resultado.get(0))
        );
    }

    @Test
    @DisplayName("Quando listamos um cliente salvo pelo id segundo ou posterior")
    void quandoListamosUmClienteSalvoPeloIdSegundoOuPosterior() {
        // Arrange
        Cliente cliente1 = clienteRepository.save(Cliente.builder()
                .nome("Cliente Dois dos Santos")
                .endereco("Rua Testada, 321")
                .codigoAcesso("654321")
                .build()
        );

        ClienteGetRequestDTO clienteDTO1 = new ClienteGetRequestDTO(cliente1);

        // Act
        List<ClienteGetRequestDTO> resultado = driver.listar(cliente1.getId());

        // Assert
        assertAll(
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals(clienteDTO1, resultado.get(0))
        );
    }

    @Test
    @DisplayName("Quando listamos um cliente pelo id inexistente")
    void quandoListamosUmClientePeloIdInexistente() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        ClienteNaoExisteException thrown = assertThrows(
                ClienteNaoExisteException.class,
                () -> driver.listar(999L)
        );

        // Assert
        assertEquals("O cliente consultado nao existe!", thrown.getMessage());
    }
}
