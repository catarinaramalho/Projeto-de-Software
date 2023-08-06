package com.ufcg.psoft.pitsa.service.pedido;

import com.ufcg.psoft.pitsa.dto.PedidoPostPutRequestDTO;
import com.ufcg.psoft.pitsa.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.pitsa.exception.PedidoStatusInvalidoException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@DisplayName("Testes do Serviço de alteração de pedido")
public class PedidoConfirmarPagamentoServiceTests {

    @Autowired
    PedidoConfirmarPagamentoService driver;
    @Autowired
    PedidoAlterarService pedidoAlterarService;
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
    Pizza pizzaM;
    Estabelecimento estabelecimento;
    Pedido pedido;
    PedidoPostPutRequestDTO pedidoPostPutRequestDTO;

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
        pizzaM = Pizza.builder()
                .sabor1(sabor1)
                .tamanho("media")
                .build();
        List<Pizza> pizzas = List.of(pizzaM);
        pedido = pedidoRepository.save(Pedido.builder()
                .preco(10.0)
                .enderecoEntrega("Casa 237")
                .clienteId(cliente.getId())
                .estabelecimentoId(estabelecimento.getId())
                .pizzas(pizzas)
                .build());
        pedidoPostPutRequestDTO = PedidoPostPutRequestDTO.builder()
                .enderecoEntrega("Apartamento 237")
                .pizzas(pizzas)
                .build();
    }

    @AfterEach
    void tearDown() {
        clienteRepository.deleteAll();
        estabelecimentoRepository.deleteAll();
        pedidoRepository.deleteAll();
        saborRepository.deleteAll();
    }

    @Test
    @DisplayName("Confirmar pagamento por cartão de crédito")
    void testConfirmarPedidoCredito() {
        // Arrange
        // Act
        Pedido resultado = driver.confirmarPagamento(cliente.getId(), pedido.getId(), "Cartão de Crédito", cliente.getCodigoAcesso(), pedidoPostPutRequestDTO);

        // Assert
        assertEquals(true, resultado.getStatusPagamento());
        assertEquals(10, resultado.getPreco());

    }

    @Test
    @DisplayName("Confirmar pagamento por cartão de débito")
    void testConfirmarPedidoDebito() {
        // Arrange

        // Act
        Pedido resultado = driver.confirmarPagamento(cliente.getId(), pedido.getId(), "Cartão de débito", cliente.getCodigoAcesso(), pedidoPostPutRequestDTO);

        // Assert
        assertEquals(true, resultado.getStatusPagamento());
        assertEquals(9.75, resultado.getPreco());

    }

    @Test
    @DisplayName("Confirmar pagamento por PIX")
    void testConfirmarPedidoPIX() {
        // Arrange

        // Act
        Pedido resultado = driver.confirmarPagamento(cliente.getId(), pedido.getId(), "PIX", cliente.getCodigoAcesso(), pedidoPostPutRequestDTO);

        // Assert
        assertEquals(true, resultado.getStatusPagamento());
        assertEquals(9.5, resultado.getPreco());

    }

    @Test
    @DisplayName("Confirmando pedido com código inválido")
    void testConfirmarPedidoCodigoInvalido() {
        // Arrange

        // Act
        CodigoDeAcessoInvalidoException thrown = assertThrows(
                CodigoDeAcessoInvalidoException.class,
                () -> driver.confirmarPagamento(cliente.getId(), pedido.getId(), "PIX", "teste", pedidoPostPutRequestDTO)
        );

        // Assert
        assertEquals("Codigo de acesso invalido!", thrown.getMessage());


    }

    @Test
    @DisplayName("Quando o cliente tenta confirmar o pagamento de um pedido com status diferente de 'Pedido recebido'")
    void testConfirmarPedidoStatusInvalido() {
        // Arrange
        pedido.setStatusEntrega("Status inválido");

        // Act
        PedidoStatusInvalidoException thrown = assertThrows(
                PedidoStatusInvalidoException.class,
                () -> driver.confirmarPagamento(cliente.getId(), pedido.getId(), "PIX", cliente.getCodigoAcesso(), pedidoPostPutRequestDTO)
        );

        // Assert
        assertEquals("O status do pedido e invalido!", thrown.getMessage());

    }

}