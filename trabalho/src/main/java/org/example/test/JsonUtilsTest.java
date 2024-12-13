package org.example.test;

import org.example.api.implementation.models.MapaImpl;
import org.example.api.implementation.utils.ImportJsonImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de teste para validar o comportamento do utilitario de importacao de
 * JSON
 * que carrega informacoes sobre o mapa e outros elementos do sistema.
 */
public class JsonUtilsTest {
    private final MapaImpl mapa = new MapaImpl();
    private final ImportJsonImpl jsonUtils = new ImportJsonImpl(mapa);

    /**
     * Testa o comportamento ao carregar um arquivo JSON com uma estrutura invalida.
     * Verifica se a excecao correta e lancada e contem a mensagem esperada.
     */
    @Test
    void testCarregarMapaComEstruturaInvalida() {
        String jsonPath = "D:\\githubProjects\\simulador-missoes-to-cruz\\trabalho\\src\\main\\java\\org\\example\\test\\resources\\invalid_field.json";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> jsonUtils.carregarMapa(jsonPath));

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Nome da divisao nao pode ser vazio ou nulo."));
    }

    /**
     * Testa o comportamento ao carregar um arquivo JSON com dados validos.
     * Verifica se as divisoes, o alvo e outros elementos sao carregados
     * corretamente.
     */
    @Test
    void testCarregarMapaComDadosValidos() {
        String jsonPath = "D:\\githubProjects\\simulador-missoes-to-cruz\\trabalho\\src\\main\\java\\org\\example\\test\\resources\\test_map.json";

        assertDoesNotThrow(() -> jsonUtils.carregarMapa(jsonPath));

        // Verifique se as divisos foram carregadas corretamente
        assertEquals(21, mapa.getDivisoes().size());
        assertEquals("Laboratorio", mapa.getAlvo().getDivisao().getNomeDivisao());
        assertEquals("quimico", mapa.getAlvo().getTipo());
    }

    /**
     * Testa o comportamento ao carregar um arquivo JSON onde uma divisao
     * referenciada
     * esta ausente. Verifica se uma excecao com a mensagem esperada e lancada.
     */
    @Test
    void testCarregarMapaComItemSemDivisao() {
        String jsonPath = "D:\\githubProjects\\simulador-missoes-to-cruz\\trabalho\\src\\main\\java\\org\\example\\test\\resources\\missing_division.json";

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> jsonUtils.carregarMapa(jsonPath));

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Divisao 'Divisao3' nao encontrada."));
    }

    /**
     * Testa o comportamento ao carregar um arquivo JSON com uma estrutura invalida,
     * onde o campo "edificio" esta ausente. Verifica se a excecao correta e lancada
     * com a mensagem esperada.
     */
    @Test
    void testCarregarMapaComLigacaoInvalida() {
        String jsonPath = "D:\\githubProjects\\simulador-missoes-to-cruz\\trabalho\\src\\main\\java\\org\\example\\test\\resources\\invalid_structure.json"; // Atualize
                                                                                                                                                             // o
                                                                                                                                                             // caminho
                                                                                                                                                             // conforme
                                                                                                                                                             // necessario

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> jsonUtils.carregarMapa(jsonPath));

        assertNotNull(exception);
        assertTrue(exception.getMessage()
                .contains("Cannot invoke \"org.json.simple.JSONArray.iterator()\" because \"edificioArray\" is null"));
    }
}
