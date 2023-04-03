package com.ufcg.psoft.mercadofacil.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.utils.TestUtils;

@SpringBootTest
@DisplayName("Testes do repositório de Produtos")
public class ProdutoRepositoryTests {
    @Autowired
    ProdutoRepository driver;
    Produto produto;

    @BeforeEach
    void setup(){
        produto = TestUtils.criarProduto(10L, "Produto Dez", "Fabricante Dez","1234567890123", 100.00);
    }
    @Testes
    @DisplayName("Quando ciar um novo produto com dados válidos")
    void testQUandoCRiarPoduto(){
        //Arrange
        //Act
        Poduto resultado =drive.save(produto);
        //Assert
        assertNOtNUll(resultado);
        assertEquals("Produto Dez", resultado.getNome());
        assertEquals("Fabricante Dez",resultado.getFabricante());
        assertEquals("1234567890123",resultado.getCodigoBarra());
        assertEquals(100.00,resultado.getPreco())
    }
}
