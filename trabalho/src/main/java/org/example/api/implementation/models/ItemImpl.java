package org.example.api.implementation.models;

import org.example.api.implementation.interfaces.IItem;

/**
 * Representa um item no mapa.
 */
public class ItemImpl implements IItem {
    private String tipo;
    private int pontos;
    private Divisao divisao;

    /**
     * Construtor do Item.
     *
     * @param tipo   Tipo do item.
     * @param pontos Pontos relacionados ao item.
     */
    public ItemImpl(String tipo, int pontos) {
        this.tipo = tipo;
        this.pontos = pontos;
    }

    @Override
    public String getTipo() {
        return tipo;
    }

    @Override
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public int getPontos() {
        return pontos;
    }

    @Override
    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    @Override
    public Divisao getDivisao() {
        return divisao;
    }

    @Override
    public void setDivisao(Divisao divisao) {
        this.divisao = divisao;
    }

    @Override
    public String toString() {
        return "Item{" +
                "tipo='" + tipo + '\'' +
                ", pontos=" + pontos +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        IItem item = (IItem) o;

        return tipo.equals(item.getTipo());
    }

    @Override
    public int hashCode() {
        return tipo != null ? tipo.hashCode() : 0;
    }
}
