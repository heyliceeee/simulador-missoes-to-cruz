package org.example.api.implementation.models;


/**
 * Representa itens como kits de vida e coletes
 */
public class Item {

    /**
     * Tipo do item (ex.: "kit de vida", "colete")
     */
    private String tipo;

    /**
     * Pontos que o item adiciona (vida ou proteção)
     */
    private int pontos;


    /**
     * Construtor do Item.
     *
     * @param tipo Tipo do item.
     * @param pontos Pontos que o item adiciona.
     */
    public Item(String tipo, int pontos) {
        this.tipo = tipo;
        this.pontos = pontos;
    }



    /**
     * Obtém o tipo do item.
     *
     * @return Tipo do item.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Define o tipo do item.
     *
     * @param tipo Novo tipo do item.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Obtém os pontos que o item fornece.
     *
     * @return Pontos fornecidos pelo item.
     */
    public int getPontos() {
        return pontos;
    }

    /**
     * Define os pontos que o item fornece.
     *
     * @param pontos Novo valor dos pontos.
     */
    public void setValor(int pontos) {
        this.pontos = pontos;
    }


    @Override
    public String toString() {
        return "Item{" +
                "tipo='" + tipo + '\'' +
                ", pontos=" + pontos +
                '}';
    }
}
