package org.example.api.implementation.models;

import org.example.api.implementation.interfaces.Divisao;
import org.example.api.implementation.interfaces.Item;

/**
 * Representa um item no mapa.
 */
public class ItemImpl implements Item {
    private String tipo;
    private int pontos;
    private Divisao divisao;

    /**
     * Construtor do Item.
     *
     * @param tipo   Tipo do item.
     * @param pontos Pontos relacionados ao item.
     * @throws IllegalArgumentException se o tipo for nulo ou vazio, ou se os pontos
     *                                  forem negativos.
     */
    public ItemImpl(String tipo, int pontos) {
        validarTipo(tipo);
        validarPontos(pontos);
        this.tipo = tipo.trim();
        this.pontos = pontos;
    }

    @Override
    public String getTipo() {
        return tipo;
    }

    @Override
    public void setTipo(String tipo) {
        validarTipo(tipo);
        this.tipo = tipo.trim();
    }

    @Override
    public int getPontos() {
        return pontos;
    }

    @Override
    public void setPontos(int pontos) {
        validarPontos(pontos);
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

    /**
     * Valida o tipo do item.
     *
     * @param tipo Tipo a ser validado.
     * @throws IllegalArgumentException se o tipo for nulo ou vazio.
     */
    private void validarTipo(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("O tipo do item não pode ser nulo ou vazio.");
        }
    }

    /**
     * Valida os pontos do item.
     *
     * @param pontos Pontos a serem validados.
     * @throws IllegalArgumentException se os pontos forem negativos.
     */
    private void validarPontos(int pontos) {
        if (pontos < 0) {
            throw new IllegalArgumentException("Os pontos do item não podem ser negativos.");
        }
    }

    @Override
    public String toString() {
        return String.format("ItemImpl{tipo='%s', pontos=%d, divisao=%s}",
                tipo, pontos, divisao != null ? divisao.getNomeDivisao() : "N/A");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Item item = (Item) o;

        return tipo.equalsIgnoreCase(item.getTipo());
    }

    @Override
    public int hashCode() {
        return tipo != null ? tipo.toLowerCase().hashCode() : 0;
    }
}
