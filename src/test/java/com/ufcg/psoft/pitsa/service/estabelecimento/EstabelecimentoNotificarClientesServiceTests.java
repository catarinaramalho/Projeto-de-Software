package com.ufcg.psoft.pitsa.service.estabelecimento;

import com.ufcg.psoft.pitsa.dto.PedidoPostPutRequestDTO;
import com.ufcg.psoft.pitsa.exception.*;
import com.ufcg.psoft.pitsa.model.*;
import com.ufcg.psoft.pitsa.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@DisplayName("Testes do Serviço de notificação de clientes quando pedido está sem entregador disponível")
class EstabelecimentoNotificarClientesServiceTests {

    @Autowired
    EstabelecimentoNotificarClientesService driver;

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    EntregadorRepository entregadorRepository;
    @Autowired
    SaborRepository saborRepository;
    @Autowired
    PizzaRepository pizzaRepository;
    @Autowired
    PedidoRepository pedidoRepository;

    Cliente cliente;
    Estabelecimento estabelecimento;
    Entregador entregador;
    Sabor sabor;
    Pizza pizza;
    Pedido pedido;
    PedidoPostPutRequestDTO pedidoPostPutRequestDTO;

    @BeforeEach
    void setUp() {
        cliente = clienteRepository.save(Cliente.builder()
                .nome("Cliente Um da Silva")
                .endereco("Rua dos Testes, 123")
                .codigoAcesso("123456")
                .build()
        );
        estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                .codigoAcesso("654321")
                .build()
        );
        entregador = entregadorRepository.save(Entregador.builder()
                .nome("Entregador Gente Boa")
                .placaVeiculo("ABC-1234")
                .corVeiculo("Preto")
                .tipoVeiculo("Carro")
                .codigoAcesso("246810")
                .build()
        );
        sabor = saborRepository.save(Sabor.builder()
                .nome("Sabor Delicioso")
                .tipo("salgado")
                .precoM(10.0)
                .precoG(20.0)
                .disponivel(true)
                .build()
        );
        pizza = pizzaRepository.save(Pizza.builder()
                .sabor1(sabor)
                .tamanho("media")
                .build()
        );
        pedido = pedidoRepository.save(Pedido.builder()
                .preco(10.0)
                .enderecoEntrega("Rua dos Testes, 123")
                .clienteId(cliente.getId())
                .estabelecimentoId(estabelecimento.getId())
                .statusPagamento(true)
                .statusEntrega("Pedido pronto")
                .entregadorId(entregador.getId())
                .pizzas(List.of(pizza))
                .build()
        );
        pedidoPostPutRequestDTO = PedidoPostPutRequestDTO.builder()
                .enderecoEntrega(pedido.getEnderecoEntrega())
                .pizzas(pedido.getPizzas())
                .build();
    }

    @AfterEach
    void tearDown() {
        clienteRepository.deleteAll();
        estabelecimentoRepository.deleteAll();
        entregadorRepository.deleteAll();
        saborRepository.deleteAll();
        pizzaRepository.deleteAll();
        pedidoRepository.deleteAll();
    }

    @Test
    @DisplayName("Quando notificamos cliente sobre pedido válido")
    void quandoNotificamosClienteSobrePedidoValido() {
        // Arrange

        // Act
        String result = driver.notificar(pedido.getId());

        // Assert
        assertEquals("Olá, " + cliente.getNome() + "! Seu pedido #" + pedido.getId() + " está pronto mas não há entregadores disponíveis.", result);
    }

    @Test
    @DisplayName("Quando notificamos cliente sobre pedido inexistente")
    void quandoNotificamosClientePedidoInexistente() {
        // Arrange
        Long pedidoId = 9999L;

        // Act
        PedidoNaoExisteException thrown = assertThrows(
                PedidoNaoExisteException.class,
                () -> driver.notificar(pedidoId)
        );

        // Assert
        assertEquals("O pedido consultado nao existe!", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando notificamos cliente sobre pedido com status de entrega diferente de 'Pedido pronto'")
    void quandoNotificamosClientePedidoComStatusEntregaDiferenteDePedidoPronto() {
        // Arrange
        pedido.setStatusEntrega("Pedido em preparo");
        modelMapper.map(pedidoPostPutRequestDTO, pedido);
        pedidoRepository.save(pedido);

        // Act
        PedidoStatusInvalidoException thrown = assertThrows(
                PedidoStatusInvalidoException.class,
                () -> driver.notificar(pedido.getId())
        );

        // Assert
        assertEquals("O status do pedido e invalido!", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando notificamos cliente inexistente")
    void quandoNotificamosClienteInexistente() {
        // Arrange
        clienteRepository.deleteAll();

        // Act
        ClienteNaoExisteException thrown = assertThrows(
                ClienteNaoExisteException.class,
                () -> driver.notificar(pedido.getId())
        );

        // Assert
        assertEquals("O cliente consultado nao existe!", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando notificamos cliente sobre pedido com estabelecimento inexistente")
    void quandoNotificamosClientePedidoComEstabelecimentoInexistente() {
        // Arrange
        estabelecimentoRepository.deleteAll();

        // Act
        EstabelecimentoNaoExisteException thrown = assertThrows(
                EstabelecimentoNaoExisteException.class,
                () -> driver.notificar(pedido.getId())
        );

        // Assert
        assertEquals("O estabelecimento consultado nao existe!", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando notificamos cliente sobre pedido porém com entregador disponível")
    void quandoNotificamosClienteSobrePedidoComEntregadorDisponivel() {
        // Arrange
        entregador.setDisponibilidade(true);
        entregadorRepository.save(entregador);
        estabelecimento.setEntregadoresDisponiveis(new LinkedList<>(List.of(entregador)));
        estabelecimentoRepository.save(estabelecimento);

        // Act
        EstabelecimentoEntregadorDisponivelException thrown = assertThrows(
                EstabelecimentoEntregadorDisponivelException.class,
                () -> driver.notificar(pedido.getId())
        );

        // Assert
        assertEquals("Ha entregador disponivel no estabelecimento!", thrown.getMessage());
    }
}
