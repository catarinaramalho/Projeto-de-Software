package com.ufcg.psoft.pitsa.service.cliente;

import com.ufcg.psoft.pitsa.dto.ClientePostPutRequestDTO;
import com.ufcg.psoft.pitsa.model.Cliente;
import com.ufcg.psoft.pitsa.repository.ClienteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DisplayName("Testes do Serviço de criação de cliente")
class ClienteCriarServiceTests {

    @Autowired
    ClienteCriarService driver;

    @Autowired
    ClienteRepository clienteRepository;

    Cliente cliente;

    ClientePostPutRequestDTO clientePostPutRequestDTO;

    @BeforeEach
    void setup() {
        cliente = Cliente.builder()
                .nome("Cliente Um da Silva")
                .endereco("Rua dos Testes, 123")
                .codigoAcesso("123456")
                .build();
        clientePostPutRequestDTO = ClientePostPutRequestDTO.builder()
                .nome("Cliente Um da Silva")
                .endereco("Rua dos Testes, 123")
                .codigoAcesso("123456")
                .build();
    }

    @AfterEach
    void tearDown() {
        clienteRepository.deleteAll();
    }

    @Test
    @DisplayName("Quando criamos um cliente primeiro")
    void quandoCriamosUmClientePrimeiro() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        Cliente resultado = driver.salvar(clientePostPutRequestDTO);

        // Assert
        assertAll(
                () -> assertEquals(1, clienteRepository.count()),
                () -> assertEquals(cliente.getNome(), resultado.getNome()),
                () -> assertEquals(cliente.getEndereco(), resultado.getEndereco()),
                () -> assertEquals(cliente.getCodigoAcesso(), resultado.getCodigoAcesso())
        );
    }

    @Test
    @DisplayName("Quando criamos um cliente segundo ou posterior")
    void quandoCriamosUmClienteSegundoOuPosterior() {
        // Arrange
        clienteRepository.save(cliente);

        // Act
        Cliente resultado = driver.salvar(clientePostPutRequestDTO);

        // Assert
        assertAll(
                () -> assertEquals(2, clienteRepository.count()),
                () -> assertEquals(cliente.getNome(), resultado.getNome()),
                () -> assertEquals(cliente.getEndereco(), resultado.getEndereco()),
                () -> assertEquals(cliente.getCodigoAcesso(), resultado.getCodigoAcesso())
        );
    }
}
