package com.ufcg.psoft.pitsa.service.entregador;

import com.ufcg.psoft.pitsa.dto.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.pitsa.dto.PedidoPostPutRequestDTO;
import com.ufcg.psoft.pitsa.exception.*;
import com.ufcg.psoft.pitsa.model.*;
import com.ufcg.psoft.pitsa.repository.*;
import com.ufcg.psoft.pitsa.service.pedido.EstabelecimentoAssociarPedidoEntregadorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("Testes do Serviço de associação de pedidos ao entregador por parte de estabelecimento")
public class EstabelecimentoAprovarEntregadorTests {
    @Autowired
    ModelMapper modelMapper;
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

    @Autowired
    EstabelecimentoAprovarEntregadorPadraoService driver;
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
        entregadorPostPutRequestDTO = EntregadorPostPutRequestDTO.builder()
                .nome("Joe Alwyn")
                .placaVeiculo("ABC-1234")
                .corVeiculo("Azul")
                .tipoVeiculo("Moto")
                .codigoAcesso("123456")
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
    @DisplayName("Alterando o status de disponibilidade do entregador")
    void testAlterarDisponibilidade() {
        //Arrange

        //Act
        driver.aprovar(entregador.getId(), entregadorPostPutRequestDTO, estabelecimento.getCodigoAcesso(), estabelecimento.getId());

        //Assert
        assertTrue(entregador.isStatusAprovacao());
    }

    @Test
    @DisplayName("Alterando o status de disponibilidade do entregador com codigo de acesso errado")
    void testAlterarDisponibilidadeCodigoAcessoErrado() {
        //Arrange

        //Act
        CodigoDeAcessoInvalidoException thrown = assertThrows(
                CodigoDeAcessoInvalidoException.class,
                () -> driver.aprovar(entregador.getId(), entregadorPostPutRequestDTO, "Gaga flopada sem hit solo desde 2011", estabelecimento.getId())
        );

        //Assert
        assertEquals("Codigo de acesso invalido!", thrown.getMessage());
    }

    @Test
    @DisplayName("Alterando o status de disponibilidade do entregador com id inexistente")
    void testAlterarDisponibilidadeIdInexistente() {
        //Arrange

        //Act
        EntregadorNaoExisteException thrown = assertThrows(
                EntregadorNaoExisteException.class,
                () -> driver.aprovar(666L, entregadorPostPutRequestDTO, estabelecimento.getCodigoAcesso(), estabelecimento.getId())
        );

        //Assert
        assertEquals("O entregador consultado nao existe!", thrown.getMessage());
    }

    @Test
    @DisplayName("Alterando o status de disponibilidade do entregador com estabelecimento inexistente")
    void testAlterarDisponibilidadeEstabelecimentoInexistente() {
        //Arrange

        //Act
        EstabelecimentoNaoExisteException thrown = assertThrows(
                EstabelecimentoNaoExisteException.class,
                () -> driver.aprovar(entregador.getId(), entregadorPostPutRequestDTO, estabelecimento.getCodigoAcesso(), 666L)
        );

        //Assert
        assertEquals("O estabelecimento consultado nao existe!", thrown.getMessage());
    }
}
