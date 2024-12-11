package org.example.api.implementation.models;

import org.example.api.implementation.interfaces.IDivisao;

/**
 * Classe auxiliar para armazenar uma divisão e seu predecessor durante a busca
 * de caminho.
 */
public class Predecessor {
    private final IDivisao atual;
    private final IDivisao predecessor;

    /**
     * Construtor da classe Predecessor.
     *
     * @param atual       A divisão atual.
     * @param predecessor A divisão predecessora.
     */
    public Predecessor(IDivisao atual, IDivisao predecessor) {
        this.atual = atual;
        this.predecessor = predecessor;
    }

    /**
     * Retorna a divisão atual.
     *
     * @return A divisão atual.
     */
    public IDivisao getAtual() {
        return atual;
    }

    /**
     * Retorna a divisão predecessora.
     *
     * @return A divisão predecessora.
     */
    public IDivisao getPredecessor() {
        return predecessor;
    }
}
