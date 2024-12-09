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
     * Define o nome da divisão.
     *
     * @param nomeDivisao Nome da divisão a ser definida.
     */
    public void setNomeDivisao(String nomeDivisao) {
        this.nomeDivisao = nomeDivisao;
    }

    /**
     * Obtém os inimigos presentes na divisão.
     *
     * @return Lista de inimigos.
     */
    public LinkedList<Inimigo> getInimigosPresentes() {
        return inimigosPresentes;
    }

    /**
     * Define os inimigos presentes na divisão.
     *
     * @param inimigosPresentes Lista de inimigos.
     */
    public void setInimigosPresentes(LinkedList<Inimigo> inimigosPresentes) {
        this.inimigosPresentes = inimigosPresentes;
    }

    /**
     * Obtém os itens presentes na divisão.
     *
     * @return Lista de itens.
     */
    public LinkedList<Item> getItensPresentes() {
        return itensPresentes;
    }

    /**
     * Define os itens presentes na divisão.
     *
     * @param itensPresentes Lista de itens.
     */
    public void setItensPresentes(LinkedList<Item> itensPresentes) {
        this.itensPresentes = itensPresentes;
    }

    /**
     * Sobrescreve o método equals para comparar divisões com base no nome.
     *
     * @param obj Objeto a ser comparado.
     * @return true se os nomes forem iguais, false caso contrário.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Divisao divisao = (Divisao) obj;
        return nomeDivisao.equals(divisao.nomeDivisao);
    }

    /**
     * Sobrescreve o método hashCode para gerar hash baseado no nome da divisão.
     *
     * @return Hash code.
     */
    @Override
    public int hashCode() {
        return nomeDivisao.hashCode();
    }

    /**
     * Sobrescreve o método toString para representar a divisão pelo seu nome.
     *
     * @return Nome da divisão.
     */
    @Override
    public String toString() {
        return nomeDivisao;
    }


    /**
     * Verifica se ha um kit na divisao.
     * Um kit pode ser do tipo "kit de vida" ou "colete".
     *
     * @return true se há um kit, false caso contrario.
     */
    public boolean temKit() {
        for (int i = 0; i < itensPresentes.getSize(); i++) {
            Item item = itensPresentes.getElementAt(i);
            if (item.getTipo().equalsIgnoreCase("kit de vida") ||
                    item.getTipo().equalsIgnoreCase("colete")) {
                return true;
            }
        }
        return false;
    }
}
