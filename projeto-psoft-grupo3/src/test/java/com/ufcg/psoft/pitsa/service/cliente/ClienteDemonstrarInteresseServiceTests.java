package com.ufcg.psoft.pitsa.service.cliente;

import com.ufcg.psoft.pitsa.exception.ClienteNaoExisteException;
import com.ufcg.psoft.pitsa.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.pitsa.exception.SaborJaDisponivelException;
import com.ufcg.psoft.pitsa.exception.SaborNaoExisteException;
import com.ufcg.psoft.pitsa.model.Cliente;
import com.ufcg.psoft.pitsa.model.Sabor;
import com.ufcg.psoft.pitsa.repository.ClienteRepository;
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
@DisplayName("Testes do Serviço de demonstar interesse em sabor")
class ClienteDemonstrarInteresseServiceTests {

    @Autowired
    ClienteDemonstrarInteresseService driver;

    @Autowired
    SaborRepository saborRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    ClienteRepository clienteRepository;

    Sabor sabor;
    Cliente cliente;

    @BeforeEach
    void setup() {
        sabor = saborRepository.save(Sabor.builder()
                .nome("Sabor Um")
                .tipo("salgado")
                .precoM(10.0)
                .precoG(20.0)
                .disponivel(false)
                .build());
        cliente = clienteRepository.save(Cliente.builder()
                .nome("Cliente Um da Silva")
                .endereco("Rua dos Testes, 123")
                .codigoAcesso("123456")
                .build()
        );
    }

    @AfterEach
    void tearDown() {
        saborRepository.deleteAll();
        estabelecimentoRepository.deleteAll();
        clienteRepository.deleteAll();
    }

    @Test
    @DisplayName("Quando demonstramos interesse em um sabor valido")
    void quandoDemonstramosInteresseEmUmSaborValido() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        Sabor resultado = driver.demonstrarInteresse(cliente.getId(), cliente.getCodigoAcesso(), sabor.getId());

        // Assert
        assertAll(
                () -> assertFalse(resultado.isDisponivel()),
                () -> assertEquals(1, resultado.getClientesInteressados().size()),
                () -> assertTrue(resultado.getClientesInteressados().contains(cliente))
        );
    }

    @Test
    @DisplayName("Quando mais de um cliente demonstra interesse em um sabor valido")
    void quandoMaisDeUmClienteDemonstraInteresseEmUmSaborValido() {
        // Arrange
        Cliente cliente1 = clienteRepository.save(Cliente.builder()
                .nome("Cliente Dois dos Santos")
                .endereco("Rua Testada, 321")
                .codigoAcesso("654321")
                .build()
        );

        // Act
        driver.demonstrarInteresse(cliente.getId(), cliente.getCodigoAcesso(), sabor.getId());
        Sabor resultado = driver.demonstrarInteresse(cliente1.getId(), cliente1.getCodigoAcesso(), sabor.getId());

        // Assert
        assertAll(
                () -> assertFalse(resultado.isDisponivel()),
                () -> assertEquals(2, resultado.getClientesInteressados().size()),
                () -> assertTrue(resultado.getClientesInteressados().contains(cliente)),
                () -> assertTrue(resultado.getClientesInteressados().contains(cliente1))
        );
    }

    @Test
    @DisplayName("Quando demonstramos interesse em um sabor codigo de acesso invalido")
    void quandoDemonstramosInteresseEmUmSaborCodigoDeAcessoInvalido() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        CodigoDeAcessoInvalidoException thrown = assertThrows(
                CodigoDeAcessoInvalidoException.class,
                () -> driver.demonstrarInteresse(cliente.getId(), "1234567", sabor.getId())
        );

        // Assert
        assertAll(
                () -> assertFalse(sabor.isDisponivel()),
                () -> assertEquals(0, sabor.getClientesInteressados().size()),
                () -> assertEquals("Codigo de acesso invalido!", thrown.getMessage())
        );
    }

    @Test
    @DisplayName("Quando demonstramos interesse em um sabor inexistente")
    void quandoDemonstramosInteresseEmUmSaborInexistente() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        SaborNaoExisteException thrown = assertThrows(
                SaborNaoExisteException.class,
                () -> driver.demonstrarInteresse(cliente.getId(), cliente.getCodigoAcesso(), 9999L)
        );

        // Assert
        assertAll(
                () -> assertFalse(sabor.isDisponivel()),
                () -> assertEquals(0, sabor.getClientesInteressados().size()),
                () -> assertEquals("O sabor consultado nao existe!", thrown.getMessage())
        );
    }

    @Test
    @DisplayName("Quando um cliente inexistente demonstra interesse em um sabor")
    void quandoUmClienteInexistenteDemonstraInteresseEmUmSabor() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        ClienteNaoExisteException thrown = assertThrows(
                ClienteNaoExisteException.class,
                () -> driver.demonstrarInteresse(9999L, cliente.getCodigoAcesso(), sabor.getId())
        );

        // Assert
        assertAll(
                () -> assertFalse(sabor.isDisponivel()),
                () -> assertEquals(0, sabor.getClientesInteressados().size()),
                () -> assertEquals("O cliente consultado nao existe!", thrown.getMessage())
        );
    }

    @Test
    @DisplayName("Quando um cliente demonstra interesse em um sabor que já está disponível")
    void quandoUmClienteDemonstraInteresseEmUmSaborQueJaEstaDisponivel() {
        // Arrange
        sabor.setDisponivel(true);
        saborRepository.save(sabor);

        // Act
        SaborJaDisponivelException thrown = assertThrows(
                SaborJaDisponivelException.class,
                () -> driver.demonstrarInteresse(cliente.getId(), cliente.getCodigoAcesso(), sabor.getId())
        );

        // Assert
        assertAll(
                () -> assertTrue(sabor.isDisponivel()),
                () -> assertEquals(0, sabor.getClientesInteressados().size()),
                () -> assertEquals("O sabor consultado ja esta disponivel!", thrown.getMessage())
        );
    }
}
