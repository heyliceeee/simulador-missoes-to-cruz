package org.example.api.implementation.models;

import org.example.api.implementation.interfaces.IDivisao;

/**
 * Classe auxiliar para armazenar uma divisão e seu predecessor durante a busca
 * de caminhos.
 */
public class Predecessor {
    private final IDivisao atual;
    private final IDivisao predecessor;

    /**
     * Construtor da classe Predecessor.
     *
     * @param atual       A divisão atual. Não pode ser nula.
     * @param predecessor A divisão predecessora. Pode ser nula (para o ponto de
     *                    partida).
     * @throws IllegalArgumentException se a divisão atual for nula.
     */
    public Predecessor(IDivisao atual, IDivisao predecessor) {
        if (atual == null) {
            throw new IllegalArgumentException("A divisão atual não pode ser nula.");
        }
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
     * @return A divisão predecessora, ou null se não houver predecessora.
     */
    public IDivisao getPredecessor() {
        return predecessor;
    }

    /**
     * Retorna uma representação em string do objeto Predecessor.
     *
     * @return String representando o Predecessor.
     */
    @Override
    public String toString() {
        return String.format("Predecessor{atual=%s, predecessor=%s}",
                atual.getNomeDivisao(),
                predecessor != null ? predecessor.getNomeDivisao() : "null");
    }

    /**
     * Verifica se este objeto é igual a outro.
     *
     * @param o Objeto a ser comparado.
     * @return true se os objetos forem iguais; caso contrário, false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Predecessor that = (Predecessor) o;

        if (!atual.equals(that.atual))
            return false;
        return predecessor != null ? predecessor.equals(that.predecessor) : that.predecessor == null;
    }

    /**
     * Retorna o hash code do objeto.
     *
     * @return O hash code calculado a partir das divisões atual e predecessora.
     */
    @Override
    public int hashCode() {
        int result = atual.hashCode();
        result = 31 * result + (predecessor != null ? predecessor.hashCode() : 0);
        return result;
    }
}
