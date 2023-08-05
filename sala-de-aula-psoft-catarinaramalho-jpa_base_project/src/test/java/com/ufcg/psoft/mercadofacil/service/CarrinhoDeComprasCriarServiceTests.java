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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("Testes do Serviço de criação do carrinho de compras")
public class CarrinhoDeComprasCriarServiceTests {

    @Autowired
    CarrinhoDeComprasCriarService driver;

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
    @DisplayName("Quando um novo carrinho de compras nulo for fornecido")
    void quandoNovoCarrinhoDeComprasNull(){
        // Arrange
        carrinho = null;

        // Act
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> driver.criar(carrinho)
        );

        // Assert
        assertEquals("Carrinho de Compras inválido", thrown.getMessage());
    }


}
