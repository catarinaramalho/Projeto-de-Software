package com.ufcg.psoft.mercadofacil.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufcg.psoft.mercadofacil.dto.LotePostPutRequestDTO;
import com.ufcg.psoft.mercadofacil.exception.CustomErrorType;
import com.ufcg.psoft.mercadofacil.model.Lote;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.LoteRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes do controlador de Lote")
public class LoteV1ControllerTests {
    @Autowired
    MockMvc driver;

    @Autowired
    LoteRepository loteRepository;


    ObjectMapper objectMapper = new ObjectMapper();

    Lote lote;
    Produto produto;

    @BeforeEach
    void setup() {
        // Object Mapper suporte para LocalDateTime
        objectMapper.registerModule(new JavaTimeModule());
        produto = Produto.builder()
                .nome("Produto")
                .codigoDeBarras("7899137500100")
                .fabricante("Fabricante")
                .preco(10.0)
                .build();

        lote = loteRepository.save(Lote.builder()
                .produto(produto)
                .numeroDeItens(10)
                .build()
        );
    }

    @AfterEach
    void tearDown() {
        loteRepository.deleteAll();
    }

    @Nested
    @DisplayName("Conjunto de casos de operações em lote")
    class OperacoesEmLote{
        @Test
        @Transactional
        @DisplayName("Teste com numero de itens negativo")
        void testaNumeroDeItensNegativo() throws Exception {
            // Arrange
            LotePostPutRequestDTO lotePostPutRequestDTO = LotePostPutRequestDTO.builder()
                    .produto(produto)
                    .numeroDeItens(-1)
                    .build();

            // Act
            String responseJsonString = driver.perform(put("/v1/lotes/" + lote.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(lotePostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Erros de validacao encontrados", resultado.getMessage());
            assertEquals("O numero de itens deve ser maior ou igual a zero", resultado.getErrors().get(0));
        }
        @Test
        @Transactional
        @DisplayName("Teste com produto inválido")
        void testaProdutoInvalido() throws Exception {
            // Arrange
            LotePostPutRequestDTO lotePostPutRequestDTO = LotePostPutRequestDTO.builder()
                    .produto(null)
                    .numeroDeItens(10)
                    .build();

            // Act
            String responseJsonString = driver.perform(put("/v1/lotes/" + lote.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(lotePostPutRequestDTO)))
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertEquals("Erros de validacao encontrados", resultado.getMessage());
            assertEquals("Produto invalido", resultado.getErrors().get(0));
        }
    }
    @Nested
    @DisplayName("Conjunto de casos de verificação dos fluxos básicos API Rest")
    class LoteVerificacaoFluxosBasicosApiRest {

        final String URI_LOTES = "/v1/lotes";
        LotePostPutRequestDTO lotePutRequestDTO;
        LotePostPutRequestDTO lotePostRequestDTO;

        @BeforeEach
        void setup() {
            lotePostRequestDTO = LotePostPutRequestDTO.builder()
                    .produto(produto)
                    .numeroDeItens(10)
                    .build();
            lotePutRequestDTO = LotePostPutRequestDTO.builder()
                    .produto(produto)
                    .numeroDeItens(10)
                    .build();
        }

        @Test
        @Transactional
        @DisplayName("Quando buscamos por todos lotes salvos")
        void quandoBuscamosPorTodosLoteSalvos() throws Exception {
            // Arrange
            // Vamos ter 3 lotes no banco
            Produto produto1 = Produto.builder()
                    .nome("Produto 1")
                    .preco(100.00)
                    .codigoDeBarras("7899137500100")
                    .fabricante("Fabricante 1")
                    .build();
            Produto produto2 = Produto.builder()
                    .nome("Produto 2")
                    .preco(200.00)
                    .codigoDeBarras("7899137500308")
                    .fabricante("Fabricante 2")
                    .build();
            Lote lote1 = Lote.builder()
                    .produto(produto1)
                    .numeroDeItens(100)
                    .build();
            Lote lote2 = Lote.builder()
                    .produto(produto2)
                    .numeroDeItens(200)
                    .build();
            loteRepository.saveAll(Arrays.asList(lote1, lote2));

            // Act
            String responseJsonString = driver.perform(get(URI_LOTES)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            List<Lote> resultado = objectMapper.readValue(responseJsonString, new TypeReference<List<Lote>>() {
            });

            // Assert
            assertAll(
                    () -> assertEquals(3, resultado.size())
            );

        }

        @Test
        @Transactional
        @DisplayName("Quando buscamos um lote salvo pelo id")
        void quandoBuscamosPorUmLoteSalvo() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_LOTES + "/" + lote.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            List<Lote> listaResultados = objectMapper.readValue(responseJsonString, new TypeReference<List<Lote>>(){});
            Lote resultado = listaResultados.stream().findFirst().orElse(Lote.builder().build());

            // Assert
            assertAll(
                    () -> assertEquals(lote.getId().longValue(), resultado.getId().longValue()),
                    () -> assertEquals(lote.getProduto(), resultado.getProduto()),
                    () -> assertEquals(lote.getNumeroDeItens(), resultado.getNumeroDeItens())
            );

        }
        @Test
        @Transactional
        @DisplayName("Quando buscamos um lote inválido pelo id")
        void quandoBuscamosPorUmLoteInvalido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(get(URI_LOTES + "/" + 999999999)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest()) // Codigo 400
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

            // Assert
            assertAll(
                    () -> assertEquals("O Lote consultado nao existe!", resultado.getMessage())
            );

        }

        @Test
        @Transactional
        @DisplayName("Quando criamos um novo lote com dados válidos")
        void quandoCriarLoteValido() throws Exception {
            // Arrange
            Produto produto3 = Produto.builder()
                    .nome("Produto 3")
                    .preco(200.00)
                    .codigoDeBarras("7899137500100")
                    .fabricante("Fabricante 3")
                    .build();

            lotePostRequestDTO = LotePostPutRequestDTO.builder()
                    .produto(produto3)
                    .numeroDeItens(10)
                    .build();

            // Act
            String responseJsonString = driver.perform(post(URI_LOTES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(lotePostRequestDTO)))
                    .andExpect(status().isCreated()) // Codigo 201
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Lote resultado = objectMapper.readValue(responseJsonString, Lote.LoteBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertNotNull(resultado.getId().longValue()),
                    () -> assertEquals(lotePostRequestDTO.getProduto().getNome(), resultado.getProduto().getNome()),
                    () -> assertEquals(lotePostRequestDTO.getProduto().getPreco().doubleValue(), resultado.getProduto().getPreco().doubleValue()),
                    () -> assertEquals(lotePostRequestDTO.getProduto().getCodigoDeBarras(), resultado.getProduto().getCodigoDeBarras()),
                    () -> assertEquals(lotePostRequestDTO.getProduto().getFabricante(), resultado.getProduto().getFabricante()),
                    () -> assertEquals(lotePostRequestDTO.getNumeroDeItens(), resultado.getNumeroDeItens())
            );
        }

        @Test
        @Transactional
        @DisplayName("Quando alteramos o lote com dados válidos")
        void quandoAlteramosLoteValido() throws Exception {
            // Arrange
            Long loteId = lote.getId();

            // Act
            String responseJsonString = driver.perform(put(URI_LOTES + "/" + lote.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(lotePutRequestDTO)))
                    .andExpect(status().isOk()) // Codigo 200
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            Lote resultado = objectMapper.readValue(responseJsonString, Lote.LoteBuilder.class).build();

            // Assert
            assertAll(
                    () -> assertEquals(resultado.getId().longValue(), loteId),
                    () -> assertEquals(lotePutRequestDTO.getProduto(), resultado.getProduto()),
                    () -> assertEquals(lotePutRequestDTO.getNumeroDeItens(), resultado.getNumeroDeItens())
            );

        }

        @Test
        @Transactional
        @DisplayName("Quando excluímos um lote salvo")
        void quandoExcluimosLoteValido() throws Exception {
            // Arrange
            // nenhuma necessidade além do setup()

            // Act
            String responseJsonString = driver.perform(delete(URI_LOTES + "/" + lote.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent()) // Codigo 204
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            assertTrue(responseJsonString.isBlank());

        }

    }
}
