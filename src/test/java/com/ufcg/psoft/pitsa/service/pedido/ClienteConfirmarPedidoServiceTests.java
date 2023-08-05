package com.ufcg.psoft.pitsa.service.pedido;

import com.ufcg.psoft.pitsa.dto.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.pitsa.dto.PedidoPostPutRequestDTO;
import com.ufcg.psoft.pitsa.exception.ClienteNaoExisteException;
import com.ufcg.psoft.pitsa.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.pitsa.exception.PedidoNaoExisteException;
import com.ufcg.psoft.pitsa.exception.PedidoStatusInvalidoException;
import com.ufcg.psoft.pitsa.model.*;
import com.ufcg.psoft.pitsa.repository.*;
import com.ufcg.psoft.pitsa.service.entregador.EstabelecimentoAprovarEntregadorService;
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
@DisplayName("Testes do Serviço de confirmação de pedidos por parte de clientes")
public class ClienteConfirmarPedidoServiceTests {

    @Autowired
    ClienteConfirmarPedidoService driver;
    @Autowired
    EstabelecimentoAssociarPedidoEntregadorService driverAssociar;
    @Autowired
    EstabelecimentoAprovarEntregadorService driverAprovar;
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
    @Autowired
    ModelMapper modelMapper;

    Cliente cliente;
    Sabor sabor1;
    Sabor sabor2;
    Pizza pizza1;
    Estabelecimento estabelecimento;
    Pedido pedido;
    Entregador entregador;
    PedidoPostPutRequestDTO pedidoPostPutRequestDTO;
    EntregadorPostPutRequestDTO entregadorPostPutRequestDTO;

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
        entregador = entregadorRepository.save(Entregador.builder()
                .nome("Biscuit")
                .placaVeiculo("ABC-1234")
                .corVeiculo("Azul")
                .tipoVeiculo("Moto")
                .codigoAcesso("101010")
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
        pedidoPostPutRequestDTO = PedidoPostPutRequestDTO.builder()
                .enderecoEntrega("Apartamento 237")
                .pizzas(pizzas)
                .build();
        entregadorPostPutRequestDTO = EntregadorPostPutRequestDTO.builder()
                .nome("Biscuit")
                .placaVeiculo("ABC-1234")
                .corVeiculo("Azul")
                .tipoVeiculo("Moto")
                .codigoAcesso("101010")
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
    @DisplayName("Quando um cliente confirma um pedido que ele salvou pelo id")
    void testConfirmarPedido() {
        //Arrange
        pedido.setStatusEntrega("Pedido pronto");
        driverAprovar.aprovar(entregador.getId(), entregadorPostPutRequestDTO, estabelecimento.getCodigoAcesso(), estabelecimento.getId());
        entregador.setDisponibilidade(true);
        driverAssociar.associar(pedido.getId(), estabelecimento.getId(), estabelecimento.getCodigoAcesso(), pedidoPostPutRequestDTO);

        //Act
        modelMapper.map(entregador, entregadorRepository.findById(entregador.getId()).get());
        entregadorRepository.save(entregador);
        Pedido pedidoConfirmado = driver.confirmar(pedido.getId(), cliente.getId(), cliente.getCodigoAcesso(), pedidoPostPutRequestDTO);

        //Assert
        assertEquals("Pedido entregue", pedidoConfirmado.getStatusEntrega());
    }

    @Test
    @DisplayName("Quando um cliente confirma um pedido que ele salvou pelo id, mas o id do cliente está errado")
    void testConfirmarPedidoIdClienteErrado() {
        //Arrange

        //Act
        ClienteNaoExisteException thrown = assertThrows(
                ClienteNaoExisteException.class,
                () -> driver.confirmar(pedido.getId(), 0L, cliente.getCodigoAcesso(), pedidoPostPutRequestDTO)
        );

        //Assert
        assertEquals("O cliente consultado nao existe!", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando um cliente confirma um pedido que ele salvou pelo id, mas o código de acesso está errado")
    void testConfirmarPedidoCodigoDeAcessoErrado() {
        //Arrange
        pedido.setStatusEntrega("Pedido em rota");
        //Act
        CodigoDeAcessoInvalidoException thrown = assertThrows(
                CodigoDeAcessoInvalidoException.class,
                () -> driver.confirmar(pedido.getId(), cliente.getId(), "000000", pedidoPostPutRequestDTO)
        );

        //Assert
        assertEquals("Codigo de acesso invalido!", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando um cliente confirma um pedido que ele salvou pelo id, mas o id do pedido está errado")
    void testConfirmarPedidoIdPedidoErrado() {
        //Arrange

        //Act
        PedidoNaoExisteException thrown = assertThrows(
                PedidoNaoExisteException.class,
                () -> driver.confirmar(0L, cliente.getId(), cliente.getCodigoAcesso(), pedidoPostPutRequestDTO)
        );

        assertEquals("O pedido consultado nao existe!", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando tentamos confirmar um pedido que não está com o status 'Pedido recebido'")
    void testConfirmarPedidoStatusErrado() {
        //Arrange
        pedido.setStatusEntrega("valor inválido");

        //Act
        PedidoStatusInvalidoException thrown = assertThrows(
                PedidoStatusInvalidoException.class,
                () -> driver.confirmar(pedido.getId(), cliente.getId(), cliente.getCodigoAcesso(), pedidoPostPutRequestDTO)
        );

        //Assert
        assertEquals("O status do pedido e invalido!", thrown.getMessage());
    }
}
