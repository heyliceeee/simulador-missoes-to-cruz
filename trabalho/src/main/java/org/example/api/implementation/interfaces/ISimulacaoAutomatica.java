package org.example.api.implementation.interfaces;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.collections.implementation.ArrayUnorderedList;

/**
 * Interface para a simulação automática de missões.
 *
 * <p>
 * Define os métodos necessários para gerenciar e executar uma simulação
 * automática,
 * permitindo acompanhar o progresso, status, itens coletados e inimigos
 * derrotados.
 * </p>
 */
public interface ISimulacaoAutomatica {

    /**
     * Executa a simulação automática até o objetivo especificado.
     *
     * @param divisaoObjetivo A divisão objetivo da missão.
     * @throws ElementNotFoundException Se algum elemento necessário não for
     *                                  encontrado durante a execução.
     */
    void executar(IDivisao divisaoObjetivo) throws ElementNotFoundException;

    /**
     * Obtém a quantidade de vida restante do agente ao final da simulação.
     *
     * @return A vida restante do agente.
     */
    public int getVidaRestante();

    /**
     * Retorna o status da simulação após a execução.
     *
     * @return Uma string indicando o resultado, como "SUCESSO" ou "FALHA".
     */
    String getStatus();

    /**
     * Obtém a divisão final onde o agente terminou a simulação.
     *
     * @return A divisão final.
     */
    IDivisao getDivisaoFinal();

    /**
     * Obtém o caminho percorrido durante a simulação, representado como uma lista
     * de divisões.
     *
     * @return Uma lista com as divisões percorridas na simulação.
     */
    ArrayUnorderedList<IDivisao> getCaminhoPercorrido();

    /**
     * Obtém o caminho percorrido durante a simulação, representado pelos nomes das
     * divisões.
     *
     * @return Uma lista com os nomes das divisões percorridas.
     */
    public ArrayUnorderedList<String> getCaminhoPercorridoNomes();

    /**
     * Obtém a lista de inimigos derrotados durante a simulação.
     *
     * @return Uma lista com os inimigos derrotados.
     */
    ArrayUnorderedList<IInimigo> getInimigosDerrotados();

    /**
     * Obtém a lista de itens coletados durante a simulação.
     *
     * @return Uma lista com os itens coletados.
     */
    ArrayUnorderedList<IItem> getItensColetados();
}
