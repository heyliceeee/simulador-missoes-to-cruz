package org.example.api.implementation.models;

import org.example.api.implementation.interfaces.IDivisao;

/**
 * Classe auxiliar para armazenar uma divisao e seu predecessor durante a busca
 * de caminho.
 */
public class Predecessor {
    private final IDivisao atual;
    private final IDivisao predecessor;

    /**
     * Construtor da classe Predecessor.
     *
     * @param atual       A divisao atual.
     * @param predecessor A divisao predecessora.
     */
    public Predecessor(IDivisao atual, IDivisao predecessor) {
        this.atual = atual;
        this.predecessor = predecessor;
    }

    /**
     * Retorna a divisao atual.
     *
     * @return A divisao atual.
     */
    public IDivisao getAtual() {
        return atual;
    }

    /**
     * Retorna a divisao predecessora.
     *
     * @return A divisao predecessora.
     */
    public IDivisao getPredecessor() {
        return predecessor;
    }
}
