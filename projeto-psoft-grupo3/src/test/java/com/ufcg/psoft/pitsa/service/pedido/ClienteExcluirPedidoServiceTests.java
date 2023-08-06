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

@SpringBootTest
@Transactional
@DisplayName("Testes do Serviço de exclusão de pedidos por parte de clientes")
public class ClienteExcluirPedidoServiceTests {

    @Autowired
    ClienteExcluirPedidoService driver;
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
        pizza1 = pizzaRepository.save(Pizza.builder()
                .sabor1(sabor1)
                .tamanho("media")
                .build());
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
    @DisplayName("Quando um cliente exclui um pedido que ele salvou pelo id")
    void quandoExcluimosUmPedidoSalvoPeloId() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        driver.clienteExcluir(pedido.getId(), cliente.getId());

        // Assert
        assertEquals(0, pedidoRepository.count());
    }

    @Test
    @DisplayName("Quando um cliente excluí um pedido salvo por ele pelo id segundo ou posterior")
    void quandoExcluimosUmPedidoSalvoPeloIdSegundoOuPosterior() {
        // Arrange
        pizza2 = pizzaRepository.save(Pizza.builder()
                .sabor1(sabor1)
                .sabor2(sabor2)
                .tamanho("grande")
                .build());
        List<Pizza> pizzas = List.of(pizza1, pizza2);
        Pedido pedido1 = pedidoRepository.save(Pedido.builder()
                .preco(25.0)
                .enderecoEntrega("Apartamento 237")
                .clienteId(cliente.getId())
                .estabelecimentoId(estabelecimento.getId())
                .pizzas(pizzas)
                .build());

        // Act
        driver.clienteExcluir(pedido.getId(), cliente.getId());

        // Assert
        assertAll(
                () -> assertEquals(1, pedidoRepository.count()),
                () -> assertEquals(pedido1, pedidoRepository.findAll().get(0))
        );
    }

    @Test
    @DisplayName("Quando um cliente excluí todos seus pedidos salvos")
    void quandoExcluimosTodosOsPedidosSalvos() {
        // Arrange
        pizza2 = pizzaRepository.save(Pizza.builder()
                .sabor1(sabor1)
                .sabor2(sabor2)
                .tamanho("grande")
                .build());
        List<Pizza> pizzas = List.of(pizza1, pizza2);
        Pedido pedido1 = pedidoRepository.save(Pedido.builder()
                .preco(25.0)
                .enderecoEntrega("Apartamento 237")
                .clienteId(cliente.getId())
                .estabelecimentoId(estabelecimento.getId())
                .pizzas(pizzas)
                .build());

        // Act
        driver.clienteExcluir(null, cliente.getId());

        // Assert
        assertEquals(0, pedidoRepository.count());
    }

    @Test
    @DisplayName("Quando um cliente excluí um pedido salvo pelo id inexistente")
    void quandoExcluimosUmPedidoSalvoPeloIdInexistente() throws PedidoNaoExisteException {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        PedidoNaoExisteException thrown = assertThrows(
                PedidoNaoExisteException.class,
                () -> driver.clienteExcluir(999L, cliente.getId())
        );

        // Assert
        assertAll(
                () -> assertEquals("O pedido consultado nao existe!", thrown.getMessage()),
                () -> assertEquals(1, pedidoRepository.count())
        );
    }


    @Test
    @DisplayName("Quando excluímos um pedido salvo pelo id com id de cliente invalido")
    void quandoExcluimosUmPedidoSalvoPeloIdComCodigoDeNulo() throws ClienteNaoExisteException {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        ClienteNaoExisteException thrown = assertThrows(
                ClienteNaoExisteException.class,
                () -> driver.clienteExcluir(pedido.getId(), 999L)
        );

        // Assert
        assertAll(
                () -> assertEquals("O cliente consultado nao existe!", thrown.getMessage()),
                () -> assertEquals(1, pedidoRepository.count())
        );
    }

    @Test
    @DisplayName("Quando um cliente tenta excluir um pedido que não é dele")
    void quandoExcluimosUmPedidoQueNaoEhDoCliente() throws PedidoNaoExisteException {
        // Arrange
        Cliente cliente2 = clienteRepository.save(Cliente.builder()
                .nome("Gustavo")
                .endereco("Paris")
                .codigoAcesso("654321")
                .build());

        // Act
        CodigoDeAcessoInvalidoException thrown = assertThrows(
                CodigoDeAcessoInvalidoException.class,
                () -> driver.clienteExcluir(pedido.getId(), cliente2.getId())
        );

        // Assert
        assertAll(
                () -> assertEquals("Codigo de acesso invalido!", thrown.getMessage()),
                () -> assertEquals(1, pedidoRepository.count())
        );
    }
}
