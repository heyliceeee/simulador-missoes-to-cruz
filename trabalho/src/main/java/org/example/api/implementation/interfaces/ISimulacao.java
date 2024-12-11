package org.example.api.implementation.interfaces;

import org.example.collections.implementation.ArrayUnorderedList;

public interface ISimulacao {
    /**
     * Executa a simulacao.
     */
    void executar(IDivisao divisaoObjetivo);

    /**
     * Obtem a vida restante de Tó Cruz.
     *
     * @return A quantidade de vida restante.
     */
    int getVidaRestante();

    /**
     * Retorna o status da simulacao (SUCESSO ou FALHA).
     *
     * @return O status da simulacao.
     */
    String getStatus();

    /**
     * Obtem a divisao final onde Tó Cruz está localizado após a simulacao.
     *
     * @return A divisao final.
     */
    IDivisao getDivisaoFinal();

    /**
     * Obtem o caminho percorrido durante a simulacao.
     *
     * @return Uma lista de divisões representando o caminho percorrido.
     */
    ArrayUnorderedList<IDivisao> getCaminhoPercorrido();

    /**
     * Obtem os itens coletados durante a simulacao.
     *
     * @return Uma lista de itens coletados.
     */
    ArrayUnorderedList<IItem> getItensColetados();

    /**
     * Obtem os inimigos derrotados durante a simulacao.
     *
     * @return Uma lista de inimigos derrotados.
     */
    ArrayUnorderedList<IInimigo> getInimigosDerrotados();
}
