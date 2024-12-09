package org.example.test;

import org.example.api.implementation.models.Inimigo;
import org.example.api.implementation.models.Mapa;
import org.example.api.implementation.models.ToCruz;
import org.example.api.implementation.simulation.SimulacaoAutomatica;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SimulacaoAutomaticaTest {
    private Mapa mapa;
    private ToCruz toCruz;


    @Before
    public void setUp() {
        mapa = new Mapa();
        toCruz = new ToCruz("Tó Cruz", 100);

        mapa.adicionarDivisao("Heliporto");
        mapa.adicionarDivisao("Laboratório");
        mapa.adicionarLigacao("Heliporto", "Laboratório");
        mapa.definirAlvo("Laboratório", "quimico");

        toCruz.moverPara(mapa.getDivisaoPorNome("Heliporto"));
    }


    @Test
    public void testSimulacaoComSucesso() {
        SimulacaoAutomatica simulacao = new SimulacaoAutomatica(mapa, toCruz);
        simulacao.executar(mapa.getDivisaoPorNome("Laboratório"));

        assertEquals("SUCESSO", simulacao.getStatus());
        assertEquals("Laboratório", simulacao.getDivisaoFinal().getNomeDivisao());
    }

    @Test
    public void testSimulacaoComInimigos() {
        mapa.adicionarInimigo("Laboratório", new Inimigo("badguy", 30));

        SimulacaoAutomatica simulacao = new SimulacaoAutomatica(mapa, toCruz);
        simulacao.executar(mapa.getDivisaoPorNome("Laboratório"));

        assertEquals("SUCESSO", simulacao.getStatus());
        assertTrue(toCruz.getVida() < 100); // Tó Cruz perdeu vida no combate
    }
}
