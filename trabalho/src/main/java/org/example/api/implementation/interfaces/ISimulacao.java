package org.example.api.implementation.interfaces;

import org.example.collections.implementation.ArrayUnorderedList;

public interface ISimulacao {
    /**
     * Executa a simulação.
     */
    void executar(IDivisao divisaoObjetivo);

    /**
     * Obtém a vida restante de Tó Cruz.
     *
     * @return A quantidade de vida restante.
     */
    int getVidaRestante();

    /**
     * Retorna o status da simulação (SUCESSO ou FALHA).
     *
     * @return O status da simulação.
     */
    String getStatus();

    /**
     * Obtém a divisão final onde Tó Cruz está localizado após a simulação.
     *
     * @return A divisão final.
     */
    IDivisao getDivisaoFinal();

    /**
     * Obtém o caminho percorrido durante a simulação.
     *
     * @return Uma lista de divisões representando o caminho percorrido.
     */
    ArrayUnorderedList<IDivisao> getCaminhoPercorrido();

    /**
     * Obtém os itens coletados durante a simulação.
     *
     * @return Uma lista de itens coletados.
     */
    ArrayUnorderedList<IItem> getItensColetados();

    /**
     * Obtém os inimigos derrotados durante a simulação.
     *
     * @return Uma lista de inimigos derrotados.
     */
    ArrayUnorderedList<IInimigo> getInimigosDerrotados();
}
