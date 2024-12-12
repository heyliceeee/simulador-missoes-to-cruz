package org.example.api.implementation.models;

import org.example.api.implementation.interfaces.Inimigo;

/**
 * Representa um inimigo no mapa.
 */
public class InimigoImpl implements Inimigo {
    private String nome;
    private int poder;

    /**
     * Construtor do Inimigo.
     *
     * @param nome  Nome do inimigo.
     * @param poder Poder do inimigo.
     * @throws IllegalArgumentException se o nome for nulo ou vazio, ou se o poder for negativo.
     */
    public InimigoImpl(String nome, int poder) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do inimigo não pode ser nulo ou vazio.");
        }
        if (poder < 0) {
            throw new IllegalArgumentException("O poder do inimigo não pode ser negativo.");
        }
        this.nome = nome;
        this.poder = poder;
    }

    /**
     * Obtém o nome do inimigo.
     *
     * @return Nome do inimigo.
     */
    @Override
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do inimigo.
     *
     * @param nome Novo nome do inimigo.
     * @throws IllegalArgumentException se o nome for nulo ou vazio.
     */
    @Override
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do inimigo não pode ser nulo ou vazio.");
        }
        this.nome = nome;
    }

    /**
     * Obtém o poder atual do inimigo.
     *
     * @return Poder do inimigo.
     */
    @Override
    public int getPoder() {
        return poder;
    }

    /**
     * Define o poder do inimigo.
     *
     * @param poder Novo poder do inimigo.
     * @throws IllegalArgumentException se o poder for negativo.
     */
    @Override
    public void setPoder(int poder) {
        if (poder < 0) {
            throw new IllegalArgumentException("O poder do inimigo não pode ser negativo.");
        }
        this.poder = poder;
    }

    /**
     * Reduz o poder do inimigo ao sofrer dano.
     *
     * @param dano Quantidade de dano sofrido.
     * @throws IllegalArgumentException se o dano for negativo.
     */
    @Override
    public void sofrerDano(int dano) {
        if (dano < 0) {
            throw new IllegalArgumentException("O dano não pode ser negativo.");
        }
        this.poder = Math.max(0, this.poder - dano); // Garante que o poder não fique negativo
    }

    /**
     * Retorna uma representação em string do inimigo.
     *
     * @return String representando o inimigo.
     */
    @Override
    public String toString() {
        return "InimigoImpl{" +
                "nome='" + nome + '\'' +
                ", poder=" + poder +
                '}';
    }

    /**
     * Verifica a igualdade entre dois objetos do tipo InimigoImpl.
     *
     * @param o Objeto a ser comparado.
     * @return true se os objetos forem iguais, false caso contrário.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InimigoImpl inimigo = (InimigoImpl) o;

        return nome.equals(inimigo.nome);
    }

    /**
     * Retorna o hash code do objeto baseado no nome.
     *
     * @return Hash code do objeto.
     */
    @Override
    public int hashCode() {
        return nome.hashCode();
    }
}
