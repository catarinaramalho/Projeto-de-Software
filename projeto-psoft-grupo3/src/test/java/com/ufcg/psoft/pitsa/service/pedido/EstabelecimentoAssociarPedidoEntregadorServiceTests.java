package com.ufcg.psoft.pitsa.service.pedido;

import com.ufcg.psoft.pitsa.dto.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.pitsa.dto.PedidoPostPutRequestDTO;
import com.ufcg.psoft.pitsa.exception.*;
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
@DisplayName("Testes do Serviço de associação de pedidos ao entregador por parte de estabelecimento")
public class EstabelecimentoAssociarPedidoEntregadorServiceTests {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    EstabelecimentoAssociarPedidoEntregadorService driver;
    @Autowired
    EstabelecimentoAprovarEntregadorService driverAprovar;
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
    PedidoPostPutRequestDTO pedidoPostPutRequestDTO;
    EntregadorPostPutRequestDTO entregadorPostPutRequestDTO;
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
        entregadorPostPutRequestDTO = EntregadorPostPutRequestDTO.builder()
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
    @DisplayName("Quando um estabelecimento associa um pedido ao entregador")
    void testEstabelecimentoAssociarPedidoAoEntregador() throws PedidoNaoExisteException, CodigoDeAcessoInvalidoException, EstabelecimentoNaoExisteException {
        //Arrange
        pedido.setStatusEntrega("Pedido pronto");
        driverAprovar.aprovar(entregador.getId(), entregadorPostPutRequestDTO, estabelecimento.getCodigoAcesso(), estabelecimento.getId());
        entregador.setDisponibilidade(true);

        //Act
        modelMapper.map(entregador, entregadorRepository.findById(entregador.getId()).get());
        entregadorRepository.save(entregador);
        Pedido resultado = driver.associar(pedido.getId(), estabelecimento.getId(), estabelecimento.getCodigoAcesso(), pedidoPostPutRequestDTO);

        //Assert
        assertEquals("Apartamento 237", resultado.getEnderecoEntrega());
    }

    @Test
    @DisplayName("Quando um estabelecimento associa um pedido ao entregador com código de acesso inválido")
    void testEstabelecimentoAssociarPedidoAoEntregadorSemCodigoAcesso() throws PedidoNaoExisteException, CodigoDeAcessoInvalidoException, EstabelecimentoNaoExisteException {
        //Arrange

        //Act
        CodigoDeAcessoInvalidoException thrown = assertThrows(
                CodigoDeAcessoInvalidoException.class,
                () -> driver.associar(pedido.getId(), estabelecimento.getId(), "123400", pedidoPostPutRequestDTO)
        );

        //Assert
        assertEquals("Codigo de acesso invalido!", thrown.getMessage());

    }

    @Test
    @DisplayName("Quando um estabelecimento associa um pedido ao entregador com estabelecimento inválido")
    void testEstabelecimentoAssociarPedidoAoEntregadorEstabelecimentoInvalido() throws PedidoNaoExisteException, CodigoDeAcessoInvalidoException, EstabelecimentoNaoExisteException {
        //Arrange

        //Act
        EstabelecimentoNaoExisteException thrown = assertThrows(
                EstabelecimentoNaoExisteException.class,
                () -> driver.associar(pedido.getId(), 900L, estabelecimento.getCodigoAcesso(), pedidoPostPutRequestDTO)
        );

        assertEquals("O estabelecimento consultado nao existe!", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando um estabelecimento associa um pedido ao entregador com id do Pedido inválido")
    void testEstabelecimentoAssociarPedidoAoEntregadorPedidoInvalido() throws PedidoNaoExisteException, CodigoDeAcessoInvalidoException, EstabelecimentoNaoExisteException {
        //Arrange

        //Act
        PedidoNaoExisteException thrown = assertThrows(
                PedidoNaoExisteException.class,
                () -> driver.associar(0L, estabelecimento.getId(), estabelecimento.getCodigoAcesso(), pedidoPostPutRequestDTO)
        );

        //Assert
        assertEquals("O pedido consultado nao existe!", thrown.getMessage());
    }

    /**@Test
    @DisplayName("Quando um estabelecimento associa um pedido ao entregador com id do Entregador inválido")
    void testEstabelecimentoAssociarPedidoAoEntregadorEntregadorInvalido() {
        //Arrange
        pedido.setStatusEntrega("Pedido pronto");
        driverAprovar.aprovar(entregador.getId(), entregadorPostPutRequestDTO, estabelecimento.getCodigoAcesso(), estabelecimento.getId());

        //Act
        EntregadorNaoExisteException thrown = assertThrows(
                EntregadorNaoExisteException.class,
                () -> driver.associar(pedido.getId(), estabelecimento.getId(), estabelecimento.getCodigoAcesso(), pedidoPostPutRequestDTO)
        );

        //Assert
        assertEquals("O entregador consultado nao existe!", thrown.getMessage());
    } */

    @Test
    @DisplayName("Quando tentamos associar um pedido com status de entrega diferente de 'Pedido pronto'")
    void testEstabelecimentoAssociarPedidoAoEntregadorComStatusInvalido() throws PedidoNaoExisteException, CodigoDeAcessoInvalidoException, EstabelecimentoNaoExisteException {
        //Arrange
        pedido.setStatusEntrega("valor inválido");

        //Act
        PedidoStatusInvalidoException thrown = assertThrows(
                PedidoStatusInvalidoException.class,
                () -> driver.associar(pedido.getId(), estabelecimento.getId(), estabelecimento.getCodigoAcesso(), pedidoPostPutRequestDTO)
        );

        //Assert
        assertEquals("O status do pedido e invalido!", thrown.getMessage());
    }

    /**@Test
    @DisplayName("Quando tentamos associar um pedido com o entregador indisponível")
    void testEstabelecimentoAssociarPedidoAoEntregadorComEntregadorIndisponivel() throws PedidoNaoExisteException, CodigoDeAcessoInvalidoException, EstabelecimentoNaoExisteException {
        //Arrange
        pedido.setStatusEntrega("Pedido pronto");

        //Act
        EntregadorNaoDisponivelException thrown = assertThrows(
                EntregadorNaoDisponivelException.class,
                () -> driver.associar(pedido.getId(), estabelecimento.getId(), estabelecimento.getCodigoAcesso(), pedidoPostPutRequestDTO)
        );

        entregador.setDisponibilidade(false);
        modelMapper.map(entregador, entregadorRepository.findById(entregador.getId()).get());
        entregadorRepository.save(entregador);

        //Assert
        assertEquals("Entregador não disponível!", thrown.getMessage());
    }*/
}
