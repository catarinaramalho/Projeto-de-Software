package com.ufcg.psoft.pitsa.service.pedido;

import com.ufcg.psoft.pitsa.dto.PedidoPostPutRequestDTO;
import com.ufcg.psoft.pitsa.exception.*;
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
@DisplayName("Testes do Serviço de criação de pedido")
class PedidoCriarServiceTests {

    @Autowired
    PedidoCriarService driver;

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

        pedido = Pedido.builder()
                .preco(10.0)
                .enderecoEntrega("Casa 237")
                .clienteId(cliente.getId())
                .estabelecimentoId(estabelecimento.getId())
                .pizzas(pizzasM)
                .build();
        pedidoPostPutRequestDTO = PedidoPostPutRequestDTO.builder()
                .enderecoEntrega("Apartamento 237")
                .pizzas(pizzasM)
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
    @DisplayName("Criar primeiro pedido com sucesso (pizza media)")
    void testCriarPedidoComSucesso() {
        // Arrange
        // Nenhuma necessidade além do setup()

        // Act
        Pedido resultado = driver.salvar(cliente.getId(), cliente.getCodigoAcesso(), estabelecimento.getId(), pedidoPostPutRequestDTO);

        // Assert
        assertAll(
                () -> assertEquals(1, pedidoRepository.count()),
                () -> assertEquals(pedido.getClienteId(), resultado.getClienteId()),
                () -> assertEquals(pedido.getEstabelecimentoId(), resultado.getEstabelecimentoId()),
                () -> assertEquals("Apartamento 237", resultado.getEnderecoEntrega()),
                () -> assertEquals(pedido.getPreco(), resultado.getPreco()),
                () -> assertEquals(pedido.getPizzas().get(0).getSabor1().getNome(), resultado.getPizzas().get(0).getSabor1().getNome()),
                () -> assertEquals(pedido.getPizzas().get(0).getSabor2(), resultado.getPizzas().get(0).getSabor2()),
                () -> assertEquals(pedido.getPizzas().get(0).getTamanho(), resultado.getPizzas().get(0).getTamanho()),
                () -> assertEquals(resultado.getStatusEntrega(), "Pedido recebido")
        );
    }

    @Test
    @DisplayName("Criar primeiro pedido com sucesso (pizza grande)")
    void testCriarPedidoComSucessoPizzaGrande() {
        // Arrange
        Pedido pedidoGrande = Pedido.builder()
                .preco(25.0)
                .enderecoEntrega("Casa 237")
                .clienteId(cliente.getId())
                .estabelecimentoId(estabelecimento.getId())
                .pizzas(pizzasG)
                .build();
        PedidoPostPutRequestDTO pedidoGrandePostPutRequestDTO = PedidoPostPutRequestDTO.builder()
                .enderecoEntrega("Casa 237")
                .pizzas(pizzasG)
                .build();

        // Act
        Pedido resultado = driver.salvar(cliente.getId(), cliente.getCodigoAcesso(), estabelecimento.getId(), pedidoGrandePostPutRequestDTO);

        // Assert
        assertAll(
                () -> assertEquals(1, pedidoRepository.count()),
                () -> assertEquals(pedidoGrande.getClienteId(), resultado.getClienteId()),
                () -> assertEquals(pedidoGrande.getEstabelecimentoId(), resultado.getEstabelecimentoId()),
                () -> assertEquals("Casa 237", resultado.getEnderecoEntrega()),
                () -> assertEquals(pedidoGrande.getPreco(), resultado.getPreco()),
                () -> assertEquals(pedidoGrande.getPizzas().get(0).getSabor1().getNome(), resultado.getPizzas().get(0).getSabor1().getNome()),
                () -> assertEquals(pedidoGrande.getPizzas().get(0).getSabor2().getNome(), resultado.getPizzas().get(0).getSabor2().getNome()),
                () -> assertEquals(pedidoGrande.getPizzas().get(0).getTamanho(), resultado.getPizzas().get(0).getTamanho()),
                () -> assertEquals(resultado.getStatusEntrega(), "Pedido recebido")
        );
    }

    @Test
    @DisplayName("Criar primeiro pedido com sucesso (pizza grande) sem enedereço de entrega")
    void testCriarPedidoComSucessoPizzaGrandeSemEnderecoEntrega() {
        // Arrange
        Pedido pedidoGrande = Pedido.builder()
                .preco(25.0)
                .enderecoEntrega(null)
                .clienteId(cliente.getId())
                .estabelecimentoId(estabelecimento.getId())
                .pizzas(pizzasG)
                .build();
        PedidoPostPutRequestDTO pedidoGrandePostPutRequestDTO = PedidoPostPutRequestDTO.builder()
                .enderecoEntrega(null)
                .pizzas(pizzasG)
                .build();

        // Act
        Pedido resultado = driver.salvar(cliente.getId(), cliente.getCodigoAcesso(), estabelecimento.getId(), pedidoGrandePostPutRequestDTO);

        // Assert
        assertAll(
                () -> assertEquals(1, pedidoRepository.count()),
                () -> assertEquals(pedidoGrande.getClienteId(), resultado.getClienteId()),
                () -> assertEquals(pedidoGrande.getEstabelecimentoId(), resultado.getEstabelecimentoId()),
                () -> assertEquals(cliente.getEndereco(), resultado.getEnderecoEntrega()),
                () -> assertEquals(pedidoGrande.getPreco(), resultado.getPreco()),
                () -> assertEquals(pedidoGrande.getPizzas().get(0).getSabor1().getNome(), resultado.getPizzas().get(0).getSabor1().getNome()),
                () -> assertEquals(pedidoGrande.getPizzas().get(0).getSabor2().getNome(), resultado.getPizzas().get(0).getSabor2().getNome()),
                () -> assertEquals(pedidoGrande.getPizzas().get(0).getTamanho(), resultado.getPizzas().get(0).getTamanho()),
                () -> assertEquals(resultado.getStatusEntrega(), "Pedido recebido")
        );
    }

    @Test
    @DisplayName("Criar primeiro pedido com sucesso (pizza grande) sem sabor 2")
    void testCriarPedidoComSucessoPizzaGrandeSemSabor2() {
        // Arrange
        pizzaGrande = pizzaRepository.save(Pizza.builder()
                .sabor1(sabor1)
                .sabor2(null)
                .tamanho("grande")
                .build());
        // Lista com uma pizza grande
        pizzasG = List.of(pizzaGrande);
        Pedido pedidoGrande = Pedido.builder()
                .preco(20.0)
                .enderecoEntrega("Casa 237")
                .clienteId(cliente.getId())
                .estabelecimentoId(estabelecimento.getId())
                .pizzas(pizzasG)
                .build();
        PedidoPostPutRequestDTO pedidoGrandePostPutRequestDTO = PedidoPostPutRequestDTO.builder()
                .enderecoEntrega("Casa 237")
                .pizzas(pizzasG)
                .build();

        // Act
        Pedido resultado = driver.salvar(cliente.getId(), cliente.getCodigoAcesso(), estabelecimento.getId(), pedidoGrandePostPutRequestDTO);

        // Assert
        assertAll(
                () -> assertEquals(1, pedidoRepository.count()),
                () -> assertEquals(pedidoGrande.getClienteId(), resultado.getClienteId()),
                () -> assertEquals(pedidoGrande.getEstabelecimentoId(), resultado.getEstabelecimentoId()),
                () -> assertEquals("Casa 237", resultado.getEnderecoEntrega()),
                () -> assertEquals(pedidoGrande.getPreco(), resultado.getPreco()),
                () -> assertEquals(pedidoGrande.getPizzas().get(0).getSabor1().getNome(), resultado.getPizzas().get(0).getSabor1().getNome()),
                () -> assertEquals(pedidoGrande.getPizzas().get(0).getSabor2(), resultado.getPizzas().get(0).getSabor2()),
                () -> assertEquals(pedidoGrande.getPizzas().get(0).getTamanho(), resultado.getPizzas().get(0).getTamanho()),
                () -> assertEquals(resultado.getStatusEntrega(), "Pedido recebido")
        );
    }

    @Test
    @DisplayName("Criar primeiro pedido com sucesso (pizza media e grande)")
    void testCriarPedidoComSucessoPizzaMediaGrande() {
        // Arrange
        Pedido pedidoMediaGrande = Pedido.builder()
                .preco(35.0)
                .enderecoEntrega("Casa 237")
                .clienteId(cliente.getId())
                .estabelecimentoId(estabelecimento.getId())
                .pizzas(pizzasMG)
                .build();
        PedidoPostPutRequestDTO pedidoMediaGrandePostPutRequestDTO = PedidoPostPutRequestDTO.builder()
                .enderecoEntrega("Casa 237")
                .pizzas(pizzasMG)
                .build();

        // Act
        Pedido resultado = driver.salvar(cliente.getId(), cliente.getCodigoAcesso(), estabelecimento.getId(), pedidoMediaGrandePostPutRequestDTO);

        // Assert
        assertAll(
                () -> assertEquals(1, pedidoRepository.count()),
                () -> assertEquals(pedidoMediaGrande.getClienteId(), resultado.getClienteId()),
                () -> assertEquals(pedidoMediaGrande.getEstabelecimentoId(), resultado.getEstabelecimentoId()),
                () -> assertEquals("Casa 237", resultado.getEnderecoEntrega()),
                () -> assertEquals(pedidoMediaGrande.getPreco(), resultado.getPreco()),
                () -> assertEquals(pedidoMediaGrande.getPizzas().get(0).getSabor1().getNome(), resultado.getPizzas().get(0).getSabor1().getNome()),
                () -> assertEquals(pedidoMediaGrande.getPizzas().get(0).getSabor2(), resultado.getPizzas().get(0).getSabor2()),
                () -> assertEquals(pedidoMediaGrande.getPizzas().get(0).getTamanho(), resultado.getPizzas().get(0).getTamanho()),
                () -> assertEquals(pedidoMediaGrande.getPizzas().get(1).getSabor1().getNome(), resultado.getPizzas().get(1).getSabor1().getNome()),
                () -> assertEquals(pedidoMediaGrande.getPizzas().get(1).getSabor2().getNome(), resultado.getPizzas().get(1).getSabor2().getNome()),
                () -> assertEquals(pedidoMediaGrande.getPizzas().get(1).getTamanho(), resultado.getPizzas().get(1).getTamanho()),
                () -> assertEquals(resultado.getStatusEntrega(), "Pedido recebido")
        );
    }

    @Test
    @DisplayName("Quando criamos um pedido passando codigo de acesso invalido")
    void quandoCriamosUmPedidoPassandoCodigoDeAcessoInvalido() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        CodigoDeAcessoInvalidoException thrown = assertThrows(
                CodigoDeAcessoInvalidoException.class,
                () -> driver.salvar(cliente.getId(), "999999", estabelecimento.getId(), pedidoPostPutRequestDTO)
        );

        // Assert
        assertAll(
                () -> assertEquals(0, pedidoRepository.count()),
                () -> assertEquals("Codigo de acesso invalido!", thrown.getMessage())
        );
    }

    @Test
    @DisplayName("Quando criamos um pedido passando id de cliente invalido")
    void quandoCriamosUmPedidoPassandoIdDeClienteInvalido() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        ClienteNaoExisteException thrown = assertThrows(
                ClienteNaoExisteException.class,
                () -> driver.salvar(999999L, cliente.getCodigoAcesso(), estabelecimento.getId(), pedidoPostPutRequestDTO)
        );

        // Assert
        assertAll(
                () -> assertEquals(0, pedidoRepository.count()),
                () -> assertEquals("O cliente consultado nao existe!", thrown.getMessage())
        );
    }

    @Test
    @DisplayName("Quando criamos um pedido com sabor indisponível")
    void quandoCriamosUmPedidoComSaborIndisponivel() {
        // Arrange
        sabor1 = saborRepository.save(Sabor.builder()
                .nome("Sabor Um")
                .tipo("salgado")
                .precoM(10.0)
                .precoG(20.0)
                .disponivel(false)
                .build());
        sabor2 = saborRepository.save(Sabor.builder()
                .nome("Sabor Dois")
                .tipo("doce")
                .precoM(10.0)
                .precoG(30.0)
                .disponivel(true)
                .build());
        Pizza pizzaGrande2 = Pizza.builder()
                .sabor1(sabor1)
                .sabor2(sabor2)
                .tamanho("grande")
                .build();
        PedidoPostPutRequestDTO pedidoGrandePostPutRequestDTO = PedidoPostPutRequestDTO.builder()
                .enderecoEntrega("Casa 237")
                .pizzas(List.of(pizzaGrande2))
                .build();

        // Act
        SaborNaoEstaDisponivelException thrown = assertThrows(
                SaborNaoEstaDisponivelException.class,
                () -> driver.salvar(cliente.getId(), cliente.getCodigoAcesso(), estabelecimento.getId(), pedidoGrandePostPutRequestDTO)
        );

        // Assert
        assertAll(
                () -> assertEquals(0, pedidoRepository.count()),
                () -> assertEquals("O sabor consultado nao esta disponivel!", thrown.getMessage())
        );
    }

    @Test
    @DisplayName("Quando criamos um pedido com sabor indisponível")
    void quandoCriamosUmPedidoComSabor2Indisponivel() {
        // Arrange
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
                .disponivel(false)
                .build());
        Pizza pizzaGrande2 = Pizza.builder()
                .sabor1(sabor1)
                .sabor2(sabor2)
                .tamanho("grande")
                .build();
        PedidoPostPutRequestDTO pedidoGrandePostPutRequestDTO = PedidoPostPutRequestDTO.builder()
                .enderecoEntrega("Casa 237")
                .pizzas(List.of(pizzaGrande2))
                .build();

        // Act
        SaborNaoEstaDisponivelException thrown = assertThrows(
                SaborNaoEstaDisponivelException.class,
                () -> driver.salvar(cliente.getId(), cliente.getCodigoAcesso(), estabelecimento.getId(), pedidoGrandePostPutRequestDTO)
        );

        // Assert
        assertAll(
                () -> assertEquals(0, pedidoRepository.count()),
                () -> assertEquals("O sabor consultado nao esta disponivel!", thrown.getMessage())
        );
    }

    @Test
    @DisplayName("Quando criamos um pedido passando id de estabelecimento invalido")
    void quandoCriamosUmPedidoPassandoIdDeEstabelecimentoInvalido() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        EstabelecimentoNaoExisteException thrown = assertThrows(
                EstabelecimentoNaoExisteException.class,
                () -> driver.salvar(cliente.getId(), cliente.getCodigoAcesso(), 999999L, pedidoPostPutRequestDTO)
        );

        // Assert
        assertAll(
                () -> assertEquals(0, pedidoRepository.count()),
                () -> assertEquals("O estabelecimento consultado nao existe!", thrown.getMessage())
        );
    }

    @Test
    @DisplayName("Quando criamos um pedido de pizza média passando quantidade de sabores invalida")
    void quandoCriamosUmPedidoPizzaMediaComQuantidadeDeSaboresInvalida() {
        // Arrange
        Pizza pizzaMedia2 = Pizza.builder()
                .sabor1(sabor1)
                .sabor2(sabor2)
                .tamanho("media")
                .build();
        pizzasM = List.of(pizzaMedia2);
        PedidoPostPutRequestDTO pedidoPostPutRequestDTO = PedidoPostPutRequestDTO.builder()
                .enderecoEntrega("Casa 237")
                .pizzas(pizzasM)
                .build();

        // Act
        QuantidadeDeSaboresInvalidaException thrown = assertThrows(
                QuantidadeDeSaboresInvalidaException.class,
                () -> driver.salvar(cliente.getId(), cliente.getCodigoAcesso(), estabelecimento.getId(), pedidoPostPutRequestDTO)
        );

        // Assert
        assertAll(
                () -> assertEquals(0, pedidoRepository.count()),
                () -> assertEquals("Quantidade de sabores invalida, pizzas medias so podem ter 1 sabor!", thrown.getMessage())
        );
    }
}
