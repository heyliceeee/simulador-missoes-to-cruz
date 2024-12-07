package org.example.api.implementation.models;

/**
 * Representa um item no mapa.
 */
public class Item {
    private String tipo;
    private int pontos;

    /**
     * Construtor do Item.
     *
     * @param tipo   Tipo do item.
     * @param pontos Pontos relacionados ao item.
     */
    public Item(String tipo, int pontos) {
        this.tipo = tipo;
        this.pontos = pontos;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return tipo.equals(item.tipo);
    }

    @Override
    public int hashCode() {
        return tipo.hashCode();
    }
}
