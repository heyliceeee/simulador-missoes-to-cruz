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
 * Classe de testes para validar o comportamento da classe SimulacaoAutomaticaImpl.
 * Testa cenários como execução de simulação, movimentação de Tó Cruz, cálculo de trajeto e interações.
 */
public class SimulacaoAutomaticaTest {
    private IMapa mapa;
    private ToCruz toCruz;
    private SimulacaoAutomaticaImpl simulacao;

    /**
     * Configura o ambiente de teste antes de cada método.
     * Cria um mapa com divisões, conexões, inimigos e itens.
     */
    @BeforeEach
    void setUp() {
        mapa = new MapaImpl();
        toCruz = new ToCruz("To Cruz", 100); // Nome e vida inicial
        simulacao = new SimulacaoAutomaticaImpl(mapa, toCruz);

        // Criar divisões para testes
        mapa.adicionarDivisao("Entrada");
        mapa.adicionarDivisao("Objetivo");
        mapa.adicionarDivisao("Saida");

        // Conexões
        mapa.adicionarLigacao("Entrada", "Objetivo");
        mapa.adicionarLigacao("Objetivo", "Saida");

        // Adicionar inimigos e itens
        mapa.adicionarInimigo("Objetivo", new InimigoImpl("Inimigo1", 30));
        mapa.adicionarItem("Objetivo", new ItemImpl("kit de vida", 20));
        mapa.adicionarEntradaSaida("Entrada");
        mapa.adicionarEntradaSaida("Saida");
    }

    /**
     * Testa o cenário de execução da simulação com sucesso.
     * Verifica se o objetivo foi alcançado, os inimigos derrotados e os itens coletados.
     */
    @Test
    void testExecutarComSucesso() throws ElementNotFoundException {
        IDivisao divisaoObjetivo = mapa.getDivisaoPorNome("Objetivo");

        simulacao.executar(divisaoObjetivo);

        // Verificar se o objetivo foi alcançado
        assertEquals("Entrada", simulacao.getDivisaoFinal().getNomeDivisao());
        assertEquals("SUCESSO", simulacao.getStatus());
        assertTrue(simulacao.getVidaRestante() > 0);

        // Verificar se os inimigos foram derrotados e os itens coletados
        assertEquals(0, simulacao.getInimigosDerrotados().size());
        assertEquals(1, simulacao.getItensColetados().size());
    }

    /**
     * Testa o comportamento da simulação ao receber uma divisão de objetivo nula.
     * Verifica se a exceção correta é lançada.
     */
    @Test
    void testExecutarComDivisaoObjetivoNula() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> simulacao.executar(null));

        assertNotNull(exception);
        assertEquals("Erro: Divisao objetivo nao encontrada.", exception.getMessage());
    }

    /**
     * Testa o cálculo de vida restante durante um trajeto simulado.
     * Verifica se a vida restante é calculada corretamente.
     */
    @Test
    void testSimularTrajeto() {
        ArrayUnorderedList<IDivisao> caminhoParaObjetivo = mapa.calcularMelhorCaminho(
                mapa.getDivisaoPorNome("Entrada"),
                mapa.getDivisaoPorNome("Objetivo")
        );
        ArrayUnorderedList<IDivisao> caminhoDeVolta = mapa.calcularMelhorCaminho(
                mapa.getDivisaoPorNome("Objetivo"),
                mapa.getDivisaoPorNome("Saida")
        );

        int vidaRestante = simulacao.simularTrajeto(caminhoParaObjetivo, caminhoDeVolta);

        // Verificar se a vida restante é calculada corretamente
        assertEquals(80, vidaRestante);
    }

    /**
     * Testa o comportamento ao mover Tó Cruz para uma divisão válida.
     * Verifica se a movimentação foi bem-sucedida e se os inimigos e itens foram processados.
     */
    @Test
    void testMoverParaDivisao() throws ElementNotFoundException {
        IDivisao divisao = mapa.getDivisaoPorNome("Objetivo");

        simulacao.moverParaDivisao(divisao);

        // Verificar se Tó Cruz foi movido corretamente
        assertEquals(divisao, toCruz.getPosicaoAtual());

        // Verificar se os inimigos foram derrotados e os itens coletados
        assertEquals(0, simulacao.getInimigosDerrotados().size());
        assertEquals(1, simulacao.getItensColetados().size());
    }

    /**
     * Testa o comportamento ao mover Tó Cruz para uma divisão nula.
     * Verifica se a exceção correta é lançada.
     */
    @Test
    void testMoverParaDivisaoInvalida() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> simulacao.moverParaDivisao(null));

        assertNotNull(exception);
        assertEquals("Erro: Tentativa de mover para uma divisao nula.", exception.getMessage());
    }

    /**
     * Testa o cálculo do caminho mais curto para uma saída.
     * Verifica se o caminho encontrado está correto.
     */
    @Test
    void testEncontrarCaminhoParaSaidaMaisProxima() throws ElementNotFoundException {
        IDivisao divisaoObjetivo = mapa.getDivisaoPorNome("Objetivo");
        simulacao.executar(divisaoObjetivo);
        ArrayUnorderedList<IDivisao> caminho = simulacao.encontrarCaminhoParaSaidaMaisProxima();

        // Verificar se o caminho encontrado está correto
        assertNotNull(caminho);
        assertEquals(1, caminho.size());
        assertEquals("Entrada", caminho.getElementAt(0).getNomeDivisao());
    }

    /**
     * Testa o método de recuperação da vida restante.
     * Verifica se a vida inicial é retornada corretamente.
     */
    @Test
    void testGetVidaRestante() {
        assertEquals(100, simulacao.getVidaRestante());
    }

    /**
     * Testa o método de recuperação do status.
     * Verifica se o status inicial é retornado corretamente.
     */
    @Test
    void testGetStatus() {
        assertEquals("SUCESSO", simulacao.getStatus());
    }
}
