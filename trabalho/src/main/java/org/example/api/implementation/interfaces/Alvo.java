package org.example.api.implementation.interfaces;

/**
 * Interface que define as operações básicas para um alvo no sistema.
 *
 * <p>
 * Um alvo representa o objetivo principal de uma missão. Ele está associado a
 * uma
 * divisão no mapa e tem um tipo que o caracteriza.
 * </p>
 */
public interface Alvo {

    /**
     * Obtém a divisão onde o alvo está localizado.
     *
     * @return A divisão associada ao alvo.
     */
    Divisao getDivisao();

    /**
     * Define a divisão onde o alvo está localizado.
     *
     * @param divisao A nova divisão associada ao alvo.
     * @throws IllegalArgumentException se a divisão for nula.
     */
    void setDivisao(Divisao divisao);

    /**
     * Obtém o tipo do alvo.
     *
     * <p>
     * O tipo é uma string que descreve o objetivo, como "Resgate" ou "Eliminação".
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
