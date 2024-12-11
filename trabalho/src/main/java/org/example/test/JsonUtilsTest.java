package org.example.test;

import org.example.api.exceptions.DivisionNotFoundException;
import org.example.api.exceptions.InvalidFieldException;
import org.example.api.exceptions.InvalidJsonStructureException;
import org.example.api.implementation.interfaces.IImportJson;
import org.example.api.implementation.interfaces.IMapa;
import org.example.api.implementation.models.MapaImpl;
import org.example.api.implementation.utils.ImportJsonImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class JsonUtilsTest {
    private IMapa mapa;
    private IImportJson jsonUtils;

    @BeforeEach
    public void setUp() {
        mapa = new MapaImpl();
        jsonUtils = new ImportJsonImpl(mapa);
    }

    @Test
    public void testCarregarMapaComSucesso() {
        String jsonPath = "D:\\githubProjects\\simulador-missoes-to-cruz\\trabalho\\src\\main\\java\\org\\example\\test\\resources\\test_map.json";

        assertDoesNotThrow(() -> jsonUtils.carregarMapa(jsonPath));

        // Verificar divisões
        assertEquals(21, mapa.getDivisoes().size());
        assertNotNull(mapa.getDivisaoPorNome("Heliporto"));
        assertNotNull(mapa.getDivisaoPorNome("Escada 6"));

        // Verificar conexões
        assertTrue(mapa.podeMover("Heliporto", "Escada 6"));

        // Verificar inimigos
        assertEquals(2, mapa.getDivisaoPorNome("Heliporto").getInimigosPresentes().size());

        // Verificar itens
        assertEquals(1, mapa.getDivisaoPorNome("WC").getItensPresentes().size());

        // Verificar alvo
        assertNotNull(mapa.getAlvo());
        assertEquals("Laboratorio", mapa.getAlvo().getDivisao().getNomeDivisao());
    }

    @Test
    public void testCarregarMapaComEstruturaInvalida() {
        String jsonPath = "D:\\githubProjects\\simulador-missoes-to-cruz\\trabalho\\src\\main\\java\\org\\example\\test\\resources\\invalid_structure.json";

        InvalidJsonStructureException exception = assertThrows(InvalidJsonStructureException.class, () -> jsonUtils.carregarMapa(jsonPath));

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Campo 'edificio' obrigatorio e deve ser uma lista."));
    }


    @Test
    public void testCarregarMapaComDivisaoInexistente() {
        String jsonPath = "D:\\githubProjects\\simulador-missoes-to-cruz\\trabalho\\src\\main\\java\\org\\example\\test\\resources\\missing_division.json"; // Crie um JSON onde uma ligação ou inimigo refere-se a uma divisão inexistente

        DivisionNotFoundException exception = assertThrows(DivisionNotFoundException.class, () -> jsonUtils.carregarMapa(jsonPath));

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Divisao de destino nao encontrada: Divisao3"));
    }

    @Test
    public void testCarregarMapaComCampoInvalido() {
        String jsonPath = "D:\\githubProjects\\simulador-missoes-to-cruz\\trabalho\\src\\main\\java\\org\\example\\test\\resources\\invalid_field.json"; // Crie um JSON com campos inválidos, como poder do inimigo como string

        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> jsonUtils.carregarMapa(jsonPath));

        assertNotNull(exception);

        System.out.println("EXCEPTION: "+exception);

        assertTrue(exception.getMessage().contains("Campo 'inimigo.poder' deve ser um numero inteiro valido"));
    }
}
