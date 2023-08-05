package com.ufcg.psoft.pitsa.service.pedido;

import com.ufcg.psoft.pitsa.exception.ClienteNaoExisteException;
import com.ufcg.psoft.pitsa.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.pitsa.exception.PedidoNaoExisteException;
import com.ufcg.psoft.pitsa.model.*;
import com.ufcg.psoft.pitsa.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@DisplayName("Testes do Serviço de listagem de pedido por clientes")
public class ClienteListarPedidoServiceTests {
    @Autowired
    ClienteListarPedidoService driver;
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    SaborRepository saborRepository;
    @Autowired
    PizzaRepository pizzaRepository;

    Cliente cliente;
    Sabor sabor1;
    Sabor sabor2;
    Pizza pizza1;
    Pizza pizza2;
    Estabelecimento estabelecimento;
    Pedido pedido;

    @BeforeEach
    void setup() {
        estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                .codigoAcesso("654321")
                .build());
        sabor1 = saborRepository.save(Sabor.builder()
                .nome("Sabor Um")
                .tipo("salgado")
                .precoM(10.0)
                .precoG(20.0)
                .disponivel(true)
                .build());
        sabor2 = saborRepository.save(Sabor.builder()
                .nome("Sabor Dois")
                .tipo("doce")
                .precoM(10.0)
                .precoG(30.0)
                .disponivel(true)
                .build());
        cliente = clienteRepository.save(Cliente.builder()
                .nome("Anton Ego")
                .endereco("Paris")
                .codigoAcesso("123456")
                .build());
        pizza1 = Pizza.builder()
                .sabor1(sabor1)
                .tamanho("media")
                .build();
        List<Pizza> pizzas = List.of(pizza1);
        pedido = pedidoRepository.save(Pedido.builder()
                .preco(10.0)
                .enderecoEntrega("Casa 237")
                .clienteId(cliente.getId())
                .estabelecimentoId(estabelecimento.getId())
                .pizzas(pizzas)
                .build());
    }

    @AfterEach
    void tearDown() {
        clienteRepository.deleteAll();
        estabelecimentoRepository.deleteAll();
        pedidoRepository.deleteAll();
        saborRepository.deleteAll();
    }

    @Test
    @DisplayName("Quando um cliente lista todos os seus pedidos salvos")
    void quandoListamosTodosOsPedidosSalvosPrimeiro() {
        pizza2 = Pizza.builder()
                .sabor1(sabor1)
                .sabor2(sabor2)
                .tamanho("grande")
                .build();
        List<Pizza> pizzas = List.of(pizza1, pizza2);
        Pedido pedido1 = pedidoRepository.save(Pedido.builder()
                .preco(25.0)
                .enderecoEntrega("Apartamento 237")
                .clienteId(cliente.getId())
                .estabelecimentoId(estabelecimento.getId())
                .pizzas(pizzas)
                .build());

        // Act
        List<Pedido> resultado = driver.clienteListar(null, cliente.getId());

        // Assert
        assertAll(
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals(pedido, resultado.get(0)),
                () -> assertEquals(pedido1, resultado.get(1)));
    }

    @Test
    @DisplayName("Quando um cliente lista um pedido salvo pelo id primeiro")
    void quandoListamosUmPedidoSalvoPeloIdPrimeiro() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        List<Pedido> resultado = driver.clienteListar(pedido.getId(), cliente.getId());

        // Assert
        assertAll(
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals(pedido, resultado.get(0))
        );
    }

    @Test
    @DisplayName("Quando um cliente lista um pedido salvo pelo id segundo ou posterior")
    void quandoListamosUmPedidoSalvoPeloIdSegundoOuPosterior() {
        // Arrange
        pizza2 = Pizza.builder()
                .sabor1(sabor1)
                .sabor2(sabor2)
                .tamanho("grande")
                .build();
        List<Pizza> pizzas = List.of(pizza1, pizza2);
        Pedido pedido1 = pedidoRepository.save(Pedido.builder()
                .preco(25.0)
                .enderecoEntrega("Apartamento 237")
                .clienteId(cliente.getId())
                .estabelecimentoId(estabelecimento.getId())
                .pizzas(pizzas)
                .build());

        // Act
        List<Pedido> resultado = driver.clienteListar(pedido1.getId(), cliente.getId());

        // Assert
        assertAll(
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals(pedido1, resultado.get(0))
        );
    }

    @Test
    @DisplayName("Quando listamos um pedido pelo id inexistente")
    void quandoListamosUmPedidoPeloIdInexistente() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        PedidoNaoExisteException thrown = assertThrows(
                PedidoNaoExisteException.class,
                () -> driver.clienteListar(999L, cliente.getId())
        );

        // Assert
        assertEquals("O pedido consultado nao existe!", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando um cliente lista um pedido passando seu id invalido")
    void quandoListamosUmPedidoPassandoIdClienteInvalido() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        ClienteNaoExisteException thrown = assertThrows(
                ClienteNaoExisteException.class,
                () -> driver.clienteListar(pedido.getId(), 999L)
        );

        // Assert
        assertEquals("O cliente consultado nao existe!", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando um cliente lista um pedido que não é dele")
    void quandoListamosUmPedidoQueNaoEhDoCliente() {
        // Arrange
        Cliente cliente2 = clienteRepository.save(Cliente.builder()
                .nome("Rute")
                .endereco("UFCG")
                .codigoAcesso("121212")
                .build());

        // Act
        CodigoDeAcessoInvalidoException thrown = assertThrows(
                CodigoDeAcessoInvalidoException.class,
                () -> driver.clienteListar(pedido.getId(), cliente2.getId())
        );

        // Assert
        assertEquals("Codigo de acesso invalido!", thrown.getMessage());
    }
}