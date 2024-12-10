package org.example.api.implementation.models;

import org.example.api.implementation.interfaces.Alvo;
import org.example.api.implementation.interfaces.Divisao;

/**
 * Implementação do Alvo da missão.
 */
public class AlvoImpl implements Alvo {
    private Divisao divisao;
    private String tipo;

    /**
     * Construtor do Alvo.
     *
     * @param divisao Divisão onde o alvo está localizado.
     * @param tipo    Tipo do alvo.
     */
    public AlvoImpl(Divisao divisao, String tipo) {
        this.divisao = divisao;
        this.tipo = tipo;
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
    public String getTipo() {
        return tipo;
    }

    @Override
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Alvo{" +
                "divisao=" + divisao +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
