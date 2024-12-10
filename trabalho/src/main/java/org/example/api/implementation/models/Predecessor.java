package org.example.api.implementation.models;

import org.example.api.implementation.interfaces.Divisao;

/**
 * Classe auxiliar para armazenar uma divisão e seu predecessor durante a busca de caminho.
 */
public class Predecessor {
    private final Divisao atual;
    private final Divisao predecessor;

    /**
     * Construtor da classe Predecessor.
     *
     * @param atual       A divisão atual.
     * @param predecessor A divisão predecessora.
     */
    public Predecessor(Divisao atual, Divisao predecessor) {
        this.atual = atual;
        this.predecessor = predecessor;
    }

    /**
     * Retorna a divisão atual.
     *
     * @return A divisão atual.
     */
    public Divisao getAtual() {
        return atual;
    }

    /**
     * Retorna a divisão predecessora.
     *
     * @return A divisão predecessora.
     */
    public Divisao getPredecessor() {
        return predecessor;
    }
}
