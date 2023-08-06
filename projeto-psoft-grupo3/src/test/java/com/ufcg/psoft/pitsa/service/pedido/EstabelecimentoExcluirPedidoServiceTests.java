package com.ufcg.psoft.pitsa.service.pedido;

import com.ufcg.psoft.pitsa.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.pitsa.exception.EstabelecimentoNaoExisteException;
import com.ufcg.psoft.pitsa.exception.PedidoNaoExisteException;
import com.ufcg.psoft.pitsa.model.*;
import com.ufcg.psoft.pitsa.repository.ClienteRepository;
import com.ufcg.psoft.pitsa.repository.EstabelecimentoRepository;
import com.ufcg.psoft.pitsa.repository.PedidoRepository;
import com.ufcg.psoft.pitsa.repository.SaborRepository;
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
@DisplayName("Testes do Serviço de exclusão de pedidos por parte de estabelecimento")
public class EstabelecimentoExcluirPedidoServiceTests {

    @Autowired
    EstabelecimentoExcluirPedidoService driver;
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    SaborRepository saborRepository;

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
    @DisplayName("Quando um estabelecimento exclui um pedido salvo pelo id")
    void quandoExcluimosUmPedidoSalvoPeloId() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        driver.estabelecimentoExcluir(pedido.getId(), estabelecimento.getId());

        // Assert
        assertEquals(0, pedidoRepository.count());
    }

    @Test
    @DisplayName("Quando um estabelecimento excluí um pedido salvo pelo id segundo ou posterior")
    void quandoExcluimosUmPedidoSalvoPeloIdSegundoOuPosterior() {
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
        driver.estabelecimentoExcluir(pedido.getId(), estabelecimento.getId());

        // Assert
        assertAll(
                () -> assertEquals(1, pedidoRepository.count()),
                () -> assertEquals(pedido1, pedidoRepository.findAll().get(0))
        );
    }

    @Test
    @DisplayName("Quando um estabelecimento excluí todos pedidos salvos")
    void quandoExcluimosTodosOsPedidosSalvos() {
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
        driver.estabelecimentoExcluir(null, estabelecimento.getId());

        // Assert
        assertEquals(0, pedidoRepository.count());
    }

    @Test
    @DisplayName("Quando um estabelecimento excluí um pedido salvo pelo id inexistente")
    void quandoExcluimosUmPedidoSalvoPeloIdInexistente() throws PedidoNaoExisteException {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        PedidoNaoExisteException thrown = assertThrows(
                PedidoNaoExisteException.class,
                () -> driver.estabelecimentoExcluir(999L, estabelecimento.getId())
        );

        // Assert
        assertAll(
                () -> assertEquals("O pedido consultado nao existe!", thrown.getMessage()),
                () -> assertEquals(1, pedidoRepository.count())
        );
    }


    @Test
    @DisplayName("Quando excluímos um pedido salvo pelo id com id de estabelecimento invalido")
    void quandoExcluimosUmPedidoSalvoPeloIdComCodigoDeNulo() throws EstabelecimentoNaoExisteException {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        EstabelecimentoNaoExisteException thrown = assertThrows(
                EstabelecimentoNaoExisteException.class,
                () -> driver.estabelecimentoExcluir(pedido.getId(), 999L)
        );

        // Assert
        assertAll(
                () -> assertEquals("O estabelecimento consultado nao existe!", thrown.getMessage()),
                () -> assertEquals(1, pedidoRepository.count())
        );
    }

    @Test
    @DisplayName("Quando um Estabelecimento tenta excluir um pedido que não é dele")
    void quandoExcluimosUmPedidoQueNaoEhDoEstabelecimento() throws PedidoNaoExisteException {
        // Arrange
        Estabelecimento estabelecimento2 = estabelecimentoRepository.save(Estabelecimento.builder()
                .codigoAcesso("654301")
                .build());

        // Act
        CodigoDeAcessoInvalidoException thrown = assertThrows(
                CodigoDeAcessoInvalidoException.class,
                () -> driver.estabelecimentoExcluir(pedido.getId(), estabelecimento2.getId())
        );

        // Assert
        assertAll(
                () -> assertEquals("Codigo de acesso invalido!", thrown.getMessage()),
                () -> assertEquals(1, pedidoRepository.count())
        );
    }
}
