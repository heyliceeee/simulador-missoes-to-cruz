package org.example.api.implementation.interfaces;

/**
 * Representa a interface para um inimigo no sistema.
 * 
 * <p>
 * A interface define os metodos basicos que devem ser implementados por uma
 * classe de inimigos, incluindo atributos como nome, poder e a capacidade
 * de sofrer dano.
 * </p>
 */
public interface IInimigo {

    /**
     * Obtem o nome do inimigo.
     *
     * @return O nome do inimigo.
     */
    String getNome();

    /**
     * Define o nome do inimigo.
     *
     * @param nome O novo nome do inimigo.
     * @throws IllegalArgumentException Se o nome fornecido for nulo ou vazio.
     */
    void setNome(String nome);

    /**
     * Obtem o poder do inimigo.
     *
     * @return O poder do inimigo.
     */
    int getPoder();

    /**
     * Define o poder do inimigo.
     *
     * @param poder O novo poder do inimigo.
     * @throws IllegalArgumentException Se o poder fornecido for negativo.
     */
    void setPoder(int poder);

    /**
     * Reduz o poder do inimigo com base no dano recebido.
     *
     * @param dano A quantidade de dano sofrido pelo inimigo.
     * @throws IllegalArgumentException Se o dano fornecido for negativo.
     */
    void sofrerDano(int dano);
}
