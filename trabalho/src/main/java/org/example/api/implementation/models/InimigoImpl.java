package org.example.api.implementation.models;

import org.example.api.implementation.interfaces.IInimigo;

/**
 * Representa um inimigo no mapa.
 */
public class InimigoImpl implements IInimigo {
    private String nome;
    private int poder;
    private int vida = 100;

    /**
     * Construtor do Inimigo.
     *
     * @param nome  Nome do inimigo.
     * @param poder Poder do inimigo.
     * @throws IllegalArgumentException se o nome for nulo ou vazio, ou se o poder
     *                                  for negativo.
     */
    public InimigoImpl(String nome, int poder) {
        validarNome(nome);
        validarPoder(poder);
        this.nome = nome.trim();
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
     * Obtem a vida atual do inimigo
     * 
     * @return vida atual do inimigo
     */
    public int getVida() {
        return vida;
    }

    /**
     * Define a quantidade de vida atual do inimigo
     * 
     * @param vida quantidade de vida atual do inimigo
     */
    public void setVida(int vida) {
        this.vida = vida;
    }

    /**
     * Define o nome do inimigo.
     *
     * @param nome Novo nome do inimigo.
     * @throws IllegalArgumentException se o nome for nulo ou vazio.
     */
    @Override
    public void setNome(String nome) {
        validarNome(nome);
        this.nome = nome.trim();
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
     * @throws IllegalArgumentException se o poder for negativo.
     */
    @Override
    public void setPoder(int poder) {
        validarPoder(poder);
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
            throw new IllegalArgumentException("O dano nao pode ser negativo.");
        }
        this.poder = Math.max(0, this.poder - dano);
    }

    /**
     * Valida o nome fornecido.
     *
     * @param nome Nome a ser validado.
     * @throws IllegalArgumentException se o nome for nulo ou vazio.
     */
    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do inimigo nao pode ser nulo ou vazio.");
        }
    }

    /**
     * Valida o poder fornecido.
     *
     * @param poder Poder a ser validado.
     * @throws IllegalArgumentException se o poder for negativo.
     */
    private void validarPoder(int poder) {
        if (poder < 0) {
            throw new IllegalArgumentException("O poder do inimigo nao pode ser negativo.");
        }
    }

    /**
     * Retorna uma representacao em string do inimigo.
     *
     * @return String representando o inimigo.
     */
    @Override
    public String toString() {
        return String.format("InimigoImpl{nome='%s', poder=%d}", nome, poder);
    }

    /**
     * Verifica a igualdade entre dois objetos do tipo InimigoImpl.
     *
     * @param o Objeto a ser comparado.
     * @return true se os objetos forem iguais, false caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

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
