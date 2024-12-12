package org.example.api.implementation.interfaces;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.collections.implementation.ArrayUnorderedList;

/**
 * Interface para a simulação manual de missões.
 *
 * <p>
 * Define os métodos necessários para gerenciar e executar uma simulação manual,
 * permitindo monitorar o progresso, itens coletados e inimigos derrotados.
 * </p>
 */
public interface SimulacaoManual {

    /**
     * Executa a simulação manual até o objetivo especificado.
     *
     * @param divisaoObjetivo A divisão objetivo da missão.
     * @throws ElementNotFoundException Se algum elemento necessário não for
     *                                  encontrado durante a execução.
     */
    void executar(Divisao divisaoObjetivo) throws ElementNotFoundException;

    /**
     * Obtém a quantidade de vida restante do agente ao final da simulação.
     *
     * @return A vida restante do agente.
     */
    int getVidaRestante();

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
    Divisao getDivisaoFinal();

    /**
     * Obtém a lista de divisões percorridas durante a simulação.
     *
     * @return Uma lista com as divisões percorridas na simulação.
     */
    ArrayUnorderedList<Divisao> getCaminhoPercorrido();

    /**
     * Obtém a lista de itens coletados durante a simulação.
     *
     * @return Uma lista com os itens coletados.
     */
    ArrayUnorderedList<Item> getItensColetados();

    /**
     * Obtém a lista de inimigos derrotados durante a simulação.
     *
     * @return Uma lista com os inimigos derrotados.
     */
    ArrayUnorderedList<Inimigo> getInimigosDerrotados();

    /**
     * Converte o caminho percorrido em uma lista de nomes das divisões.
     *
     * @return Uma lista contendo os nomes das divisões percorridas.
     */
    ArrayUnorderedList<String> getCaminhoPercorridoNomes();
}
