package com.ufcg.psoft.pitsa.service.pedido;

import com.ufcg.psoft.pitsa.dto.PedidoPostPutRequestDTO;
import com.ufcg.psoft.pitsa.exception.CancelamentoPedidoInvalidoException;
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
@DisplayName("Testes do Serviço de cancelamento de pedido")
class PedidoCancelarServiceTests {

    @Autowired
    PedidoCancelarService driver;
    @Autowired
    EstabelecimentoPrepararPedidoService prepararPedido;

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
    @Autowired
    EntregadorRepository entregadorRepository;

    Cliente cliente;
    Entregador entregador;
    Sabor sabor1;
    Sabor sabor2;
    Estabelecimento estabelecimento;

    Pedido pedido;
    PedidoPostPutRequestDTO pedidoPostPutRequestDTO;
    Pizza pizzaMedia;
    Pizza pizzaGrande;
    List<Pizza> pizzasM;
    List<Pizza> pizzasG;
    List<Pizza> pizzasMG;

    @BeforeEach
    void setup() {
        estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                .codigoAcesso("654321")
                .build()
        );
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
                .nome("Cliente 1")
                .endereco("Rua 1")
                .codigoAcesso("123456")
                .build());
        entregador = entregadorRepository.save(Entregador.builder()
                .nome("Joãozinho")
                .placaVeiculo("ABC-1234")
                .corVeiculo("Azul")
                .tipoVeiculo("Moto")
                .codigoAcesso("101010")
                .build());
        pizzaMedia = pizzaRepository.save(Pizza.builder()
                .sabor1(sabor1)
                .tamanho("media")
                .build());
        pizzaGrande = pizzaRepository.save(Pizza.builder()
                .sabor1(sabor1)
                .sabor2(sabor2)
                .tamanho("grande")
                .build());
        // Lista com uma pizza media
        pizzasM = List.of(pizzaMedia);
        // Lista com uma pizza grande
        pizzasG = List.of(pizzaGrande);
        // Lista com duas pizzas, uma media e uma grande
        pizzasMG = List.of(pizzaMedia, pizzaGrande);

        pedido = pedidoRepository.save(Pedido.builder()
                .preco(10.0)
                .enderecoEntrega("Casa 237")
                .clienteId(cliente.getId())
                .estabelecimentoId(estabelecimento.getId())
                .entregadorId(entregador.getId())
                .pizzas(pizzasM)
                .build());
        pedidoPostPutRequestDTO = PedidoPostPutRequestDTO.builder()
                .enderecoEntrega("Apartamento 237")
                .pizzas(pizzasM)
                .build();
    }

    @AfterEach
    void tearDown() {
        clienteRepository.deleteAll();
        entregadorRepository.deleteAll();
        estabelecimentoRepository.deleteAll();
        pedidoRepository.deleteAll();
        saborRepository.deleteAll();
    }

    @Test
    @DisplayName("Teste de cancelamento válido de um pedido primeiro")
    void testCancelamentoValidoPedido() {
        // Arrange
        // Nada mais necessário além do setup

        // Act
        driver.cancelar(pedido.getId(), cliente.getCodigoAcesso());

        // Assert
        assertEquals(0, pedidoRepository.count());
    }

    @Test
    @DisplayName("Teste de cancelamento válido de um pedido segundo")
    void testCancelamentoValidoPedidoSegundo() {
        // Arrange
        Pedido pedido2 = pedidoRepository.save(Pedido.builder()
                .preco(25.0)
                .enderecoEntrega("Laguinho da UFCG")
                .clienteId(cliente.getId())
                .estabelecimentoId(estabelecimento.getId())
                .entregadorId(entregador.getId())
                .pizzas(pizzasG)
                .build());

        // Act
        driver.cancelar(pedido2.getId(), cliente.getCodigoAcesso());

        // Assert
        assertEquals(1, pedidoRepository.count());
    }

    @Test
    @DisplayName("Teste de cancelamento de um pedido inexistente")
    void testCancelamentoPedidoInexistente() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        PedidoNaoExisteException thrown = assertThrows(
                PedidoNaoExisteException.class,
                () -> driver.cancelar(999L, cliente.getCodigoAcesso())
        );

        // Assert
        assertAll(
                () -> assertEquals("O pedido consultado nao existe!", thrown.getMessage()),
                () -> assertEquals(1, pedidoRepository.count())
        );
    }

    @Test
    @DisplayName("Quando um cliente tenta cancelar um pedido de outro cliente")
    void testCancelamentoPedidoDeOutroCliente() {
        // Arrange
        Cliente cliente2 = clienteRepository.save(Cliente.builder()
                .nome("Cliente 2")
                .endereco("Rua 2")
                .codigoAcesso("654321")
                .build());

        // Act
        CodigoDeAcessoInvalidoException thrown = assertThrows(
                CodigoDeAcessoInvalidoException.class,
                () -> driver.cancelar(pedido.getId(), cliente2.getCodigoAcesso())
        );

        // Assert
        assertAll(
                () -> assertEquals("Codigo de acesso invalido!", thrown.getMessage()),
                () -> assertEquals(1, pedidoRepository.count())
        );
    }

    @Test
    @DisplayName("Quando um cliente tenta cancelar um pedido que já está pronto")
    void testCancelamentoPedidoPronto() {
        // Arrange
        pedido.setStatusEntrega("Pedido pronto");

        // Act
        CancelamentoPedidoInvalidoException thrown = assertThrows(
                CancelamentoPedidoInvalidoException.class,
                () -> driver.cancelar(pedido.getId(), cliente.getCodigoAcesso())
        );

        // Assert
        assertAll(
                () -> assertEquals("Pedidos que ja estao prontos nao podem ser cancelados!", thrown.getMessage()),
                () -> assertEquals(1, pedidoRepository.count())
        );
    }
}
