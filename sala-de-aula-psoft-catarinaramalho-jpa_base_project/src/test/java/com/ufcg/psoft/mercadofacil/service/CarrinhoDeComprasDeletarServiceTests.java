package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.CarrinhoDeCompras;
import com.ufcg.psoft.mercadofacil.model.Cliente;
import com.ufcg.psoft.mercadofacil.model.Lote;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.CarrinhoDeComprasRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("Testes do Serviço de deletar do carrinho de compras")
public class CarrinhoDeComprasDeletarServiceTests {
    @Autowired
    CarrinhoDeComprasDeletarService driver;
    @Autowired
    CarrinhoDeComprasRepository carrinhoDeComprasRepository;

    Cliente cliente;
    Lote lote;
    Produto produto;

    CarrinhoDeCompras carrinho;

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

    @Test
    @DisplayName("Deletar carrinho de compras inexistente")
    void deletarCarrinhoDeComprasInexistente() {
        //Arrange
        carrinho = CarrinhoDeCompras.builder()
                .quantidade(1)
                .id(1L)
                .cliente(cliente)
                .lote(lote)
                .build();

        //Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.deletar(null)
        );

        // Assert
        assertEquals("Id inválido", thrown.getMessage());
    }
}