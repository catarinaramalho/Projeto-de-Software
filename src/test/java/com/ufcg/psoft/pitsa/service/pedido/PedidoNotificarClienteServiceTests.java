package com.ufcg.psoft.pitsa.service.pedido;

import com.ufcg.psoft.pitsa.dto.PedidoPostPutRequestDTO;
import com.ufcg.psoft.pitsa.exception.ClienteNaoExisteException;
import com.ufcg.psoft.pitsa.exception.EntregadorNaoExisteException;
import com.ufcg.psoft.pitsa.exception.PedidoNaoExisteException;
import com.ufcg.psoft.pitsa.exception.PedidoStatusInvalidoException;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@DisplayName("Testes do Serviço de notificação de clientes quando pedido está em rota")
class PedidoNotificarClienteServiceTests {

    @Autowired
    PedidoNotificarClienteService driver;

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
                .statusEntrega("Pedido em rota")
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
    void quandoNotificamosClientePedidoValido() {
        // Arrange
        // nenhuma necessidade além do setUp()

        // Act
        String notificacao = driver.notificar(pedido.getId());

        // Assert
        String notificacaoEsperada = cliente.getNome() + ", o seu pedido #" + pedido.getId() + ", está em rota de entrega!\n"
                + "O entregador " + entregador.getNome() + " está a caminho do seu endereço!\n"
                + "Tipo do Veículo: " + entregador.getTipoVeiculo() + "\n"
                + "Placa do Veículo: " + entregador.getPlacaVeiculo() + "\n"
                + "Cor do Veículo: " + entregador.getCorVeiculo();
        assertEquals(notificacaoEsperada, notificacao);
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
    @DisplayName("Quando notificamos cliente sobre pedido que não está em rota")
    void quandoNotificamosClientePedidoNaoEstaEmRota() {
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
    @DisplayName("Quando notificamos sobre pedido com cliente inexistente")
    void quandoNotificamosClienteInexistente() {
        // Arrange
        pedido.setClienteId(9999L);
        modelMapper.map(pedidoPostPutRequestDTO, pedido);
        pedidoRepository.save(pedido);

        // Act
        ClienteNaoExisteException thrown = assertThrows(
                ClienteNaoExisteException.class,
                () -> driver.notificar(pedido.getId())
        );

        // Assert
        assertEquals("O cliente consultado nao existe!", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando notificamos sobre pedido com entregador inexistente")
    void quandoNotificamosEntregadorInexistente() {
        // Arrange
        pedido.setEntregadorId(9999L);
        modelMapper.map(pedidoPostPutRequestDTO, pedido);
        pedidoRepository.save(pedido);

        // Act
        EntregadorNaoExisteException thrown = assertThrows(
                EntregadorNaoExisteException.class,
                () -> driver.notificar(pedido.getId())
        );

        // Assert
        assertEquals("O entregador consultado nao existe!", thrown.getMessage());
    }
}
