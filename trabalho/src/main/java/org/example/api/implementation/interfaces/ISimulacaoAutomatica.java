package org.example.api.implementation.interfaces;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.collections.implementation.ArrayUnorderedList;

/**
 * Interface para a simulacao automatica de missoes.
 *
 * <p>
 * Define os metodos necessarios para gerenciar e executar uma simulacao
 * automatica,
 * permitindo acompanhar o progresso, status, itens coletados e inimigos
 * derrotados.
 * </p>
 */
public interface ISimulacaoAutomatica {

    /**
     * Executa a simulacao automatica ate o objetivo especificado.
     *
     * @param divisaoObjetivo A divisao objetivo da missao.
     * @throws ElementNotFoundException Se algum elemento necessario nao for
     *                                  encontrado durante a execucao.
     */
    void executar(IDivisao divisaoObjetivo) throws ElementNotFoundException;

    /**
     * Obtem a quantidade de vida restante do agente ao final da simulacao.
     *
     * @return A vida restante do agente.
     */
    public int getVidaRestante();

    /**
     * Retorna o status da simulacao apos a execucao.
     *
     * @return Uma string indicando o resultado, como "SUCESSO" ou "FALHA".
     */
    String getStatus();

    /**
     * Obtem a divisao final onde o agente terminou a simulacao.
     *
     * @return A divisao final.
     */
    IDivisao getDivisaoFinal();

    /**
     * Obtem o caminho percorrido durante a simulacao, representado como uma lista
     * de divisoes.
     *
     * @return Uma lista com as divisoes percorridas na simulacao.
     */
    ArrayUnorderedList<IDivisao> getCaminhoPercorrido();

    /**
     * Obtem o caminho percorrido durante a simulacao, representado pelos nomes das
     * divisoes.
     *
     * @return Uma lista com os nomes das divisoes percorridas.
     */
    public ArrayUnorderedList<String> getCaminhoPercorridoNomes();

    /**
     * Obtem a lista de inimigos derrotados durante a simulacao.
     *
     * @return Uma lista com os inimigos derrotados.
     */
    ArrayUnorderedList<IInimigo> getInimigosDerrotados();

    /**
     * Obtem a lista de itens coletados durante a simulacao.
     *
     * @return Uma lista com os itens coletados.
     */
    ArrayUnorderedList<IItem> getItensColetados();
}
