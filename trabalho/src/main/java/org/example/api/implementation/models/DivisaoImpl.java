package org.example.api.implementation.models;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.implementation.interfaces.Divisao;
import org.example.api.implementation.interfaces.Inimigo;
import org.example.api.implementation.interfaces.Item;
import org.example.collections.implementation.ArrayUnorderedList;

/**
 * Representa uma divisão do edifício.
 */
public class DivisaoImpl implements Divisao {
    private final String nomeDivisao;
    private final ArrayUnorderedList<Inimigo> inimigosPresentes;
    private final ArrayUnorderedList<Item> itensPresentes;
    private boolean entradaSaida;

    /**
     * Construtor da Divisão.
     *
     * @param nomeDivisao Nome da divisão.
     * @throws IllegalArgumentException se o nome da divisão for nulo ou vazio.
     */
    public DivisaoImpl(String nomeDivisao) {
        if (nomeDivisao == null || nomeDivisao.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da divisão não pode ser nulo ou vazio.");
        }
        this.nomeDivisao = nomeDivisao.trim();
        this.inimigosPresentes = new ArrayUnorderedList<>();
        this.itensPresentes = new ArrayUnorderedList<>();
    }

    /**
     * Obtém o nome da divisão.
     *
     * @return Nome da divisão.
     */
    @Override
    public String getNomeDivisao() {
        return nomeDivisao;
    }

    /**
     * Adiciona um inimigo à divisão.
     *
     * @param inimigo O inimigo a ser adicionado.
     * @throws IllegalArgumentException se o inimigo for nulo.
     */
    @Override
    public void adicionarInimigo(Inimigo inimigo) {
        if (inimigo == null) {
            throw new IllegalArgumentException("O inimigo não pode ser nulo.");
        }
        inimigosPresentes.addToRear(inimigo);
    }

    /**
     * Remove um inimigo da divisão.
     *
     * @param inimigo O inimigo a ser removido.
     * @throws ElementNotFoundException se o inimigo não estiver presente na
     *                                  divisão.
     * @throws IllegalArgumentException se o inimigo for nulo.
     */
    @Override
    public void removerInimigo(Inimigo inimigo) throws ElementNotFoundException {
        if (inimigo == null) {
            throw new IllegalArgumentException("O inimigo não pode ser nulo.");
        }
        inimigosPresentes.remove(inimigo);
    }

    /**
     * Adiciona um item à divisão.
     *
     * @param item O item a ser adicionado.
     * @throws IllegalArgumentException se o item for nulo.
     */
    @Override
    public void adicionarItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("O item não pode ser nulo.");
        }
        itensPresentes.addToRear(item);
    }

    /**
     * Remove um item da divisão.
     *
     * @param item O item a ser removido.
     * @throws ElementNotFoundException se o item não estiver presente na divisão.
     * @throws IllegalArgumentException se o item for nulo.
     */
    @Override
    public void removerItem(Item item) throws ElementNotFoundException {
        if (item == null) {
            throw new IllegalArgumentException("O item não pode ser nulo.");
        }
        itensPresentes.remove(item);
    }

    /**
     * Verifica se a divisão é uma entrada ou saída.
     *
     * @return true se for entrada ou saída, false caso contrário.
     */
    @Override
    public boolean isEntradaSaida() {
        return entradaSaida;
    }

    /**
     * Define se a divisão é uma entrada ou saída.
     *
     * @param entradaSaida true para marcar como entrada/saída, false caso
     *                     contrário.
     */
    @Override
    public void setEntradaSaida(boolean entradaSaida) {
        this.entradaSaida = entradaSaida;
    }

    /**
     * Obtém a lista de inimigos presentes na divisão.
     *
     * @return Lista de inimigos presentes.
     */
    @Override
    public ArrayUnorderedList<Inimigo> getInimigosPresentes() {
        return inimigosPresentes;
    }

    /**
     * Obtém a lista de itens presentes na divisão.
     *
     * @return Lista de itens presentes.
     */
    @Override
    public ArrayUnorderedList<Item> getItensPresentes() {
        return itensPresentes;
    }

    /**
     * Representação textual da divisão.
     *
     * @return String representando a divisão.
     */
    @Override
    public String toString() {
        return "DivisaoImpl{" +
                "nomeDivisao='" + nomeDivisao + '\'' +
                ", inimigosPresentes=" + inimigosPresentes.size() +
                ", itensPresentes=" + itensPresentes.size() +
                ", entradaSaida=" + entradaSaida +
                '}';
    }

    /**
     * Verifica a igualdade entre dois objetos do tipo DivisaoImpl.
     *
     * @param o Objeto a ser comparado.
     * @return true se os objetos forem iguais, false caso contrário.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        DivisaoImpl divisao = (DivisaoImpl) o;

        return nomeDivisao.equals(divisao.nomeDivisao);
    }

    /**
     * Retorna o hash code da divisão baseado no nome.
     *
     * @return Hash code da divisão.
     */
    @Override
    public int hashCode() {
        return nomeDivisao.hashCode();
    }
}
