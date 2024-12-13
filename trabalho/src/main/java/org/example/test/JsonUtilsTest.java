package org.example.test;

import org.example.api.implementation.models.MapaImpl;
import org.example.api.implementation.utils.ImportJsonImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class JsonUtilsTest {
    private final MapaImpl mapa = new MapaImpl();
    private final ImportJsonImpl jsonUtils = new ImportJsonImpl(mapa);

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

    @Test
    void testCarregarMapaComDadosValidos() {
        String jsonPath = "D:\\githubProjects\\simulador-missoes-to-cruz\\trabalho\\src\\main\\java\\org\\example\\test\\resources\\test_map.json";

        assertDoesNotThrow(() -> jsonUtils.carregarMapa(jsonPath));

        // Verifique se as divisos foram carregadas corretamente
        assertEquals(21, mapa.getDivisoes().size());
        assertEquals("Laboratorio", mapa.getAlvo().getDivisao().getNomeDivisao());
        assertEquals("quimico", mapa.getAlvo().getTipo());
    }

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
