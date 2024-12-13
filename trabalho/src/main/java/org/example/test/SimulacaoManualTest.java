package org.example.test;

import org.example.api.implementation.interfaces.IMapa;
import org.example.api.implementation.models.InimigoImpl;
import org.example.api.implementation.models.ItemImpl;
import org.example.api.implementation.models.MapaImpl;
import org.example.api.implementation.models.ToCruz;
import org.example.api.implementation.simulation.SimulacaoManualImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de testes para validar o comportamento da SimulacaoManualImpl.
 */
public class SimulacaoManualTest {
    private IMapa mapa;
    private ToCruz toCruz;
    private SimulacaoManualImpl simulacaoManual;

    /**
     * Configuração inicial antes de cada teste.
     */
    @BeforeEach
    void setUp() {
        mapa = new MapaImpl();
        toCruz = new ToCruz("To Cruz", 100); // Nome e vida inicial
        simulacaoManual = new SimulacaoManualImpl(mapa, toCruz);

        // Criar divisões para testes
        mapa.adicionarDivisao("Entrada");
        mapa.adicionarDivisao("Objetivo");
        mapa.adicionarDivisao("Saida");

        // Adicionar conexões
        mapa.adicionarLigacao("Entrada", "Objetivo");
        mapa.adicionarLigacao("Objetivo", "Saida");

        // Adicionar inimigos e itens
        mapa.adicionarInimigo("Objetivo", new InimigoImpl("Inimigo1", 30));
        mapa.adicionarItem("Objetivo", new ItemImpl("kit de vida", 20));
        mapa.adicionarEntradaSaida("Entrada");
        mapa.adicionarEntradaSaida("Saida");
    }

    /**
     * Testa a recuperação da vida restante.
     */
    @Test
    void testGetVidaRestante() {
        assertEquals(100, simulacaoManual.getVidaRestante());
    }

    /**
     * Testa a recuperação do caminho percorrido pelo Tó Cruz.
     */
    @Test
    void testGetCaminhoPercorrido() {
        assertNotNull(simulacaoManual.getCaminhoPercorrido());
        assertTrue(simulacaoManual.getCaminhoPercorrido().isEmpty());
    }
}