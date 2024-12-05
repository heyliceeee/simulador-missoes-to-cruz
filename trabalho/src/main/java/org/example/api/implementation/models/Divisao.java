package org.example.api.implementation.models;


import org.example.collections.implementation.LinkedList;

/**
 * Representa uma divisão do edifício.
 */
public class Divisao {

    private String nomeDivisao;
    private LinkedList<Inimigo> inimigosPresentes;
    private LinkedList<Item> itensPresentes;


    /**
     * Construtor da Divisão.
     *
     * @param nomeDivisao Nome da divisão.
     */
    public Divisao(String nomeDivisao) {
        this.nomeDivisao = nomeDivisao;
        this.inimigosPresentes = new LinkedList<>();
        this.itensPresentes = new LinkedList<>();
    }

    /**
     * Adiciona um inimigo à divisão.
     *
     * @param inimigo Inimigo a ser adicionado.
     */
    public void adicionarInimigo(Inimigo inimigo) {
        inimigosPresentes.add(inimigo);
    }

    /**
     * Remove um inimigo da divisão.
     *
     * @param inimigo Inimigo a ser removido.
     */
    public void removerInimigo(Inimigo inimigo) {
        inimigosPresentes.remove(inimigo);
    }

    /**
     * Adiciona um item à divisão.
     *
     * @param item Item a ser adicionado.
     */
    public void adicionarItem(Item item) {
        itensPresentes.add(item);
    }

    /**
     * Remove um item da divisão.
     *
     * @param item Item a ser removido.
     */
    public void removerItem(Item item) {
        itensPresentes.remove(item);
    }

    /**
     * Obtém o nome da divisão.
     *
     * @return Nome da divisão.
     */
    public String getNomeDivisao() {
        return nomeDivisao;
    }


    /**
     * Define o nome da divisao
     * @param nomeDivisao o nome da divisao a ser definida
     */
    public void setNomeDivisao(String nomeDivisao) {
        this.nomeDivisao = nomeDivisao;
    }

    /**
     * Obtem os inimigos presentes na divisao
     * @return os inimigos presentes na divisao
     */
    public LinkedList<Inimigo> getInimigosPresentes() {
        return inimigosPresentes;
    }

    /**
     * Define os inimigos presentes na divisao
     * @param inimigosPresentes os inimigos presentes
     */
    public void setInimigosPresentes(LinkedList<Inimigo> inimigosPresentes) {
        this.inimigosPresentes = inimigosPresentes;
    }

    public LinkedList<Item> getItensPresentes() {
        return itensPresentes;
    }

    public void setItensPresentes(LinkedList<Item> itensPresentes) {
        this.itensPresentes = itensPresentes;
    }
}
