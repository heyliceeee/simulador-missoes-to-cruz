package org.example.api.implementation.models;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.implementation.interfaces.IDivisao;
import org.example.api.implementation.interfaces.IInimigo;
import org.example.api.implementation.interfaces.IItem;
import org.example.collections.implementation.ArrayUnorderedList;

/**
 * Representa uma divisao do edificio.
 */
public class DivisaoImpl implements IDivisao {
    private final String nomeDivisao;
    private ArrayUnorderedList<IInimigo> inimigosPresentes;
    private ArrayUnorderedList<IItem> itensPresentes;
    private boolean entradaSaida;

    /**
     * Construtor da Divisao.
     *
     * @param nomeDivisao Nome da divisao.
     * @throws IllegalArgumentException se o nome da divisao for nulo ou vazio.
     */
    public DivisaoImpl(String nomeDivisao) {
        if (nomeDivisao == null || nomeDivisao.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da divisao nao pode ser nulo ou vazio.");
        }
        this.nomeDivisao = nomeDivisao.trim();
        this.inimigosPresentes = new ArrayUnorderedList<>();
        this.itensPresentes = new ArrayUnorderedList<>();
    }

    /**
     * Obtem o nome da divisao.
     *
     * @return Nome da divisao.
     */
    @Override
    public String getNomeDivisao() {
        return nomeDivisao;
    }

    @Override
    public void setNomeDivisao(String nomeDivisao) {

    }

    /**
     * Adiciona um inimigo a divisao.
     *
     * @param inimigo O inimigo a ser adicionado.
     * @throws IllegalArgumentException se o inimigo for nulo.
     */
    @Override
    public void adicionarInimigo(IInimigo inimigo) {
        if (inimigo == null) {
            throw new IllegalArgumentException("O inimigo nao pode ser nulo.");
        }
        inimigosPresentes.addToRear(inimigo);
    }

    /**
     * Remove um inimigo da divisao.
     *
     * @param inimigo O inimigo a ser removido.
     * @throws ElementNotFoundException se o inimigo nao estiver presente na
     *                                  divisao.
     * @throws IllegalArgumentException se o inimigo for nulo.
     */
    @Override
    public void removerInimigo(IInimigo inimigo) throws ElementNotFoundException {
        if (inimigo == null) {
            throw new IllegalArgumentException("O inimigo nao pode ser nulo.");
        }
        inimigosPresentes.remove(inimigo);
    }

    /**
     * Adiciona um item a divisao.
     *
     * @param item O item a ser adicionado.
     * @throws IllegalArgumentException se o item for nulo.
     */
    @Override
    public void adicionarItem(IItem item) {
        if (item == null) {
            throw new IllegalArgumentException("O item nao pode ser nulo.");
        }
        itensPresentes.addToRear(item);
    }

    /**
     * Remove um item da divisao.
     *
     * @param item O item a ser removido.
     * @throws ElementNotFoundException se o item nao estiver presente na divisao.
     * @throws IllegalArgumentException se o item for nulo.
     */
    @Override
    public void removerItem(IItem item) throws ElementNotFoundException {
        if (item == null) {
            throw new IllegalArgumentException("O item nao pode ser nulo.");
        }
        itensPresentes.remove(item);
    }

    /**
     * Verifica se a divisao e uma entrada ou saida.
     *
     * @return true se for entrada ou saida, false caso contrario.
     */
    @Override
    public boolean isEntradaSaida() {
        return entradaSaida;
    }

    /**
     * Define se a divisao e uma entrada ou saida.
     *
     * @param entradaSaida true para marcar como entrada/saida, false caso
     *                     contrario.
     */
    @Override
    public void setEntradaSaida(boolean entradaSaida) {
        this.entradaSaida = entradaSaida;
    }

    /**
     * Obtem a lista de inimigos presentes na divisao.
     *
     * @return Lista de inimigos presentes.
     */
    @Override
    public ArrayUnorderedList<IInimigo> getInimigosPresentes() {
        return inimigosPresentes;
    }

    /**
     * Obtem a lista de itens presentes na divisao.
     *
     * @return Lista de itens presentes.
     */
    @Override
    public void setInimigosPresentes(ArrayUnorderedList<IInimigo> inimigosPresentes) {
        this.inimigosPresentes = inimigosPresentes;
    }

    /**
     * Obtem os itens presentes na divisao.
     *
     * @return Lista de itens.
     */
    @Override
    public ArrayUnorderedList<IItem> getItensPresentes() {
        return itensPresentes;
    }

    /*
     * Define os itens presentes na divisao.
     *
     * @param itensPresentes Lista de itens.
     */
    @Override
    public void setItensPresentes(ArrayUnorderedList<IItem> itensPresentes) {
        this.itensPresentes = itensPresentes;
    }

    /**
     * Representacao textual da divisao.
     *
     * @return String representando a divisao.
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
     * @return true se os objetos forem iguais, false caso contrario.
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
     * Retorna o hash code da divisao baseado no nome.
     *
     * @return Hash code da divisao.
     */
    @Override
    public int hashCode() {
        return nomeDivisao.hashCode();
    }

    /**
     * Verifica se ha um kit na divisao.
     * Um kit pode ser do tipo "kit de vida" ou "colete".
     *
     * @return true se ha um kit, false caso contrario.
     */
    @Override
    public boolean temKit() {
        for (int i = 0; i < itensPresentes.size(); i++) {
            IItem item = itensPresentes.getElementAt(i);
            if (item.getTipo().equals("kit de vida") ||
                    item.getTipo().equals("colete")) {
                return true;
            }
        }
        return false;
    }
}
