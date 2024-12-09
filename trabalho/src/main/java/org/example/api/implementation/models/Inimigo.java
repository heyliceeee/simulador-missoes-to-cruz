package org.example.api.implementation.models;

/**
 * Representa um inimigo no mapa.
 */
public class Inimigo {
    private String nome;
    private int poder;

    /**
     * Construtor do Inimigo.
     *
     * @param nome  Nome do inimigo.
     * @param poder Poder do inimigo.
     */
    public Inimigo(String nome, int poder) {
        this.nome = nome;
        this.poder = poder;
    }

    // Getter para o nome
    public String getNome() {
        return nome;
    }

    // Setter para o nome
    public void setNome(String nome) {
        this.nome = nome;
    }

    // Getter para o poder (Nota: 'Poder' com P maiúsculo)
    public int getPoder() {
        return poder;
    }

    // Setter para o poder
    public void setPoder(int poder) {
        this.poder = poder;
    }

    /**
     * Método para o inimigo sofrer dano.
     *
     * @param dano Quantidade de dano a ser sofrido.
     */
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Inimigo inimigo = (Inimigo) o;

        return nome.equals(inimigo.nome);
    }

    @Override
    public int hashCode() {
        return nome.hashCode();
    }
}
