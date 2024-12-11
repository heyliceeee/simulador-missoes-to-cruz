package org.example.api.implementation.models;

import org.example.api.implementation.interfaces.Item;

/**
 * Representa um item no mapa.
 */
public class ItemImpl implements Item {
    private String tipo;
    private int pontos;

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
    public String toString() {
        return "ItemImpl{" +
                "tipo='" + tipo + '\'' +
                ", pontos=" + pontos +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return tipo.equals(item.getTipo());
    }

    @Override
    public int hashCode() {
        return tipo != null ? tipo.hashCode() : 0;
    }
}
