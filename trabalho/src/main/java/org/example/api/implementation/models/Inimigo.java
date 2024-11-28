package org.example.api.implementation.models;

/**
 * Representa um inimigo no jogo.
 */
public class Inimigo {

    /**
     * Nome do inimigo
     */
    private String nome;

    /**
     *  Pontos de vida do inimigo
     */
    private int vida;

    /**
     * Dano que o inimigo pode causar
     */
    private int dano;


    /**
     * Construtor do Inimigo.
     *
     * @param nome Nome do inimigo.
     * @param vida Pontos de vida do inimigo.
     * @param dano Poder de ataque do inimigo.
     */
    public Inimigo(String nome, int vida, int dano) {
        this.nome = nome;
        this.vida = vida;
        this.dano = dano;
    }

    /**
     * Realiza um ataque e retorna o dano causado.
     *
     * @return Dano causado pelo inimigo.
     */
    public int atacar() {
        return dano;
    }

    /**
     * Reduz a vida do inimigo ao sofrer dano.
     *
     * @param dano Dano sofrido.
     */
    public void sofrerDano(int dano) {
        vida -= dano;
        if (vida <= 0) {
            System.out.println(nome + " foi derrotado!");
        } else {
            System.out.println(nome + " sofreu dano! Vida restante: " + vida);
        }
    }

    /**
     * Obtém o nome do inimigo.
     *
     * @return Nome do inimigo.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do inimigo.
     *
     * @param nome Novo nome do inimigo.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém os pontos de vida do inimigo.
     *
     * @return Pontos de vida do inimigo.
     */
    public int getVida() {
        return vida;
    }

    /**
     * Define os pontos de vida do inimigo.
     *
     * @param vida Novo valor dos pontos de vida.
     */
    public void setVida(int vida) {
        this.vida = vida;
    }

    /**
     * Obtém o dano do inimigo.
     *
     * @return dano
     */
    public int getDano() {
        return dano;
    }

    /**
     * Define o dano do inimigo.
     *
     * @param dano Novo valor do poder de ataque.
     */
    public void setDano(int dano) {
        this.dano = dano;
    }

    @Override
    public String toString() {
        return "Inimigo{" +
                "nome='" + nome + '\'' +
                ", vida=" + vida +
                ", poderAtaque=" + dano +
                '}';
    }
}
