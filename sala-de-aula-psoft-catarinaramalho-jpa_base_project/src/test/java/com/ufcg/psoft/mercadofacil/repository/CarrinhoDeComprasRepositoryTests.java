package com.ufcg.psoft.mercadofacil.repository;

import com.ufcg.psoft.mercadofacil.model.CarrinhoDeCompras;
import com.ufcg.psoft.mercadofacil.model.Cliente;
import com.ufcg.psoft.mercadofacil.model.Lote;
import com.ufcg.psoft.mercadofacil.model.Produto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DisplayName("Testes do repositório de Carrinhos de Compras")
public class CarrinhoDeComprasRepositoryTests {

    @Autowired
    CarrinhoDeComprasRepository driver;

    CarrinhoDeCompras carrinhoDeCompras;

    Cliente cliente;

    Produto produto;

    Lote lote;

    @BeforeEach
    void setup() {
        cliente = Cliente.builder()
                .cpf(12345678910L)
                .nome("Cliente Teste")
                .idade(25)
                .endereco("Rua do Teste, 123")
                .build();
        produto = Produto.builder()
                .codigoDeBarras("7899137500100")
                .nome("Produto Dez")
                .fabricante("Empresa Dez")
                .preco(450.00)
                .build();
        lote = Lote.builder()
                .produto(produto)
                .numeroDeItens(100)
                .build();
        carrinhoDeCompras = CarrinhoDeCompras.builder()
                .cliente(cliente)
                .lote(lote)
                .quantidade(10)
                .build();
    }

    @AfterEach
    void tearDown() {
        driver.deleteAll();
    }

    @Test
    @DisplayName("Inserir o primeiro carrinho de compras no banco de dados")
    void inserirPrimeiroCarrinhoDeComprar() {
        // Arrange
        driver.deleteAll();

        // Act
        CarrinhoDeCompras resultado = driver.save(carrinhoDeCompras);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, driver.findAll().size());
        assertEquals(carrinhoDeCompras, resultado);
    }

    @Test
    @DisplayName("Inserir o segundo ou posterior carrinho de compras no banco de dados")
    void inserirSegundoOuPosteriorCarrinhoDeCompras() {
        // Arrange
        driver.deleteAll();
        Cliente cliente2 = Cliente.builder()
                .cpf(12345678920L)
                .nome("Cliente Teste 2")
                .idade(20)
                .endereco("Rua do Teste, 123")
                .build();
        Produto produto2 = Produto.builder()
                .codigoDeBarras("7899137500209")
                .nome("Produto Vinte")
                .fabricante("Empresa Vinte")
                .preco(600.00)
                .build();
        Lote lote = Lote.builder()
                .produto(produto2)
                .numeroDeItens(200)
                .build();
        CarrinhoDeCompras carrinhoDeCompras2 = CarrinhoDeCompras.builder()
                .cliente(cliente2)
                .lote(lote)
                .quantidade(10)
                .build();
        driver.save(carrinhoDeCompras);

        // Act
        CarrinhoDeCompras resultado = driver.save(carrinhoDeCompras2);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, driver.findAll().size());
        assertEquals(carrinhoDeCompras2, resultado);
    }

    @Test
    @DisplayName("Deletar o primeiro carrinho de compras no banco de dados")
    void deletarPrimeiroCarrinhoDeComprar() {
        // Arrange
        driver.deleteAll();

        // Act
        driver.delete(carrinhoDeCompras);

        // Assert
        assertEquals(0, driver.findAll().size());
    }

    @Test
    @DisplayName("Deletar o segundo ou posterior carrinho de compras no banco de dados")
    void deletarSegundoOuPosteriorCarrinhoDeCompras() {
        // Arrange
        driver.deleteAll();
        Cliente cliente2 = Cliente.builder()
                .cpf(12345678920L)
                .nome("Cliente Teste 2")
                .idade(20)
                .endereco("Rua do Teste, 123")
                .build();
        Produto produto2 = Produto.builder()
                .codigoDeBarras("7899137500209")
                .nome("Produto Vinte")
                .fabricante("Empresa Vinte")
                .preco(600.00)
                .build();
        Lote lote = Lote.builder()
                .produto(produto2)
                .numeroDeItens(200)
                .build();
        CarrinhoDeCompras carrinhoDeCompras2 = CarrinhoDeCompras.builder()
                .cliente(cliente2)
                .lote(lote)
                .quantidade(10)
                .build();
        driver.save(carrinhoDeCompras);

        // Act
        driver.delete(carrinhoDeCompras2);

        // Assert
        assertEquals(1, driver.findAll().size());
    }
}