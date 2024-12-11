package org.example.api.implementation.interfaces;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.collections.implementation.ArrayUnorderedList;

/**
 * Interface para a simulacao automática de missões.
 */
public interface ISimulacaoAutomatica {

    /**
     * Executa a simulacao automática ate o objetivo.
     *
     * @param divisaoObjetivo A divisao objetivo da missao.
     * @throws ElementNotFoundException Se algum elemento nao for encontrado durante
     *                                  a execucao.
     */
    void executar(IDivisao divisaoObjetivo) throws ElementNotFoundException;

    public int getVidaRestante();

    /**
     * Retorna o status da simulacao após a execucao.
     *
     * @return "SUCESSO" ou "FALHA" com base no resultado.
     */
    String getStatus();

    /**
     * Obtem a divisao final onde o agente parou.
     *
     * @return Divisao final.
     */
    IDivisao getDivisaoFinal();

    /**
     * Obtem o caminho percorrido na simulacao.
     *
     * @return Lista de divisões percorridas.
     */
    ArrayUnorderedList<IDivisao> getCaminhoPercorrido();

    public ArrayUnorderedList<String> getCaminhoPercorridoNomes();

    /**
     * Obtem a lista de inimigos derrotados durante a simulacao.
     *
     * @return Lista de inimigos derrotados.
     */
    ArrayUnorderedList<IInimigo> getInimigosDerrotados();

    /**
     * Obtem a lista de itens coletados durante a simulacao.
     *
     * @return Lista de itens coletados.
     */
    ArrayUnorderedList<IItem> getItensColetados();
}
