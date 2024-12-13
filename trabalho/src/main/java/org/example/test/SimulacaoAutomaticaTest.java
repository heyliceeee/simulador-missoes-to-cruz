package org.example.test;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.implementation.interfaces.IDivisao;
import org.example.api.implementation.interfaces.IMapa;
import org.example.api.implementation.models.InimigoImpl;
import org.example.api.implementation.models.ItemImpl;
import org.example.api.implementation.models.MapaImpl;
import org.example.api.implementation.models.ToCruz;
import org.example.api.implementation.simulation.SimulacaoAutomaticaImpl;
import org.example.collections.implementation.ArrayUnorderedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de testes para validar o comportamento da classe
 * SimulacaoAutomaticaImpl.
 * Testa cenarios como execucao de simulacao, movimentacao de To Cruz, calculo
 * de trajeto e interacoes.
 */
public class SimulacaoAutomaticaTest {
    private IMapa mapa;
    private ToCruz toCruz;
    private SimulacaoAutomaticaImpl simulacao;

    /**
     * Configura o ambiente de teste antes de cada metodo.
     * Cria um mapa com divisoes, conexoes, inimigos e itens.
     */
    @BeforeEach
    void setUp() {
        mapa = new MapaImpl();
        toCruz = new ToCruz("To Cruz", 100); // Nome e vida inicial
        simulacao = new SimulacaoAutomaticaImpl(mapa, toCruz);

        // Criar divisoes para testes
        mapa.adicionarDivisao("Entrada");
        mapa.adicionarDivisao("Objetivo");
        mapa.adicionarDivisao("Saida");

        // Conexoes
        mapa.adicionarLigacao("Entrada", "Objetivo");
        mapa.adicionarLigacao("Objetivo", "Saida");

        // Adicionar inimigos e itens
        mapa.adicionarInimigo("Objetivo", new InimigoImpl("Inimigo1", 30));
        mapa.adicionarItem("Objetivo", new ItemImpl("kit de vida", 20));
        mapa.adicionarEntradaSaida("Entrada");
        mapa.adicionarEntradaSaida("Saida");
    }

    /**
     * Testa o cenario de execucao da simulacao com sucesso.
     * Verifica se o objetivo foi alcancado, os inimigos derrotados e os itens
     * coletados.
     */
    @Test
    void testExecutarComSucesso() throws ElementNotFoundException {
        IDivisao divisaoObjetivo = mapa.getDivisaoPorNome("Objetivo");

        simulacao.executar(divisaoObjetivo);

        // Verificar se o objetivo foi alcancado
        assertEquals("Entrada", simulacao.getDivisaoFinal().getNomeDivisao());
        assertEquals("SUCESSO", simulacao.getStatus());
        assertTrue(simulacao.getVidaRestante() > 0);

        // Verificar se os inimigos foram derrotados e os itens coletados
        assertEquals(0, simulacao.getInimigosDerrotados().size());
        assertEquals(1, simulacao.getItensColetados().size());
    }

    /**
     * Testa o comportamento da simulacao ao receber uma divisao de objetivo nula.
     * Verifica se a excecao correta e lancada.
     */
    @Test
    void testExecutarComDivisaoObjetivoNula() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> simulacao.executar(null));

        assertNotNull(exception);
        assertEquals("Erro: Divisao objetivo nao encontrada.", exception.getMessage());
    }

    /**
     * Testa o calculo de vida restante durante um trajeto simulado.
     * Verifica se a vida restante e calculada corretamente.
     */
    @Test
    void testSimularTrajeto() {
        ArrayUnorderedList<IDivisao> caminhoParaObjetivo = mapa.calcularMelhorCaminho(
                mapa.getDivisaoPorNome("Entrada"),
                mapa.getDivisaoPorNome("Objetivo"));
        ArrayUnorderedList<IDivisao> caminhoDeVolta = mapa.calcularMelhorCaminho(
                mapa.getDivisaoPorNome("Objetivo"),
                mapa.getDivisaoPorNome("Saida"));

        int vidaRestante = simulacao.simularTrajeto(caminhoParaObjetivo, caminhoDeVolta);

        // Verificar se a vida restante e calculada corretamente
        assertEquals(80, vidaRestante);
    }

    /**
     * Testa o comportamento ao mover To Cruz para uma divisao valida.
     * Verifica se a movimentacao foi bem-sucedida e se os inimigos e itens foram
     * processados.
     */
    @Test
    void testMoverParaDivisao() throws ElementNotFoundException {
        IDivisao divisao = mapa.getDivisaoPorNome("Objetivo");

        simulacao.moverParaDivisao(divisao);

        // Verificar se To Cruz foi movido corretamente
        assertEquals(divisao, toCruz.getPosicaoAtual());

        // Verificar se os inimigos foram derrotados e os itens coletados
        assertEquals(0, simulacao.getInimigosDerrotados().size());
        assertEquals(1, simulacao.getItensColetados().size());
    }

    /**
     * Testa o comportamento ao mover To Cruz para uma divisao nula.
     * Verifica se a excecao correta e lancada.
     */
    @Test
    void testMoverParaDivisaoInvalida() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> simulacao.moverParaDivisao(null));

        assertNotNull(exception);
        assertEquals("Erro: Tentativa de mover para uma divisao nula.", exception.getMessage());
    }

    /**
     * Testa o calculo do caminho mais curto para uma saida.
     * Verifica se o caminho encontrado esta correto.
     */
    @Test
    void testEncontrarCaminhoParaSaidaMaisProxima() throws ElementNotFoundException {
        IDivisao divisaoObjetivo = mapa.getDivisaoPorNome("Objetivo");
        simulacao.executar(divisaoObjetivo);
        ArrayUnorderedList<IDivisao> caminho = simulacao.encontrarCaminhoParaSaidaMaisProxima();

        // Verificar se o caminho encontrado esta correto
        assertNotNull(caminho);
        assertEquals(1, caminho.size());
        assertEquals("Entrada", caminho.getElementAt(0).getNomeDivisao());
    }

    /**
     * Testa o metodo de recuperacao da vida restante.
     * Verifica se a vida inicial e retornada corretamente.
     */
    @Test
    void testGetVidaRestante() {
        assertEquals(100, simulacao.getVidaRestante());
    }

    /**
     * Testa o metodo de recuperacao do status.
     * Verifica se o status inicial e retornado corretamente.
     */
    @Test
    void testGetStatus() {
        assertEquals("SUCESSO", simulacao.getStatus());
    }
}
