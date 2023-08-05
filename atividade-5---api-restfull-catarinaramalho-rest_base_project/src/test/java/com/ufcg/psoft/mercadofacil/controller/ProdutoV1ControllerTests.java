package com.ufcg.psoft.mercadofacil.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufcg.psoft.mercadofacil.dto.ProdutoNomePatchRequestDTO;
import com.ufcg.psoft.mercadofacil.dto.ProdutoPostPutRequestDTO;
import com.ufcg.psoft.mercadofacil.exception.CustomErrorType;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.ProdutoRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes do controlador de Produtos")
public class ProdutoV1ControllerTests {
    @Autowired
    MockMvc driver;

    @Autowired
    ProdutoRepository produtoRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    Produto produto;

    @BeforeEach
    void setup() {
        // Object Mapper suporte para LocalDateTime
        objectMapper.registerModule(new JavaTimeModule());
        produto = produtoRepository.save(Produto.builder()
                .codigoDeBarras("7899137500100")
                .fabricante("Fabricante Dez")
                .nome("Produto Dez")
                .preco(100.00)
                .build()
        );
    }

    @AfterEach
    void tearDown() {
        produtoRepository.deleteAll();
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação de campos obrigatórios")
    class ProdutoVerificacaoCamposObrigatorios {

        @Test
        @DisplayName("Quando alteramos apenas o nome do produto com dados válidos")
        void quandoAlteramosNomeDoProdutoValido() throws Exception {
            // Arrange
            ProdutoNomePatchRequestDTO produtoNomePatchRequestDTO = ProdutoNomePatchRequestDTO.builder()
                    .nome("Produto Dez Alterado")
                    .build();

            // Act
            String responseJsonString = driver.perform(patch("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produtoNomePatchRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Produto resultado = objectMapper.readValue(responseJsonString, Produto.ProdutoBuilder.class).build();

            // Assert
            assertEquals("Produto Dez Alterado", resultado.getNome());
        }

        @Test
        @DisplayName("Quando alteramos apenas o nome do produto com dados inválido (em branco)")
        void quandoAlteramosNomeDoProdutoInvalidoBranco() throws Exception {
            // Arrange
            ProdutoNomePatchRequestDTO produtoNomePatchRequestDTO = ProdutoNomePatchRequestDTO.builder()
                    .nome("")
                    .build();

            // Act
            String responseJsonString = driver.perform(patch("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produtoNomePatchRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Erros de validacao encontrados", resultado.getMessage());
            assertEquals("Nome obrigatorio", resultado.getErrors().get(0));
        }

    }

    @Nested
    @DisplayName("Conjunto de casos de verificação da regra sobre o preço")
    class ProdutoVerificacaoRegrasDoPreco {
        @Test
        @DisplayName("Quando alteramos o preço do produto com dados inválidos (preço negativo)")
        void quandoAlteramosPrecoDoProdutoInvalidoNegativo() throws Exception {
            // Arrange
            ProdutoPostPutRequestDTO produtoPostPutRequestDTO = ProdutoPostPutRequestDTO.builder()
                    .codigoDeBarras("7899137500100")
                    .fabricante("Fabricante Dez")
                    .nome("Produto Dez")
                    .preco(-100.00)
                    .build();

            // Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produtoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Erros de validacao encontrados", resultado.getMessage());
            assertEquals("Preco deve ser maior ou igual a zero", resultado.getErrors().get(0));
        }

        @Test
        @DisplayName("Quando alteramos o preço do produto com dados válidos")
        void quandoAlteramosPrecoDoProdutoValido() throws Exception {
            // Arrange
            ProdutoPostPutRequestDTO produtoPostPutRequestDTO = ProdutoPostPutRequestDTO.builder()
                    .codigoDeBarras("7899137500100")
                    .fabricante("Fabricante Dez")
                    .nome("Produto Dez")
                    .preco(250.00)
                    .build();

            // Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produtoPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Produto resultado = objectMapper.readValue(responseJsonString, Produto.ProdutoBuilder.class).build();

            // Assert
            assertEquals(250.00, resultado.getPreco());
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação da validação do código de barras")
    class ProdutoVerificacaoCodigoDeBarras {
        @Test
        @DisplayName("Quando alteramos o código de barras do produto de tamanho inválido (menor)")
        void quandoAlteramosCodigoDeBarrasDoProdutoInvalidoMenor() throws Exception {
            // Arrange
            ProdutoPostPutRequestDTO produtoPostPutRequestDTO = ProdutoPostPutRequestDTO.builder()
                    .codigoDeBarras("19009")
                    .fabricante("Fabricante Dez")
                    .nome("Produto Dez")
                    .preco(100.00)
                    .build();

            // Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produtoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Erros de validacao encontrados", resultado.getMessage());
            assertEquals("Codigo de Barras invalido", resultado.getErrors().get(0));
        }

        @Test
        @DisplayName("Quando alteramos o código de barras do produto de tamanho inválido (maior)")
        void quandoAlteramosCodigoDeBarrasDoProdutoInvalidoMaior() throws Exception {
            // Arrange
            ProdutoPostPutRequestDTO produtoPostPutRequestDTO = ProdutoPostPutRequestDTO.builder()
                    .codigoDeBarras("1234567891301234")
                    .fabricante("Fabricante Dez")
                    .nome("Produto Dez")
                    .preco(100.00)
                    .build();

            // Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produtoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Erros de validacao encontrados", resultado.getMessage());
            assertEquals("Codigo de Barras invalido", resultado.getErrors().get(0));
        }

        @Test
        @DisplayName("Quando alteramos o código de barras do produto com dados válidos")
        void quandoAlteramosCodigoDeBarrasDoProdutoValido() throws Exception {
            // Arrange
            ProdutoPostPutRequestDTO produtoPostPutRequestDTO = ProdutoPostPutRequestDTO.builder()
                    .codigoDeBarras("7899137500100")
                    .fabricante("Fabricante Dez")
                    .nome("Produto Dez")
                    .preco(100.00)
                    .build();

            // Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produtoPostPutRequestDTO)))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Produto resultado = objectMapper.readValue(responseJsonString, Produto.ProdutoBuilder.class).build();

            // Assert
            assertEquals("7899137500100", resultado.getCodigoDeBarras());
        }

        @Test
        @DisplayName("Quando alteramos o código de barras do produto com dados inválido do padrão EAN-13 de empresas brasileiras")
        void quandoAlteramosCodigoDeBarrasDoProdutoInvalido() throws Exception {
            // Arrange
            ProdutoPostPutRequestDTO produtoPostPutRequestDTO = ProdutoPostPutRequestDTO.builder()
                    .codigoDeBarras("7890037500101")
                    .fabricante("Fabricante Dez")
                    .nome("Produto Dez")
                    .preco(100.00)
                    .build();

            // Act
            String responseJsonString = driver.perform(put("/v1/produtos/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produtoPostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Erros de validacao encontrados", resultado.getMessage());
            assertEquals("Codigo de Barras invalido", resultado.getErrors().get(0));
        }
    }

    @Nested
    @DisplayName("Conjunto de casos de verificação dos fluxos básicos API Rest")
    class ProdutoVerificacaoFluxosBasicosApiRest {

        final String URI_PRODUTOS = "/v1/produtos";
        ProdutoPostPutRequestDTO produtoPutRequestDTO;
        ProdutoPostPutRequestDTO produtoPostRequestDTO;

        @BeforeEach
        void setup() {
            produtoPostRequestDTO = ProdutoPostPutRequestDTO.builder()
                    .fabricante("Fabricante Vinte")
                    .nome("Produto Vinte")
                    .codigoDeBarras("7899137500209")
                    .preco(200.00)
                    .build();
            produtoPutRequestDTO = ProdutoPostPutRequestDTO.builder()
                    .fabricante("Fabricante Dez Alterado")
                    .nome("Produto Dez Alterado")
                    .codigoDeBarras("7899137500100")
                    .preco(1000.00)
                    .build();
        }

        @Test
        @DisplayName("Quando buscamos por todos produtos salvos")
        void quandoBuscamosPorTodosProdutoSalvos() throws Exception {
            // Arrange
            // Vamos ter 3 produtos no banco
            Produto produto1 = Produto.builder()
                    .fabricante("Fabricante Vinte")
                    .nome("Produto Vinte")
                    .codigoDeBarras("7899137500100")
                    .preco(200.00)
                    .build();
            Produto produto2 = Produto.builder()
                    .fabricante("Fabricante Vinte")
                    .nome("Produto Vinte")
                    .codigoDeBarras("7899137500100")
                    .preco(200.00)
                    .build();
            produtoRepository.saveAll(Arrays.asList(produto1, produto2));

            // Act
            String responseJsonString = driver.perform(get(URI_PRODUTOS)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Produto> resultado = objectMapper.readValue(responseJsonString, new TypeReference<List<Produto>>(){});


            // Assert
            assertAll(
                    () -> assertEquals(3, resultado.size())
            );

        }

        @Test
        @DisplayName("Quando buscamos um produto salvo pelo id")
        void quandoBuscamosPorUmProdutoSalvo() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_PRODUTOS + "/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Produto> listaResultados = objectMapper.readValue(responseJsonString, new TypeReference<List<Produto>>(){});
            Produto resultado = listaResultados.stream().findFirst().orElse(Produto.builder().build());

            // Assert
            assertAll(
                    () -> assertEquals(produto.getId().longValue(), resultado.getId().longValue()),
                    () -> assertEquals(produto.getNome(), resultado.getNome()),
                    () -> assertEquals(produto.getFabricante(), resultado.getFabricante()),
                    () -> assertEquals(produto.getCodigoDeBarras(), resultado.getCodigoDeBarras()),
                    () -> assertEquals(produto.getPreco().doubleValue(), resultado.getPreco().doubleValue())
            );

        }

        @Test
        @DisplayName("Quando criamos um novo produto com dados válidos")
        void quandoCriarProdutoValido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(post(URI_PRODUTOS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produtoPostRequestDTO)))
                    .andExpect(status().isCreated()) // Codigo 201
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Produto resultado = objectMapper.readValue(responseJsonString, Produto.ProdutoBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertNotNull(resultado.getId()),
                    () -> assertEquals(produtoPostRequestDTO.getNome(), resultado.getNome()),
                    () -> assertEquals(produtoPostRequestDTO.getFabricante(), resultado.getFabricante()),
                    () -> assertEquals(produtoPostRequestDTO.getCodigoDeBarras(), resultado.getCodigoDeBarras()),
                    () -> assertEquals(produtoPostRequestDTO.getPreco(), resultado.getPreco().doubleValue())
            );

        }

        @Test
        @DisplayName("Quando alteramos o produto com dados válidos")
        void quandoAlteramosProdutoValido() throws Exception {
            // Arrange
            Long produtoId = produto.getId();

            // Act
            String responseJsonString = driver.perform(put(URI_PRODUTOS + "/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(produtoPutRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Produto resultado = objectMapper.readValue(responseJsonString, Produto.ProdutoBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertEquals(resultado.getId().longValue(), produtoId),
                    () -> assertEquals(produtoPutRequestDTO.getNome(), resultado.getNome()),
                    () -> assertEquals(produtoPutRequestDTO.getFabricante(), resultado.getFabricante()),
                    () -> assertEquals(produtoPutRequestDTO.getCodigoDeBarras(), resultado.getCodigoDeBarras()),
                    () -> assertEquals(produtoPutRequestDTO.getPreco(), resultado.getPreco().doubleValue())
            );

        }

        @Test
        @DisplayName("Quando excluímos um produto salvo")
        void quandoExcluimosProdutoValido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_PRODUTOS + "/" + produto.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent()) // Codigo 204
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            assertTrue(responseJsonString.isBlank());

        }

    }

}