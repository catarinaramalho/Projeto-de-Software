package com.ufcg.psoft.pitsa.service.estabelecimento;

import com.ufcg.psoft.pitsa.dto.SaborGetRequestDTO;
import com.ufcg.psoft.pitsa.dto.SaborPostPutRequestDTO;
import com.ufcg.psoft.pitsa.exception.TipoSaborInvalidoException;
import com.ufcg.psoft.pitsa.model.Estabelecimento;
import com.ufcg.psoft.pitsa.model.Sabor;
import com.ufcg.psoft.pitsa.repository.EstabelecimentoRepository;
import com.ufcg.psoft.pitsa.repository.SaborRepository;
import com.ufcg.psoft.pitsa.service.sabor.SaborCriarService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("Testes do serviço de listagem de cardápio de estabelecimento")
public class EstabelecimentoListarCardapioServiceTests {

    @Autowired
    EstabelecimentoListarCardapioService driver;

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    SaborRepository saborRepository;

    @Autowired
    SaborCriarService saborCriarService;

    Estabelecimento estabelecimento;

    Sabor sabor;

    SaborPostPutRequestDTO saborPostPutRequestDTO;

    @BeforeEach
    void setup() {
        estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
                .codigoAcesso("123456")
                .sabores(new HashSet<>())
                .build()
        );
        saborPostPutRequestDTO = SaborPostPutRequestDTO.builder()
                .nome("Sabor Um")
                .tipo("salgado")
                .precoM(10.00)
                .precoG(20.00)
                .build();
        sabor = saborCriarService.salvar(saborPostPutRequestDTO, estabelecimento.getId(), estabelecimento.getCodigoAcesso());
    }

    @AfterEach
    void tearDown() {
        estabelecimentoRepository.deleteAll();
        saborRepository.deleteAll();
    }

    @Test
    @DisplayName("Quando listamos o cardápio de um estabelecimento válido")
    void quandoListamosCardapioEstablecimentoValido() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        List<SaborGetRequestDTO> resultado = driver.listarCardapio(estabelecimento.getId(), null);

        // Assert
        assertAll(
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals(sabor.getNome(), resultado.get(0).getNome()),
                () -> assertEquals(sabor.getPrecoM(), resultado.get(0).getPrecoM()),
                () -> assertEquals(sabor.getPrecoG(), resultado.get(0).getPrecoG())
        );
    }

    @Test
    @DisplayName("Quando listamos o cardápio de um estabelecimento válido mais de um")
    void quandoListamosCardapioEstablecimentoValidoMaisDeUm() {
        // Arrange
        SaborPostPutRequestDTO saborPostPutRequestDTO1 = SaborPostPutRequestDTO.builder()
                .nome("Sabor Dois")
                .tipo("doce")
                .precoM(15.00)
                .precoG(27.50)
                .build();
        saborCriarService.salvar(saborPostPutRequestDTO1, estabelecimento.getId(), estabelecimento.getCodigoAcesso());

        // Act
        List<SaborGetRequestDTO> resultado = driver.listarCardapio(estabelecimento.getId(), null);

        // Assert
        assertAll(
                () -> assertEquals(2, resultado.size())
        );
    }

    @Test
    @DisplayName("Quando listamos o cardápio de um estabelecimento com tipo invalido")
    void quandoListamosCardapioEstablecimentoTipoInvalido() {
        // Arrange
        // nenhuma necessidade além do setup()

        // Act
        TipoSaborInvalidoException thrown = assertThrows(
                TipoSaborInvalidoException.class,
                () -> driver.listarCardapio(estabelecimento.getId(), "invalido")
        );

        // Assert
        assertAll(
                () -> assertEquals("O tipo de sabor e invalido!", thrown.getMessage())
        );
    }

    @Test
    @DisplayName("Quando listamos o cardápio de um estabelecimento com tipo valido")
    void quandoListamosCardapioEstablecimentoTipoValido() {
        // Arrange
        SaborPostPutRequestDTO saborPostPutRequestDTO1 = SaborPostPutRequestDTO.builder()
                .nome("Sabor Dois")
                .tipo("doce")
                .precoM(15.00)
                .precoG(27.50)
                .build();
        saborCriarService.salvar(saborPostPutRequestDTO1, estabelecimento.getId(), estabelecimento.getCodigoAcesso());

        // Act
        List<SaborGetRequestDTO> resultado = driver.listarCardapio(estabelecimento.getId(), "doce");

        // Assert
        assertAll(
                () -> assertEquals(1, resultado.size()),
                () -> assertEquals(saborPostPutRequestDTO1.getNome(), resultado.get(0).getNome()),
                () -> assertEquals(saborPostPutRequestDTO1.getPrecoM(), resultado.get(0).getPrecoM()),
                () -> assertEquals(saborPostPutRequestDTO1.getPrecoG(), resultado.get(0).getPrecoG())
        );
    }

    @Test
    @DisplayName("Quando listamos o cardápio de um estabelecimento com sabores disponíveis e indisponíveis")
    void quandoListamosCardapioEstablecimentoComSaboresDisponiveisEIndisponiveis() {
        // Arrange
        SaborPostPutRequestDTO sabor2 = SaborPostPutRequestDTO.builder()
                .nome("Sabor Dois")
                .tipo("salgado")
                .precoM(15.00)
                .precoG(27.50)
                .disponivel(false)
                .build();
        saborCriarService.salvar(sabor2, estabelecimento.getId(), estabelecimento.getCodigoAcesso());
        SaborPostPutRequestDTO sabor3 = SaborPostPutRequestDTO.builder()
                .nome("Sabor Três")
                .tipo("salgado")
                .precoM(15.00)
                .precoG(27.50)
                .build();
        saborCriarService.salvar(sabor3, estabelecimento.getId(), estabelecimento.getCodigoAcesso());
        // Act
        List<SaborGetRequestDTO> resultado = driver.listarCardapio(estabelecimento.getId(), null);

        // Assert
        assertAll(
                () -> assertEquals(3, resultado.size()),
                () -> assertEquals(sabor2.getNome(), resultado.get(2).getNome()),
                () -> assertFalse(resultado.get(2).isDisponivel())
        );
    }

    @Test
    @DisplayName("Quando listamos o cardápio do tipo doce de um estabelecimento com sabores disponíveis e indisponíveis")
    void quandoListamosCardapioDoceEstablecimentoComSaboresDisponiveisEIndisponiveis() {
        // Arrange
        SaborPostPutRequestDTO sabor2 = SaborPostPutRequestDTO.builder()
                .nome("Sabor Dois")
                .tipo("doce")
                .precoM(15.00)
                .precoG(27.50)
                .disponivel(false)
                .build();
        saborCriarService.salvar(sabor2, estabelecimento.getId(), estabelecimento.getCodigoAcesso());
        SaborPostPutRequestDTO sabor3 = SaborPostPutRequestDTO.builder()
                .nome("Sabor Três")
                .tipo("doce")
                .precoM(15.00)
                .precoG(27.50)
                .disponivel(true)
                .build();
        saborCriarService.salvar(sabor3, estabelecimento.getId(), estabelecimento.getCodigoAcesso());
        // Act
        List<SaborGetRequestDTO> resultado = driver.listarCardapio(estabelecimento.getId(), "doce");

        // Assert
        assertAll(
                () -> assertEquals(2, resultado.size()),
                () -> assertEquals(sabor2.getNome(), resultado.get(1).getNome()),
                () -> assertFalse(resultado.get(1).isDisponivel())
        );
    }

    @Test
    @DisplayName("Quando listamos o cardápio do tipo salgado de um estabelecimento com sabores disponíveis e indisponíveis")
    void quandoListamosCardapioSalgadoEstablecimentoComSaboresDisponiveisEIndisponiveis() {
        // Arrange
        SaborPostPutRequestDTO sabor2 = SaborPostPutRequestDTO.builder()
                .nome("Sabor Dois")
                .tipo("salgado")
                .precoM(15.00)
                .precoG(27.50)
                .disponivel(false)
                .build();
        saborCriarService.salvar(sabor2, estabelecimento.getId(), estabelecimento.getCodigoAcesso());
        SaborPostPutRequestDTO sabor3 = SaborPostPutRequestDTO.builder()
                .nome("Sabor Três")
                .tipo("salgado")
                .precoM(15.00)
                .precoG(27.50)
                .build();
        saborCriarService.salvar(sabor3, estabelecimento.getId(), estabelecimento.getCodigoAcesso());
        // Act
        List<SaborGetRequestDTO> resultado = driver.listarCardapio(estabelecimento.getId(), "salgado");

        // Assert
        assertAll(
                () -> assertEquals(3, resultado.size()),
                () -> assertEquals(sabor2.getNome(), resultado.get(2).getNome()),
                () -> assertFalse(resultado.get(2).isDisponivel())
        );
    }
}