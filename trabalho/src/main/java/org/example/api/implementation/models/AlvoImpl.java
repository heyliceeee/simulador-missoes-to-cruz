package org.example.api.implementation.models;

import org.example.api.implementation.interfaces.IAlvo;
import org.example.api.implementation.interfaces.IDivisao;

/**
 * Implementacao do Alvo da missao.
 */
public class AlvoImpl implements IAlvo {
    private IDivisao divisao;
    private String tipo;

    /**
     * Construtor do Alvo.
     *
     * @param divisao Divisao onde o alvo está localizado.
     * @param tipo    Tipo do alvo.
     */
    public AlvoImpl(IDivisao divisao, String tipo) {
        this.divisao = divisao;
        this.tipo = tipo;
    }

    @Override
    public IDivisao getDivisao() {
        return divisao;
    }

    @Override
    public void setDivisao(IDivisao divisao) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        IAlvo alvo = (IAlvo) o;

        if (!divisao.equals(alvo.getDivisao()))
            return false;
        return tipo.equals(alvo.getTipo());
    }

    @Override
    public int hashCode() {
        int result = divisao.hashCode();
        result = 31 * result + tipo.hashCode();
        return result;
    }
}
