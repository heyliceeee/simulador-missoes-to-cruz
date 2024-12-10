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
    private String nomeDivisao;
    private ArrayUnorderedList<Inimigo> inimigosPresentes;
    private ArrayUnorderedList<Item> itensPresentes;
    private boolean entradaSaida;

    /**
     * Construtor da Divisão.
     *
     * @param nomeDivisao Nome da divisão.
     */
    public DivisaoImpl(String nomeDivisao) {
        this.nomeDivisao = nomeDivisao;
        this.inimigosPresentes = new ArrayUnorderedList<>();
        this.itensPresentes = new ArrayUnorderedList<>();
    }

    @Override
    public String getNomeDivisao() {
        return nomeDivisao;
    }

    @Override
    public void adicionarInimigo(Inimigo inimigo) {
        inimigosPresentes.addToRear(inimigo);
    }

    @Override
    public void removerInimigo(Inimigo inimigo) throws ElementNotFoundException {
        inimigosPresentes.remove(inimigo);
    }

    @Override
    public void adicionarItem(Item item) {
        itensPresentes.addToRear(item);
    }

    @Override
    public void removerItem(Item item) throws ElementNotFoundException {
        itensPresentes.remove(item);
    }

    @Override
    public boolean isEntradaSaida() {
        return entradaSaida;
    }

    @Override
    public void setEntradaSaida(boolean entradaSaida) {
        this.entradaSaida = entradaSaida;
    }

    @Override
    public ArrayUnorderedList<Inimigo> getInimigosPresentes() {
        return inimigosPresentes;
    }

    @Override
    public ArrayUnorderedList<Item> getItensPresentes() {
        return itensPresentes;
    }
}
