package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.ProdutoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DisplayName("Testes do Serviço de listagem do produto")
public class ProdutoListarServiceTests {

    @Autowired
    ProdutoListarService driver;

    @Autowired
    ProdutoRepository produtoRepository;

    Produto produto;

    @BeforeEach
    void setup() {
        produto = produtoRepository.save(Produto.builder()
                .codigoDeBarras("7899137500100")
                .nome("Produto Dez")
                .fabricante("Empresa Dez")
                .preco(450.00)
                .build()
        );
    }

    @AfterEach
    void tearDown() {
        produtoRepository.deleteAll();
    }

   @Test
    @DisplayName("Quando um id válido é listado")
    void quandoIdValidoListado() {
        produtoRepository.save(produto);
        List<Produto> resultado = driver.listar(produto.getId());
        assertEquals(produto.getId(), resultado.get(0).getId());
    }

    @Test
    @DisplayName("Quando um id nulo for fornecido para listagem")
    void quandoIdNull() {
        produtoRepository.save(produto);
        List<Produto> resultado = driver.listar(null);
        assertEquals(1, resultado.size());
    }
}
