package com.ufcg.psoft.pitsa.service.pedido;

import com.ufcg.psoft.pitsa.dto.PedidoPostPutRequestDTO;
import com.ufcg.psoft.pitsa.exception.CodigoDeAcessoInvalidoException;
import com.ufcg.psoft.pitsa.exception.EstabelecimentoNaoExisteException;
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
@DisplayName("Testes do Serviço de associação de pedidos ao entregador por parte de estabelecimento")
public class EstabelecimentoPrepararPedidoServiceTests {

    @Autowired
    EstabelecimentoPrepararPedidoService driver;
    @Autowired
    ClienteConfirmarPedidoService clienteConfirmarPedidoService;
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    EntregadorRepository entregadorRepository;
    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;
    @Autowired
    SaborRepository saborRepository;
    @Autowired
    ModelMapper modelMapper;
    PedidoPostPutRequestDTO pedidoPostPutRequestDTO;
    Entregador entregador;

    Cliente cliente;
    Sabor sabor1;
    Sabor sabor2;
    Pizza pizza1;
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
        pedidoPostPutRequestDTO = PedidoPostPutRequestDTO.builder()
                .enderecoEntrega("Apartamento 237")
                .pizzas(pizzas)
                .build();

        entregador = Entregador.builder()
                .nome("Joe Alwyn")
                .placaVeiculo("ABC-1234")
                .corVeiculo("Azul")
                .tipoVeiculo("Moto")
                .codigoAcesso("123456")
                .build();
        entregadorRepository.save(entregador);
    }

    @AfterEach
    void tearDown() {
        clienteRepository.deleteAll();
        estabelecimentoRepository.deleteAll();
        pedidoRepository.deleteAll();
        saborRepository.deleteAll();
    }

    @Test
    @DisplayName("Quando um estabelecimento prepara pedido com sucesso")
    void quandoEstabelecimentoPreparaPedidoComSucesso() {
        //Arrange
        pedido.setStatusEntrega("Pedido em preparo");

        //Act
        modelMapper.map(pedido, pedidoRepository.findById(pedido.getId()).get());
        pedidoRepository.save(pedido);
        Pedido resultado = driver.prepararPedido(pedido.getId(), estabelecimento.getId(), estabelecimento.getCodigoAcesso(), pedidoPostPutRequestDTO);

        //Assert
        assertEquals("Pedido pronto", resultado.getStatusEntrega());
    }

    @Test
    @DisplayName("Quando um estabelecimento prepara pedido com id inválido")
    void quandoEstabelecimentoPreparaPedidoComIdInvalido() throws PedidoNaoExisteException {
        //Arrange

        //Act
        PedidoNaoExisteException thrown = assertThrows(
                PedidoNaoExisteException.class,
                () -> driver.prepararPedido(0L, estabelecimento.getId(), estabelecimento.getCodigoAcesso(), pedidoPostPutRequestDTO)
        );

        assertEquals("O pedido consultado nao existe!", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando um estabelecimento prepara pedido com id do estabelecimento inválido")
    void quandoEstabelecimentoPreparaPedidoComEstabelecimentoIdInvalido() throws EstabelecimentoNaoExisteException {
        //Arrange

        //Act
        EstabelecimentoNaoExisteException thrown = assertThrows(
                EstabelecimentoNaoExisteException.class,
                () -> driver.prepararPedido(pedido.getId(), 0L, estabelecimento.getCodigoAcesso(), pedidoPostPutRequestDTO)
        );

        //Assert
        assertEquals("O estabelecimento consultado nao existe!", thrown.getMessage());

    }

    @Test
    @DisplayName("Quando um estabelecimento prepara pedido com código de acesso inválido")
    void quandoEstabelecimentoPreparaPedidoComEstabelecimentoCodigoInvalido() throws CodigoDeAcessoInvalidoException {
        //Arrange

        //Act
        CodigoDeAcessoInvalidoException thrown = assertThrows(
                CodigoDeAcessoInvalidoException.class,
                () -> driver.prepararPedido(pedido.getId(), estabelecimento.getId(), "111111", pedidoPostPutRequestDTO)
        );

        //Assert
        assertEquals("Codigo de acesso invalido!", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando Tentamos preparar um pedido que não está 'em preparo'")
    void quandoTentamosPrepararPedidoQueNaoEstaEmPreparo() throws PedidoNaoExisteException {
        //Arrange
        pedido.setStatusEntrega("valor inválido");

        //Act
        PedidoStatusInvalidoException thrown = assertThrows(
                PedidoStatusInvalidoException.class,
                () -> driver.prepararPedido(pedido.getId(), estabelecimento.getId(), estabelecimento.getCodigoAcesso(), pedidoPostPutRequestDTO)
        );

        //Assert
        assertEquals("O status do pedido e invalido!", thrown.getMessage());
    }
}
