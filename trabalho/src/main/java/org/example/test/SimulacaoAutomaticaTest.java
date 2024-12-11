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
        mapa.adicionarDivisao("Laboratório");
        mapa.adicionarLigacao("Heliporto", "Laboratório");
        mapa.definirAlvo("Laboratório", "quimico");

        toCruz.moverPara(mapa.getDivisaoPorNome("Heliporto"));
    }


    @Test
    public void testSimulacaoComSucesso() throws ElementNotFoundException {
        SimulacaoAutomaticaImpl simulacao = new SimulacaoAutomaticaImpl(mapa, toCruz);
        simulacao.executar(mapa.getDivisaoPorNome("Laboratório"));

        assertEquals("SUCESSO", simulacao.getStatus());
        assertEquals("Laboratório", simulacao.getDivisaoFinal().getNomeDivisao());
    }

    @Test
    public void testSimulacaoComInimigos() throws ElementNotFoundException {
        mapa.adicionarInimigo("Laboratório", new InimigoImpl("badguy", 30));

        SimulacaoAutomaticaImpl simulacao = new SimulacaoAutomaticaImpl(mapa, toCruz);
        simulacao.executar(mapa.getDivisaoPorNome("Laboratório"));

        assertEquals("SUCESSO", simulacao.getStatus());
        assertTrue(toCruz.getVida() < 100); // Tó Cruz perdeu vida no combate
    }
}
