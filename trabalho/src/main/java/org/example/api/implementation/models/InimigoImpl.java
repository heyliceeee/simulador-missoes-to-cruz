package org.example.api.implementation.models;

import org.example.api.implementation.interfaces.IInimigo;

/**
 * Representa um inimigo no mapa.
 */
public class InimigoImpl implements IInimigo {
    private String nome;
    private int poder;

    /**
     * Construtor do Inimigo.
     *
     * @param nome  Nome do inimigo.
     * @param poder Poder do inimigo.
     */
    public InimigoImpl(String nome, int poder) {
        this.nome = nome;
        this.poder = poder;
    }

    /**
     * Obtem o nome do inimigo.
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
     */
    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtem o poder atual do inimigo.
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
     */
    @Override
    public void setPoder(int poder) {
        this.poder = poder;
    }

    /**
     * Reduz o poder do inimigo ao sofrer dano.
     *
     * @param dano Quantidade de dano sofrido.
     */
    @Override
    public void sofrerDano(int dano) {
        this.poder -= dano;
        if (this.poder < 0) {
            this.poder = 0; // Evita que o poder seja negativo
        }
        System.out.println(this.nome + " sofreu " + dano + " de dano. Poder restante: " + this.poder);
    }

    @Override
    public String toString() {
        return "Inimigo{" +
                "nome='" + nome + '\'' +
                ", poder=" + poder +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        InimigoImpl inimigo = (InimigoImpl) o;

        return nome.equals(inimigo.nome);
    }

    @Override
    public int hashCode() {
        return nome.hashCode();
    }
}
