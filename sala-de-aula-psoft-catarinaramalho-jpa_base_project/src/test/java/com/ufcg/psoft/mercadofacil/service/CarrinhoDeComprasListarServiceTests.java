package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.CarrinhoDeCompras;
import com.ufcg.psoft.mercadofacil.model.Cliente;
import com.ufcg.psoft.mercadofacil.model.Lote;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.CarrinhoDeComprasRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@SpringBootTest
@DisplayName("Testes do Serviço de listagem do carrinho de compras")
public class CarrinhoDeComprasListarServiceTests {
    @Autowired
    CarrinhoDeComprasListarService driver;
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
        carrinho = CarrinhoDeCompras.builder()
                .quantidade(1)
                .id(1L)
                .cliente(cliente)
                .lote(lote)
                .build();
    }

    @AfterEach
    void tearDown() {
        carrinhoDeComprasRepository.deleteAll();
    }

    @Test
    @DisplayName("Teste de listagem de carrinho de compras com carrinho nulo")
    void testListarCarrinhoDeComprasNull(){
        List<CarrinhoDeCompras> resultado = driver.listar(null);
        Assertions.assertEquals(0, resultado.size());
    }
    @Test
    @DisplayName("Quando um id válido é listado")
    void quandoIdValidoListado() {
        carrinhoDeComprasRepository.save(carrinho);
        List<CarrinhoDeCompras> resultado = driver.listar(carrinho.getId());
        Assertions.assertEquals(carrinho.getId(), resultado.get(0).getId());
    }


}