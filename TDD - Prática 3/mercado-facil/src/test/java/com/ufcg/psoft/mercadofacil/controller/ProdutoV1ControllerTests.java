package com.ufcg.psoft.mercadofacil.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.ProdutoRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes do controlador de Produtos")
public class ProdutoV1ControllerTests {
    @Autowired
    MockMvc driver;

    @Autowired
    ProdutoRepository<Produto, Long> produtoRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    Produto produto;

    @BeforeEach
    void setup() {
        produto = produtoRepository.find(10L);
    }

    @AfterEach
    void tearDown() {
        produto = null;
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação de campos obrigatórios")
    class ProdutoValidacaoCamposObrigatorios {

        @Test
        @DisplayName("Quando alteramos o nome do produto com dados válidos")
        void quandoAlteramosNomeDoProdutoValido() throws Exception {
            // Arrange
            produto.setNome("Produto Dez Alterado");

            // Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produto)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Produto resultado = objectMapper.readValue(responseJsonString, Produto.ProdutoBuilder.class).build();

            // Assert
            assertEquals(resultado.getNome(), "Produto Dez Alterado");
        }
        @Test
        @DisplayName("Quando alteramos o nome do produto com dados inválidos") 
        void quandoAlteramosNomeDoProdutoInvalido() throws Exception {
            // Arrange
            produto.setNome("");

            // Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produto)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getErrorMessage();

            // Assert
            assertEquals(responseJsonString, "Nome inválido");
        }
        @Test
        @DisplayName("Quando alteramos o nome do produto com dados inválidos") 
        void quandoAlteramosNomeDoProdutoInvalido2() throws Exception {
            // Arrange
            produto.setNome(null);

            // Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produto)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getErrorMessage();

            // Assert
            assertEquals(responseJsonString, "Nome inválido");
        }
    
        @Test
        @DisplayName("Quando alteramos o id do produto com dados válidos")
        void quandoAlteramosIdDoProdutoValido() throws Exception {
            // Arrange
            produto.setId(1L);

            // Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produto)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Produto resultado = objectMapper.readValue(responseJsonString, Produto.ProdutoBuilder.class).build();

            // Assert
            assertEquals(resultado.getId(), 1L);
        }
        @Test
        @DisplayName("Quando alteramos o id do produto com dados inválidos")
        void quandoAlteramosIdDoProdutoInvalido() throws Exception {
            // Arrange
            produto.setId(null);

            // Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produto)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getErrorMessage();

            // Assert
            assertEquals(responseJsonString, "Id inválido");
        }
        @Test
        @DisplayName("Quando alteramos o nome do fabricante do produto com dados válidos")
        void quandoAlteramosNomeDoFabricanteDoProdutoValido() throws Exception {
            // Arrange
            produto.setFabricante(new Fabricante("Fabricante Alterado"));

            // Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produto)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Produto resultado = objectMapper.readValue(responseJsonString, Produto.ProdutoBuilder.class).build();

            // Assert
            assertEquals(resultado.getFabricante().getNome(), "Fabricante Alterado");
        }
        @Test
        @DisplayName("Quando alteramos o nome do fabricante do produto com dados inválidos")
        void quandoAlteramosNomeDoFabricanteDoProdutoInvalido() throws Exception {
            // Arrange
            produto.setFabricante(new Fabricante(""));

            // Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produto)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getErrorMessage();

            // Assert
            assertEquals(responseJsonString, "Fabricante inválido");
        }
        @Test
        @DisplayName("Quando alteramos o nome do fabricante do produto com dados inválidos")
        void quandoAlteramosNomeDoFabricanteDoProdutoInvalido2() throws Exception {
            // Arrange
            produto.setFabricante(new Fabricante(null));

            // Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produto)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getErrorMessage();

            // Assert
            assertEquals(responseJsonString, "Fabricante inválido");
        }
        
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação da regra sobre o preço")
    class ProdutoValidacaoRegrasDoPreco {
        @Test
        @DisplayName("Quando alteramos o preço do produto com dados válidos")
        void quandoAlteramosPrecoDoProdutoValido() throws Exception {
            // Arrange
            produto.setPreco(10.0);

            // Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produto)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Produto resultado = objectMapper.readValue(responseJsonString, Produto.ProdutoBuilder.class).build();

            // Assert
            assertEquals(resultado.getPreco(), 10.0);
        }
        @Test
        @DisplayName("Quando alteramos o preço do produto com dados inválidos")
        void quandoAlteramosPrecoDoProdutoInvalido() throws Exception {
            // Arrange
            produto.setPreco(-10.0);

            // Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produto)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getErrorMessage();

            // Assert
            assertEquals(responseJsonString, "Preço inválido");
        }
        @Test
        @DisplayName("Quando alteramos o preço do produto com dados inválidos")
        void quandoAlteramosPrecoDoProdutoInvalido2() throws Exception {
            // Arrange
            produto.setPreco(0.0);

            // Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produto)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getErrorMessage();

            // Assert
            assertEquals(responseJsonString, "Preço inválido");
        }

    }

    @Nested
    @DisplayName("Conjunto de casos de verificação da validação do código de barras")
    class ProdutoValidacaoCodigoDeBarras {
        @Test
        @DisplayName("Quando alteramos o código de barras do produto com dados válidos")
        void quandoAlteramosCodigoDeBarrasDoProdutoValido() throws Exception {
            // Arrange
            produto.setCodigoDeBarras("7899137500100");

            // Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produto)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Produto resultado = objectMapper.readValue(responseJsonString, Produto.ProdutoBuilder.class).build();

            // Assert
            assertEquals(resultado.getCodigoDeBarras(), "7899137500100");
        }
        @Test
        @DisplayName("Quando alteramos o código de barras do produto com dados inválidos")
        void quandoAlteramosCodigoDeBarrasDoProdutoInvalido() throws Exception {
            // Arrange
            produto.setCodigoDeBarras("789913750020");

            // Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produto)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getErrorMessage();

            // Assert
            assertEquals(responseJsonString, "Código de barras inválido");
        }
        @Test
        @DisplayName("Quando alteramos o código de barras do produto com dados inválidos")
        void quandoAlteramosCodigoDeBarrasDoProdutoInvalido2() throws Exception {
            // Arrange
            produto.setCodigoDeBarras("");

            // Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produto)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getErrorMessage();

            // Assert
            assertEquals(responseJsonString, "Código de barras inválido");
        }
        @Test
        @DisplayName("Quando alteramos o código de barras do produto com dados inválidos")
        void quandoAlteramosCodigoDeBarrasDoProdutoInvalido3() throws Exception {
            // Arrange
            produto.setCodigoDeBarras(null);

            // Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produto)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getErrorMessage();

            // Assert
            assertEquals(responseJsonString, "Código de barras inválido");
        }
        @Test
        @DisplayName("Validação de código de barras com menos de 13 dígitos")
        void quandoAlteramosCodigoDeBarrasDoProdutoInvalido4() throws Exception {
            // Arrange
            produto.setCodigoDeBarras("7899150010");

            // Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produto)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getErrorMessage();

            // Assert
            assertEquals(responseJsonString, "Código de barras inválido");
        }
        @Test
        @DisplayName("Validação de código de barras com mais de 13 dígitos")
        void quandoAlteramosCodigoDeBarrasDoProdutoInvalido5() throws Exception {
            // Arrange
            produto.setCodigoDeBarras("789915001000000");

            // Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produto)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getErrorMessage();

            // Assert
            assertEquals(responseJsonString, "Código de barras inválido");
        }
        @Test
        @DisplayName("Validação de código de barras com dígito verificador inválido")
        void quandoAlteramosCodigoDeBarrasDoProdutoInvalido6() throws Exception {
            // Arrange
            produto.setCodigoDeBarras("7899150010001");

            // Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produto)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getErrorMessage();

            // Assert
            assertEquals(responseJsonString, "Código de barras inválido");
        }
    }

}
