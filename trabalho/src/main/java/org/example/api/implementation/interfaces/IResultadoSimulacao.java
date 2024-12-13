package org.example.api.implementation.interfaces;

import org.example.collections.implementation.ArrayUnorderedList;

/**
 * Interface que representa o resultado de uma simulacao.
 *
 * <p>
 * Esta interface define os metodos necessarios para acessar e modificar os
 * dados associados ao resultado de uma simulacao, incluindo informacoes sobre o
 * trajeto percorrido, status, vida restante e outros detalhes relevantes.
 * </p>
 */
public interface IResultadoSimulacao {

    /**
     * Obtem o identificador unico do resultado da simulacao.
     *
     * @return O identificador unico da simulacao.
     */
    String getId();

    /**
     * Define o identificador unico do resultado da simulacao.
     *
     * @param id O identificador unico da simulacao.
     */
    void setId(String id);

    /**
     * Obtem o nome da divisao inicial onde a simulacao comecou.
     *
     * @return O nome da divisao inicial.
     */
    String getDivisaoInicial();

    /**
     * Define o nome da divisao inicial onde a simulacao comecou.
     *
     * @param divisaoInicial O nome da divisao inicial.
     */
    void setDivisaoInicial(String divisaoInicial);

    /**
     * Obtem o nome da divisao final onde a simulacao terminou.
     *
     * @return O nome da divisao final.
     */
    String getDivisaoFinal();

    /**
     * Define o nome da divisao final onde a simulacao terminou.
     *
     * @param divisaoFinal O nome da divisao final.
     */
    void setDivisaoFinal(String divisaoFinal);

    /**
     * Obtem o status da simulacao, como "SUCESSO" ou "FALHA".
     *
     * @return O status da simulacao.
     */
    String getStatus();

    /**
     * Define o status da simulacao, como "SUCESSO" ou "FALHA".
     *
     * @param status O status da simulacao.
     */
    void setStatus(String status);

    /**
     * Obtem a quantidade de vida restante ao final da simulacao.
     *
     * @return O valor da vida restante.
     */
    int getVidaRestante();

    /**
     * Define a quantidade de vida restante ao final da simulacao.
     *
     * @param vidaRestante O valor da vida restante.
     */
    void setVidaRestante(int vidaRestante);

    /**
     * Obtem o trajeto percorrido durante a simulacao, representado como uma lista
     * de divisoes.
     *
     * @return Uma lista com os nomes das divisoes do trajeto.
     */
    ArrayUnorderedList<String> getTrajeto();

    /**
     * Define o trajeto percorrido durante a simulacao.
     *
     * @param trajeto Uma lista com os nomes das divisoes do trajeto.
     */
    void setTrajeto(ArrayUnorderedList<String> trajeto);

    /**
     * Obtem os nomes das divisoes marcadas como entradas ou saidas no mapa da
     * simulacao.
     *
     * @return Uma lista com os nomes das divisoes de entrada/saida.
     */
    ArrayUnorderedList<String> getEntradasSaidas();

    /**
     * Define os nomes das divisoes marcadas como entradas ou saidas no mapa da
     * simulacao.
     *
     * @param entradasSaidas Uma lista com os nomes das divisoes de entrada/saida.
     */
    void setEntradasSaidas(ArrayUnorderedList<String> entradasSaidas);

    /**
     * Obtem o codigo identificador da missao associada à simulacao.
     *
     * @return O codigo da missao.
     */
    String getCodigoMissao();

    /**
     * Obtem a versao da missao associada à simulacao.
     *
     * @return A versao da missao.
     */
    int getVersaoMissao();

    /**
     * Retorna uma representacao em string do resultado da simulacao.
     *
     * @return Uma string representando o resultado da simulacao.
     */
    @Override
    String toString();
}
