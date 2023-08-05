package com.ufcg.psoft.pitsa.service.pedido;

import com.ufcg.psoft.pitsa.dto.PedidoPostPutRequestDTO;
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
@DisplayName("Testes do Serviço de alteração de pedido")
public class PedidoAlterarServiceTests {

    @Autowired
    PedidoAlterarService driver;
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
        pizzaM = pizzaRepository.save(Pizza.builder()
                .sabor1(sabor1)
                .tamanho("media")
                .build());
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
    @DisplayName("Quando alteramos pedido com dados válidos")
    void quandoAlteramosPedidoComDadosValidos() {
        // Arrange
        Pizza pizzaG = Pizza.builder()
                .sabor1(sabor1)
                .sabor2(sabor2)
                .tamanho("grande")
                .build();
        List<Pizza> pizzasMG = List.of(pizzaM, pizzaG);
        pedidoPostPutRequestDTO.setEnderecoEntrega("Apartamento 237");
        pedidoPostPutRequestDTO.setPizzas(pizzasMG);

        // Act
        Pedido resultado = driver.alterar(pedido.getId(), cliente.getCodigoAcesso(), pedidoPostPutRequestDTO);

        // Assert
        assertAll(
                () -> assertEquals(1, pedidoRepository.count()),
                () -> assertEquals(pedido.getId(), resultado.getId()),
                () -> assertEquals(pedidoPostPutRequestDTO.getEnderecoEntrega(), resultado.getEnderecoEntrega()),
                () -> assertEquals(pedidoPostPutRequestDTO.getPizzas().get(0).getSabor1(), resultado.getPizzas().get(0).getSabor1()),
                () -> assertEquals(pedidoPostPutRequestDTO.getPizzas().get(0).getSabor2(), resultado.getPizzas().get(0).getSabor2()),
                () -> assertEquals(pedidoPostPutRequestDTO.getPizzas().get(0).getTamanho(), resultado.getPizzas().get(0).getTamanho()),
                () -> assertEquals(pedidoPostPutRequestDTO.getPizzas().get(1).getSabor1(), resultado.getPizzas().get(1).getSabor1()),
                () -> assertEquals(pedidoPostPutRequestDTO.getPizzas().get(1).getSabor2(), resultado.getPizzas().get(1).getSabor2()),
                () -> assertEquals(pedidoPostPutRequestDTO.getPizzas().get(1).getTamanho(), resultado.getPizzas().get(1).getTamanho())
        );
    }

    @Test
    @DisplayName("Quando alteramos pedido com id inexistente")
    void quandoAlteramosPedidoComIdInexistente() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        PedidoNaoExisteException thrown = assertThrows(
                PedidoNaoExisteException.class,
                () -> driver.alterar(2L, cliente.getCodigoAcesso(), pedidoPostPutRequestDTO)
        );


        // Assert
        assertAll(
                () -> assertEquals("O pedido consultado nao existe!", thrown.getMessage()),
                () -> assertEquals(1, pedidoRepository.count())
        );
    }

    @Test
    @DisplayName("Quando alteramos pedido com código de acesso inválido")
    void quandoAlteramosPedidoComCodigoDeAcessoInvalido() {
        // Arrange
        String codigoAcesso = "787878";

        // Act
        CodigoDeAcessoInvalidoException thrown = assertThrows(
                CodigoDeAcessoInvalidoException.class,
                () -> driver.alterar(pedido.getId(), codigoAcesso, pedidoPostPutRequestDTO)
        );

        // Assert
        assertAll(
                () -> assertEquals("Codigo de acesso invalido!", thrown.getMessage()),
                () -> assertEquals(1, pedidoRepository.count())
        );
    }
}