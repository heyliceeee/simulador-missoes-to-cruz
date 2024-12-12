package org.example.api.implementation.interfaces;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.collections.implementation.ArrayUnorderedList;

/**
 * Interface para a simulacao manual de missoes.
 */
public interface ISimulacaoManual {

    /**
     * Executa a simulacao automatica ate o objetivo.
     *
     * @param divisaoObjetivo A divisao objetivo da missao.
     * @throws ElementNotFoundException Se algum elemento nao for encontrado durante
     *                                  a execucao.
     */
    void executar(IDivisao divisaoObjetivo) throws ElementNotFoundException;

    public int getVidaRestante();

    /**
     * Retorna o status da simulacao apos a execucao.
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
     * Obtem a lista de divisoes percorridas na simulacao.
     *
     * @return Lista de divisoes percorridas.
     */
    ArrayUnorderedList<IDivisao> getCaminhoPercorrido();

    /**
     * Obtem a lista de itens coletados durante a simulacao.
     *
     * @return Lista de itens coletados.
     */
    ArrayUnorderedList<IItem> getItensColetados();

    /**
     * Obtem a lista de inimigos derrotados durante a simulacao.
     *
     * @return Lista de inimigos derrotados.
     */
    ArrayUnorderedList<IInimigo> getInimigosDerrotados();

    /**
     * Converte o caminho percorrido em uma lista de nomes de divisoes.
     *
     * @return Lista de nomes das divisoes percorridas.
     */
    ArrayUnorderedList<String> getCaminhoPercorridoNomes();

}
