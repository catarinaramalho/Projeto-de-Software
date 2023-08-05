package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.CarrinhoDeCompras;
import com.ufcg.psoft.mercadofacil.model.Lote;
import com.ufcg.psoft.mercadofacil.model.Cliente;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.CarrinhoDeComprasRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("Testes do Serviço de alteração do Carrinho de Compras")
public class CarrinhoDeComprasAlterarServiceTests {

    @Autowired
    CarrinhoDeComprasAlterarPadraoService driver;

    @Autowired
    CarrinhoDeComprasRepository carrinhoDeComprasRepository;

    Cliente cliente;
    Lote lote;
    CarrinhoDeCompras carrinho;
    Produto produto;

    @BeforeEach
    void setup() {
        produto = Produto.builder()
                .id(1L)
                .codigoDeBarras("7899137500100")
                .nome("Produto Dez")
                .fabricante("Empresa Dez")
                .preco(450.00)
                .build();
        lote = Lote.builder()
                .id(1L)
                .produto(produto)
                .numeroDeItens(400)
                .build();
        cliente = Cliente.builder()
                .id(1L)
                .cpf(12312312398L)
                .nome("Cliente 1")
                .idade(18)
                .endereco("Rua Das Neves")
                .build();
    }

    @AfterEach
    void tearDown() {
        carrinhoDeComprasRepository.deleteAll();
    }


    @Test
    @DisplayName("Quando um novo carrinho De Compras nulo for fornecido")
    void quandoCarrinhoNull() {
        // Arrange
        carrinho = null;

        // Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(carrinho)
        );

        // Assert
        assertEquals("Carrinho De Compras inválido", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando um novo lote nulo for fornecido")
    void quandoLoteNull() {
        // Arrange
        carrinho = CarrinhoDeCompras.builder()
                .id(1L)
                .lote(null)
                .cliente(cliente)
                .quantidade(20).
                build();

        // Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(carrinho)
        );

        // Assert
        assertEquals("Lote inválido", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando um novo cliente nulo for fornecido")
    void quandoClienteNull() {
        // Arrange
        carrinho = CarrinhoDeCompras.builder()
                .id(1L)
                .lote(lote)
                .cliente(null)
                .quantidade(20)
                .build();

        // Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(carrinho)
        );

        // Assert
        assertEquals("Cliente inválido", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando um novo id nulo for fornecido para o Carrinho")
    void quandoNovoIdNull() {
        // Arrange
        carrinho = CarrinhoDeCompras.builder()
                .id(null)
                .lote(lote)
                .cliente(cliente)
                .quantidade(70)
                .build();

        // Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(carrinho)
        );

        // Assert
        assertEquals("Id inválido", thrown.getMessage());
    }

    @Test
    @DisplayName("Quando a quantidade é menor que zero")
    void quandoQuantidadeMenorZero() {
        // Arrange
        carrinho = CarrinhoDeCompras.builder()
                .id(1L)
                .lote(lote)
                .cliente(cliente)
                .quantidade(-20)
                .build();

        // Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.alterar(carrinho)
        );

        // Assert
        assertEquals("Quantidade inválida", thrown.getMessage());
    }


}