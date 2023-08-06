package com.ufcg.psoft.pitsa.service.sabor;

import com.ufcg.psoft.pitsa.exception.SaborNaoExisteException;
import com.ufcg.psoft.pitsa.model.Cliente;
import com.ufcg.psoft.pitsa.model.Sabor;
import com.ufcg.psoft.pitsa.repository.ClienteRepository;
import com.ufcg.psoft.pitsa.repository.SaborRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("Testes do Serviço de notificar clientes interessados em sabor")
class SaborNotificarClientesServiceTests {

    @Autowired
    SaborNotificarClientesService driver;

    @Autowired
    SaborRepository saborRepository;
    @Autowired
    ClienteRepository clienteRepository;

    Sabor sabor;
    Cliente cliente;

    @BeforeEach
    void setup() {
        cliente = clienteRepository.save(Cliente.builder()
                .nome("Cliente Um da Silva")
                .endereco("Rua dos Testes, 123")
                .codigoAcesso("123456")
                .build()
        );
        sabor = saborRepository.save(Sabor.builder()
                .nome("Sabor Um")
                .tipo("salgado")
                .precoM(10.0)
                .precoG(20.0)
                .clientesInteressados(new HashSet<>() {{
                    add(cliente);
                }})
                .build()
        );
    }

    @AfterEach
    void tearDown() {
        saborRepository.deleteAll();
        clienteRepository.deleteAll();
    }

    @Test
    @DisplayName("Quando notificamos clientes interessados em sabor valido")
    void quandoNotificamosClientesInteressadosEmSaborValido() {
        // Arrange

        // Act
        List<String> resultado = driver.notificarClientes(sabor.getId());

        // Assert

        assertAll(
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals(cliente.getNome() + ", o sabor " + sabor.getNome() + " está disponível!", resultado.get(0)),
                () -> assertEquals(0, sabor.getClientesInteressados().size())
        );
    }

    @Test
    @DisplayName("Quando notificamos clientes interessados em sabor invalido")
    void quandoNotificamosClientesInteressadosEmSaborInvalido() {
        // Arrange

        // Act
        SaborNaoExisteException thrown = assertThrows(
                SaborNaoExisteException.class,
                () -> driver.notificarClientes(9999L)
        );

        // Assert
        assertEquals("O sabor consultado nao existe!", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando notificamos clientes interessados em sabor ainda indisponivel")
    void quandoNotificamosClientesInteressadosEmSaborAindaIndisponivel() {
        // Arrange
        sabor.setDisponivel(false);
        saborRepository.save(sabor);

        // Act
        List<String> resultado = driver.notificarClientes(sabor.getId());

        // Assert
        assertAll(
                () -> assertEquals(0, resultado.size()),
                () -> assertEquals(1, sabor.getClientesInteressados().size())
        );
    }
}
