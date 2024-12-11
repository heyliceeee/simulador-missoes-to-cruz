package org.example.api.implementation.interfaces;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.collections.implementation.ArrayUnorderedList;

/**
 * Interface para a simulação automática de missões.
 */
public interface ISimulacaoAutomatica {

    /**
     * Executa a simulação automática até o objetivo.
     *
     * @param divisaoObjetivo A divisão objetivo da missão.
     * @throws ElementNotFoundException Se algum elemento não for encontrado durante
     *                                  a execução.
     */
    void executar(IDivisao divisaoObjetivo) throws ElementNotFoundException;

    public int getVidaRestante();

    /**
     * Retorna o status da simulação após a execução.
     *
     * @return "SUCESSO" ou "FALHA" com base no resultado.
     */
    String getStatus();

    /**
     * Obtém a divisão final onde o agente parou.
     *
     * @return Divisão final.
     */
    IDivisao getDivisaoFinal();

    /**
     * Obtém o caminho percorrido na simulação.
     *
     * @return Lista de divisões percorridas.
     */
    ArrayUnorderedList<IDivisao> getCaminhoPercorrido();

    public ArrayUnorderedList<String> getCaminhoPercorridoNomes();

    /**
     * Obtém a lista de inimigos derrotados durante a simulação.
     *
     * @return Lista de inimigos derrotados.
     */
    ArrayUnorderedList<IInimigo> getInimigosDerrotados();

    /**
     * Obtém a lista de itens coletados durante a simulação.
     *
     * @return Lista de itens coletados.
     */
    ArrayUnorderedList<IItem> getItensColetados();
}
