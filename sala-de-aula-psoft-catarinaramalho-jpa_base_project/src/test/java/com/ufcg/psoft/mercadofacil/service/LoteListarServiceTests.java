package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.Lote;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.LoteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DisplayName("Testes do Serviço de listagem do lote")
public class LoteListarServiceTests {

    @Autowired
    LoteListarService driver;

    @Autowired
    LoteRepository loteRepository;

    Produto produto;
    Lote lote;

    @BeforeEach
    void setup() {
        produto = Produto.builder()
                .codigoDeBarras("7899137500100")
                .nome("Produto Dez")
                .fabricante("Empresa Dez")
                .preco(450.00)
                .build();
        lote = loteRepository.save(Lote.builder().produto(produto).numeroDeItens(400).build());
    }

    @AfterEach
    void tearDown() {
        loteRepository.deleteAll();
    }

    @Test
    @DisplayName("Quando um id válido é listado")
    void quandoIdValidoListado() {
        List<Lote> resultado = driver.listar(lote.getId());
        assertEquals(lote.getId(), resultado.get(0).getId());
    }

    @Test
    @DisplayName("Quando um id nulo for fornecido para listagem")
    void quandoIdNull() {
        List<Lote> resultado = driver.listar(null);
        assertEquals(1, resultado.size());
    }
}
