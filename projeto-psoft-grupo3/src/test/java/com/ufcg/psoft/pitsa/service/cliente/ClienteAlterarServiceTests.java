package com.ufcg.psoft.pitsa.service.cliente;

import com.ufcg.psoft.pitsa.dto.ClientePostPutRequestDTO;
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
@DisplayName("Testes do Serviço de criação de cliente")
class ClienteAlterarServiceTests {

    @Autowired
    ClienteAlterarService driver;

    @Autowired
    ClienteRepository clienteRepository;

    Cliente cliente;

    ClientePostPutRequestDTO clientePostPutRequestDTO;

    @BeforeEach
    void setup() {
        cliente = clienteRepository.save(Cliente.builder()
                .nome("Cliente Um da Silva")
                .endereco("Rua dos Testes, 123")
                .codigoAcesso("123456")
                .build()
        );
        clientePostPutRequestDTO = ClientePostPutRequestDTO.builder()
                .nome(cliente.getNome())
                .endereco(cliente.getEndereco())
                .codigoAcesso(cliente.getCodigoAcesso())
                .build();
    }

    @AfterEach
    void tearDown() {
        clienteRepository.deleteAll();
    }

    @Test
    @DisplayName("Quando alteramos cliente com dados válidos")
    void quandoAlteramosClienteComDadosValidos() {
        // Arrange
        clientePostPutRequestDTO.setNome("Nome Novo");
        clientePostPutRequestDTO.setEndereco("Endereço Novo");
        clientePostPutRequestDTO.setCodigoAcesso("654321");

        // Act
        Cliente resultado = driver.alterar(cliente.getId(), cliente.getCodigoAcesso(), clientePostPutRequestDTO);

        // Assert
        assertAll(
                () -> assertEquals(1, clienteRepository.count()),
                () -> assertEquals(cliente.getId(), resultado.getId()),
                () -> assertEquals(clientePostPutRequestDTO.getNome(), resultado.getNome()),
                () -> assertEquals(clientePostPutRequestDTO.getEndereco(), resultado.getEndereco()),
                () -> assertEquals(clientePostPutRequestDTO.getCodigoAcesso(), resultado.getCodigoAcesso())
        );
    }

    @Test
    @DisplayName("Quando alteramos cliente com id inexistente")
    void quandoAlteramosClienteComIdInexistente() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        ClienteNaoExisteException thrown = assertThrows(
                ClienteNaoExisteException.class,
                () -> driver.alterar(2L, cliente.getCodigoAcesso(), clientePostPutRequestDTO)
        );

        // Assert
        assertAll(
                () -> assertEquals("O cliente consultado nao existe!", thrown.getMessage()),
                () -> assertEquals(1, clienteRepository.count())
        );
    }

    @Test
    @DisplayName("Quando alteramos cliente com código de acesso inválido")
    void quandoAlteramosClienteComCodigoDeAcessoInvalido() {
        // Arrange
        String codigoAcesso = "654321";

        // Act
        CodigoDeAcessoInvalidoException thrown = assertThrows(
                CodigoDeAcessoInvalidoException.class,
                () -> driver.alterar(cliente.getId(), codigoAcesso, clientePostPutRequestDTO)
        );

        // Assert
        assertAll(
                () -> assertEquals("Codigo de acesso invalido!", thrown.getMessage()),
                () -> assertEquals(1, clienteRepository.count())
        );
    }
}
