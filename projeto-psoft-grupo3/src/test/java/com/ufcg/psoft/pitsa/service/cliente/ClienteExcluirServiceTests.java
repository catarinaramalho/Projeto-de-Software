package com.ufcg.psoft.pitsa.service.cliente;

import com.ufcg.psoft.pitsa.exception.ClienteNaoExisteException;
import com.ufcg.psoft.pitsa.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.pitsa.model.Cliente;
import com.ufcg.psoft.pitsa.repository.ClienteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Testes do Serviço de exclusão de cliente")
public class ClienteExcluirServiceTests {

    @Autowired
    ClienteExcluirService driver;

    @Autowired
    ClienteRepository clienteRepository;

    Cliente cliente;

    @BeforeEach
    void setup() {
        cliente = clienteRepository.save(Cliente.builder()
                .nome("Cliente Um da Silva")
                .endereco("Rua dos Testes, 123")
                .codigoAcesso("123456")
                .build()
        );
    }

    @AfterEach
    void tearDown() {
        clienteRepository.deleteAll();
    }

    @Test
    @DisplayName("Quando excluímos um cliente salvo pelo id primeiro")
    void quandoExcluimosUmClienteSalvoPeloIdPrimeiro() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        driver.excluir(cliente.getId(), cliente.getCodigoAcesso());

        // Assert
        assertEquals(0, clienteRepository.count());
    }

    @Test
    @DisplayName("Quando excluímos um cliente salvo pelo id segundo ou posterior")
    void quandoExcluimosUmClienteSalvoPeloIdSegundoOuPosterior() {
        // Arrange
        Cliente cliente1 = clienteRepository.save(Cliente.builder()
                .nome("Cliente Dois dos Santos")
                .endereco("Rua Testada, 321")
                .codigoAcesso("654321")
                .build()
        );

        // Act
        driver.excluir(cliente.getId(), cliente.getCodigoAcesso());

        // Assert
        assertAll(
                () -> assertEquals(1, clienteRepository.count()),
                () -> assertEquals(cliente1, clienteRepository.findAll().get(0))
        );
    }

    @Test
    @DisplayName("Quando excluímos um cliente pelo id inexistente")
    void quandoExcluimosUmClientePeloIdInexistente() throws ClienteNaoExisteException {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        ClienteNaoExisteException thrown = assertThrows(
                ClienteNaoExisteException.class,
                () -> driver.excluir(999L, null)
        );

        // Assert
        assertAll(
                () -> assertEquals("O cliente consultado nao existe!", thrown.getMessage()),
                () -> assertEquals(1, clienteRepository.count())
        );
    }

    @Test
    @DisplayName("Quando excluímos um cliente pelo id com código de nulo")
    void quandoExcluimosUmClientePeloIdComCodigoDeNulo() throws CodigoDeAcessoInvalidoException {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        CodigoDeAcessoInvalidoException thrown = assertThrows(
                CodigoDeAcessoInvalidoException.class,
                () -> driver.excluir(cliente.getId(), null)
        );

        // Assert
        assertAll(
                () -> assertEquals("Codigo de acesso invalido!", thrown.getMessage()),
                () -> assertEquals(1, clienteRepository.count())
        );
    }

    @Test
    @DisplayName("Quando excluímos um cliente pelo id com código de acesso inválido")
    void quandoExcluimosUmClientePeloIdComCodigoDeAcessoInvalido() throws CodigoDeAcessoInvalidoException {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        CodigoDeAcessoInvalidoException thrown = assertThrows(
                CodigoDeAcessoInvalidoException.class,
                () -> driver.excluir(cliente.getId(), "999999")
        );

        // Assert
        assertAll(
                () -> assertEquals("Codigo de acesso invalido!", thrown.getMessage()),
                () -> assertEquals(1, clienteRepository.count())
        );
    }
}
