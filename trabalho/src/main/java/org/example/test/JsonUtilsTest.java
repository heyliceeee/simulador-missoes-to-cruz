package org.example.test;

import org.example.api.implementation.models.MapaImpl;
import org.example.api.implementation.utils.ImportJsonImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Classe de teste para validar o comportamento do utilitário de importação de JSON
 * que carrega informações sobre o mapa e outros elementos do sistema.
 */
public class JsonUtilsTest {
    private final MapaImpl mapa = new MapaImpl();
    private final ImportJsonImpl jsonUtils = new ImportJsonImpl(mapa);

    /**
     * Testa o comportamento ao carregar um arquivo JSON com uma estrutura inválida.
     * Verifica se a exceção correta é lançada e contém a mensagem esperada.
     */
    @Test
    void testCarregarMapaComEstruturaInvalida() {
        String jsonPath = "D:\\githubProjects\\simulador-missoes-to-cruz\\trabalho\\src\\main\\java\\org\\example\\test\\resources\\invalid_field.json";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> jsonUtils.carregarMapa(jsonPath)
        );

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Nome da divisao nao pode ser vazio ou nulo."));
    }

    /**
     * Testa o comportamento ao carregar um arquivo JSON com dados válidos.
     * Verifica se as divisões, o alvo e outros elementos são carregados corretamente.
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
     * Testa o comportamento ao carregar um arquivo JSON onde uma divisão referenciada
     * está ausente. Verifica se uma exceção com a mensagem esperada é lançada.
     */
    @Test
    void testCarregarMapaComItemSemDivisao() {
        String jsonPath = "D:\\githubProjects\\simulador-missoes-to-cruz\\trabalho\\src\\main\\java\\org\\example\\test\\resources\\missing_division.json";

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> jsonUtils.carregarMapa(jsonPath)
        );

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Divisao 'Divisao3' nao encontrada."));
    }

    /**
     * Testa o comportamento ao carregar um arquivo JSON com uma estrutura inválida,
     * onde o campo "edificio" está ausente. Verifica se a exceção correta é lançada
     * com a mensagem esperada.
     */
    @Test
    void testCarregarMapaComLigacaoInvalida() {
        String jsonPath = "D:\\githubProjects\\simulador-missoes-to-cruz\\trabalho\\src\\main\\java\\org\\example\\test\\resources\\invalid_structure.json"; // Atualize o caminho conforme necessário

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> jsonUtils.carregarMapa(jsonPath)
        );

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Cannot invoke \"org.json.simple.JSONArray.iterator()\" because \"edificioArray\" is null"));
    }
}
