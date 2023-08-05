package com.ufcg.psoft.mercadofacil.repository;

import com.ufcg.psoft.mercadofacil.model.Cliente;
import com.ufcg.psoft.mercadofacil.repository.ClienteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@DisplayName("Testes do repositório de Clientes")
public class ClienteRepositoryTests {

    @Autowired
    ClienteRepository driver;

    Cliente cliente;

    @BeforeEach
    void setup() {
        cliente = Cliente.builder()
                .id(2L)
                .cpf(12312312398L)
                .nome("Cliente 1")
                .idade(18)
                .endereco("Rua Das Neves")
                .build();
    }

    @AfterEach
    void tearDown() {
        driver.deleteAll();
    }

    @Test
    @DisplayName("Quando criar um novo cliente com dados válidos")
    void testQuandoCriarCliente() {
        //Arrange
        //Act
        Cliente resultado = driver.save(cliente);
        //Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals("Cliente 1", resultado.getNome());
        assertEquals("Rua Das Neves", resultado.getEndereco());
        assertEquals(18, resultado.getIdade());
        assertEquals(12312312398L, resultado.getCpf());
    }

}
