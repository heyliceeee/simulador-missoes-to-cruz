package org.example.test;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.implementation.interfaces.IMapa;
import org.example.api.implementation.models.InimigoImpl;
import org.example.api.implementation.models.MapaImpl;
import org.example.api.implementation.models.ToCruz;
import org.example.api.implementation.simulation.SimulacaoAutomaticaImpl;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SimulacaoAutomaticaTest {
    private IMapa mapa;
    private ToCruz toCruz;


    @Before
    public void setUp() {
        mapa = new MapaImpl();
        toCruz = new ToCruz("To Cruz", 100);

        mapa.adicionarDivisao("Heliporto");
        mapa.adicionarDivisao("Laboratorio");
        mapa.adicionarLigacao("Heliporto", "Laboratorio");
        mapa.definirAlvo("Laboratorio", "quimico");

        toCruz.moverPara(mapa.getDivisaoPorNome("Heliporto"));
    }


    @Test
    public void testSimulacaoComSucesso() throws ElementNotFoundException {
        SimulacaoAutomaticaImpl simulacao = new SimulacaoAutomaticaImpl(mapa, toCruz);
        simulacao.executar(mapa.getDivisaoPorNome("Laboratorio"));

        assertEquals("SUCESSO", simulacao.getStatus());
        assertEquals("Laboratorio", simulacao.getDivisaoFinal().getNomeDivisao());
    }

    @Test
    public void testSimulacaoComInimigos() throws ElementNotFoundException {
        mapa.adicionarInimigo("Laboratorio", new InimigoImpl("badguy", 30));

        SimulacaoAutomaticaImpl simulacao = new SimulacaoAutomaticaImpl(mapa, toCruz);
        simulacao.executar(mapa.getDivisaoPorNome("Laboratorio"));

        assertEquals("SUCESSO", simulacao.getStatus());
        assertTrue(toCruz.getVida() < 100); // Tó Cruz perdeu vida no combate
    }
}
