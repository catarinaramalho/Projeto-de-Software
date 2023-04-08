package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Testes do Serviço de alteração do produto")
public class ProdutoAlterarServiceTests {

    @Autowired
    ProdutoAlterarService driver;

    @MockBean
    ProdutoRepository<Produto, Long> produtoRepository;

    Produto produto;

    @BeforeEach
    void setup() {
        Mockito.when(produtoRepository.find(10L))
                .thenReturn(Produto.builder()
                        .id(10L)
                        .codigoBarra("7899137500104")
                        .nome("Produto Dez")
                        .fabricante("Empresa Dez")
                        .preco(450.00)
                        .build()
                );
        produto = produtoRepository.find(10L);
    }

    @Test
    @DisplayName("Quando um novo nome válido for fornecido para o produto")
    void quandoNovoNomeValido() {
        // Arrange
        produto.setNome("Produto Dez Atualizado");
        Mockito.when(produtoRepository.update(produto))
                .thenReturn(Produto.builder()
                        .id(10L)
                        .codigoBarra("7899137500104")
                        .nome("Produto Dez Atualizado")
                        .fabricante("Empresa Dez")
                        .preco(450.00)
                        .build()
                );

        // Act
        Produto resultado = driver.alterar(produto);

        // Assert
        assertEquals("Produto Dez Atualizado", resultado.getNome());
    }

    @Test
    @DisplayName("Quando um novo id válido for fornecido para o produto")
    void quandoNovoIdValido() {
        // Arrange
        produto.setId(20L);
        Mockito.when(produtoRepository.update(produto))
                .thenReturn(Produto.builder()
                        .id(20L)
                        .codigoBarra("7899137500104")
                        .nome("Produto Dez")
                        .fabricante("Empresa Dez")
                        .preco(450.00)
                        .build()
                );

        // Act
        Produto resultado = driver.alterar(produto);

        // Assert
        assertEquals(20L, resultado.getId());
    }
    @Test
    @DisplayName("Quando um novo código de barra válido for fornecido para o produto")
    void quandoNovoCodigoBarraValido() {
                // Arrange
                produto.setCodigoBarra("7899137500105");
                Mockito.when(produtoRepository.update(produto))
                        .thenReturn(Produto.builder()
                                .id(10L)
                                .codigoBarra("7899137500105")
                                .nome("Produto Dez")
                                .fabricante("Empresa Dez")
                                .preco(450.00)
                                .build()
                        );
        
                // Act
                Produto resultado = driver.alterar(produto);
        
                // Assert
                assertEquals("7899137500105", resultado.getCodigoBarra());
        }
    @Test
    @DisplayName("Quando um novo fabricante válido for fornecido para o produto")
    void quandoNovoFabricanteValido() {
                // Arrange
                produto.setFabricante("Empresa Dez Atualizada");
                Mockito.when(produtoRepository.update(produto))
                        .thenReturn(Produto.builder()
                                .id(10L)
                                .codigoBarra("7899137500104")
                                .nome("Produto Dez")
                                .fabricante("Empresa Dez Atualizada")
                                .preco(450.00)
                                .build()
                        );
        
                // Act
                Produto resultado = driver.alterar(produto);
        
                // Assert
                assertEquals("Empresa Dez Atualizada", resultado.getFabricante());
        }
    @Test
    @DisplayName("Quando um novo preço válido for fornecido para o produto")
    void quandoNovoPrecoValido() {
                // Arrange
                produto.setPreco(500.00);
                Mockito.when(produtoRepository.update(produto))
                        .thenReturn(Produto.builder()
                                .id(10L)
                                .codigoBarra("7899137500104")
                                .nome("Produto Dez")
                                .fabricante("Empresa Dez")
                                .preco(500.00)
                                .build()
                        );
        
                // Act
                Produto resultado = driver.alterar(produto);
        
                // Assert
                assertEquals(500.00, resultado.getPreco());
        }
        @Test
        @DisplayName("Quando um novo id válido for fornecido para o produto")
        void quandoNovoIdValido() {
            // Arrange
            produto.setId(20L);
            Mockito.when(produtoRepository.update(produto))
                    .thenReturn(Produto.builder()
                            .id(20L)
                            .codigoBarra("7899137500104")
                            .nome("Produto Dez")
                            .fabricante("Empresa Dez")
                            .preco(450.00)
                            .build()
                    );
    
            // Act
            Produto resultado = driver.alterar(produto);
    
            // Assert
            assertEquals(20L, resultado.getId());
        }
        @Test
        @DisplayName("Quando um novo nome inválido for fornecido para o produto")
        void quandoNovoNomeInvalido() {
            // Arrange
            produto.setNome("");
            Mockito.when(produtoRepository.update(produto))
                    .thenReturn(Produto.builder()
                            .id(10L)
                            .codigoBarra("7899137500104")
                            .nome("")
                            .fabricante("Empresa Dez")
                            .preco(450.00)
                            .build()
                    );
    
            // Act
            Produto resultado = driver.alterar(produto);
    
            // Assert
            RuntimeException thrown = assertThrows(
                RuntimeException.class, 
                () -> driver.alterar(produto)
                );
                assertEquals("Nome Inválido", thrown.getMessage());
        }
        @Test
        @DisplayName("Quando um id inválido for fornecido para o produto")
        void quandoNovoIdInvalido() {
            // Arrange
            produto.setId(0L);
            Mockito.when(produtoRepository.update(produto))
                    .thenReturn(Produto.builder()
                            .id(null)
                            .codigoBarra("7899137500104")
                            .nome("Produto Dez")
                            .fabricante("Empresa Dez")
                            .preco(450.00)
                            .build()
                    );
    
            // Act
            Produto resultado = driver.alterar(produto);
    
            // Assert
            RuntimeException thrown = assertThrows(
                RuntimeException.class, 
                () -> driver.alterar(produto)
                );
                assertEquals("Id Inválido", thrown.getMessage());
        }

        @Test
        @DisplayName("Quando o preço é igual a zero")
                void precoIgualAZero() {
                // Arrange
                produto.setPreco(0.0);
                Mockito.when(produtoRepository.update(produto))
                        .thenThrow(new RuntimeException("Preço inválido"));

                // Act
                RuntimeException thrown = assertThrows(
                        RuntimeException.class,
                        () -> driver.alterar(produto)
                );

                // Assert
                assertEquals("Preço inválido", thrown.getMessage());
        }
        @Test
        @DisplayName("Quando o preço é menor que zero")
                void precoMenorQueZero() {
                // Arrange
                produto.setPreco(-1.0);
                Mockito.when(produtoRepository.update(produto))
                        .thenThrow(new RuntimeException("Preço inválido"));

                // Act
                RuntimeException thrown = assertThrows(
                        RuntimeException.class,
                        () -> driver.alterar(produto)
                );

                // Assert
                assertEquals("Preço inválido", thrown.getMessage());
        }
        @Test
        @DisplayName("Quando o código de barra é inválido")
                void codigoBarraInvalido() {
                // Arrange
                produto.setCodigoBarra("719913750010");
                Mockito.when(produtoRepository.update(produto))
                        .thenThrow(new RuntimeException("Código de barra inválido"));

                // Act
                RuntimeException thrown = assertThrows(
                        RuntimeException.class,
                        () -> driver.alterar(produto)
                );

                // Assert
                assertEquals("Código de barras inválido", thrown.getMessage());
        }
        @Test
        @DisplayName("Quando o código de barra é nulo")
                void codigoBarraNulo() {
                // Arrange
                produto.setCodigoBarra(null);
                Mockito.when(produtoRepository.update(produto))
                        .thenThrow(new RuntimeException("Código de barra inválido"));

                // Act
                RuntimeException thrown = assertThrows(
                        RuntimeException.class,
                        () -> driver.alterar(produto)
                );

                // Assert
                assertEquals("Código de barras inválido", thrown.getMessage());
        }
        @Test
        @DisplayName("Quando o código de barra é vazio")
                void codigoBarraVazio() {
                // Arrange
                produto.setCodigoBarra("");
                Mockito.when(produtoRepository.update(produto))
                        .thenThrow(new RuntimeException("Código de barra inválido"));

                // Act
                RuntimeException thrown = assertThrows(
                        RuntimeException.class,
                        () -> driver.alterar(produto)
                );

                // Assert
                assertEquals("Código de barras inválido", thrown.getMessage());
        }
        @Test
        @DisplayName("Quando o fabricante é nulo")
                void fabricanteNulo() {
                // Arrange
                produto.setFabricante(null);
                Mockito.when(produtoRepository.update(produto))
                        .thenThrow(new RuntimeException("Fabricante inválido"));

                // Act
                RuntimeException thrown = assertThrows(
                        RuntimeException.class,
                        () -> driver.alterar(produto)
                );

                // Assert
                assertEquals("Fabricante inválido", thrown.getMessage());
        }
        @Test
        @DisplayName("Quando o fabricante é vazio")
                void fabricanteVazio() {
                // Arrange
                produto.setFabricante("");
                Mockito.when(produtoRepository.update(produto))
                        .thenThrow(new RuntimeException("Fabricante inválido"));

                // Act
                RuntimeException thrown = assertThrows(
                        RuntimeException.class,
                        () -> driver.alterar(produto)
                );

                // Assert
                assertEquals("Fabricante inválido", thrown.getMessage());
        }
        @Test
        @DisplayName("Quando o nome é nulo")
                void nomeNulo() {
                // Arrange
                produto.setNome(null);
                Mockito.when(produtoRepository.update(produto))
                        .thenThrow(new RuntimeException("Nome inválido"));

                // Act
                RuntimeException thrown = assertThrows(
                        RuntimeException.class,
                        () -> driver.alterar(produto)
                );

                // Assert
                assertEquals("Nome inválido", thrown.getMessage());
        }
        @Test
        @DisplayName("Quando o nome é vazio")
                void nomeVazio() {
                // Arrange
                produto.setNome("");
                Mockito.when(produtoRepository.update(produto))
                        .thenThrow(new RuntimeException("Nome inválido"));

                // Act
                RuntimeException thrown = assertThrows(
                        RuntimeException.class,
                        () -> driver.alterar(produto)
                );

                // Assert
                assertEquals("Nome inválido", thrown.getMessage());
        }
        @Test
        @DisplayName("Quando o produto é nulo")
                void produtoNulo() {
                // Arrange
                produto = null;
                Mockito.when(produtoRepository.update(produto))
                        .thenThrow(new RuntimeException("Produto inválido"));

                // Act
                RuntimeException thrown = assertThrows(
                        RuntimeException.class,
                        () -> driver.alterar(produto)
                );

                // Assert
                assertEquals("Produto inválido", thrown.getMessage());
        }



}
