package org.example.api.implementation.interfaces;

import org.example.collections.implementation.ArrayUnorderedList;

/**
 * Interface que representa o resultado de uma simulação.
 *
 * <p>
 * Esta interface define os métodos necessários para acessar e modificar os
 * dados associados ao resultado de uma simulação, incluindo informações sobre o
 * trajeto percorrido, status, vida restante e outros detalhes relevantes.
 * </p>
 */
public interface ResultadoSimulacao {

    /**
     * Obtém o identificador único do resultado da simulação.
     *
     * @return O identificador único da simulação.
     */
    String getId();

    /**
     * Define o identificador único do resultado da simulação.
     *
     * @param id O identificador único da simulação.
     */
    void setId(String id);

    /**
     * Obtém o nome da divisão inicial onde a simulação começou.
     *
     * @return O nome da divisão inicial.
     */
    String getDivisaoInicial();

    /**
     * Define o nome da divisão inicial onde a simulação começou.
     *
     * @param divisaoInicial O nome da divisão inicial.
     */
    void setDivisaoInicial(String divisaoInicial);

    /**
     * Obtém o nome da divisão final onde a simulação terminou.
     *
     * @return O nome da divisão final.
     */
    String getDivisaoFinal();

    /**
     * Define o nome da divisão final onde a simulação terminou.
     *
     * @param divisaoFinal O nome da divisão final.
     */
    void setDivisaoFinal(String divisaoFinal);

    /**
     * Obtém o status da simulação, como "SUCESSO" ou "FALHA".
     *
     * @return O status da simulação.
     */
    String getStatus();

    /**
     * Define o status da simulação, como "SUCESSO" ou "FALHA".
     *
     * @param status O status da simulação.
     */
    void setStatus(String status);

    /**
     * Obtém a quantidade de vida restante ao final da simulação.
     *
     * @return O valor da vida restante.
     */
    int getVidaRestante();

    /**
     * Define a quantidade de vida restante ao final da simulação.
     *
     * @param vidaRestante O valor da vida restante.
     */
    void setVidaRestante(int vidaRestante);

    /**
     * Obtém o trajeto percorrido durante a simulação, representado como uma lista
     * de divisões.
     *
     * @return Uma lista com os nomes das divisões do trajeto.
     */
    ArrayUnorderedList<String> getTrajeto();

    /**
     * Define o trajeto percorrido durante a simulação.
     *
     * @param trajeto Uma lista com os nomes das divisões do trajeto.
     */
    void setTrajeto(ArrayUnorderedList<String> trajeto);

    /**
     * Obtém os nomes das divisões marcadas como entradas ou saídas no mapa da
     * simulação.
     *
     * @return Uma lista com os nomes das divisões de entrada/saída.
     */
    ArrayUnorderedList<String> getEntradasSaidas();

    /**
     * Define os nomes das divisões marcadas como entradas ou saídas no mapa da
     * simulação.
     *
     * @param entradasSaidas Uma lista com os nomes das divisões de entrada/saída.
     */
    void setEntradasSaidas(ArrayUnorderedList<String> entradasSaidas);

    /**
     * Obtém o código identificador da missão associada à simulação.
     *
     * @return O código da missão.
     */
    String getCodigoMissao();

    /**
     * Obtém a versão da missão associada à simulação.
     *
     * @return A versão da missão.
     */
    int getVersaoMissao();

    /**
     * Retorna uma representação em string do resultado da simulação.
     *
     * @return Uma string representando o resultado da simulação.
     */
    @Override
    String toString();
}
