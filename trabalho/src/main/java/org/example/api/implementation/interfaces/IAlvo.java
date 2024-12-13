package org.example.api.implementation.interfaces;

/**
 * Interface que define as operacoes basicas para um alvo no sistema.
 *
 * <p>
 * Um alvo representa o objetivo principal de uma missao. Ele esta associado a
 * uma
 * divisao no mapa e tem um tipo que o caracteriza.
 * </p>
 */
public interface IAlvo {

    /**
     * Obtem a divisao onde o alvo esta localizado.
     *
     * @return A divisao associada ao alvo.
     */
    IDivisao getDivisao();

    /**
     * Define a divisao onde o alvo esta localizado.
     *
     * @param divisao A nova divisao associada ao alvo.
     * @throws IllegalArgumentException se a divisao for nula.
     */
    void setDivisao(IDivisao divisao);

    /**
     * Obtem o tipo do alvo.
     *
     * <p>
     * O tipo e uma string que descreve o objetivo, como "Resgate" ou "Eliminacao".
     * </p>
     *
     * @return O tipo do alvo.
     */
    String getTipo();

    /**
     * Define o tipo do alvo.
     *
     * @param tipo O novo tipo do alvo.
     * @throws IllegalArgumentException se o tipo for nulo ou vazio.
     */
    void setTipo(String tipo);
}
