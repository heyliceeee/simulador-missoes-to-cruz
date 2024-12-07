package org.example.api.implementation.models;

/**
 * Representa o alvo da missão.
 */
public class Alvo {
    private Divisao divisao;
    private String tipo;

    /**
     * Construtor do Alvo.
     *
     * @param divisao Divisão onde o alvo está localizado.
     * @param tipo    Tipo do alvo.
     */
    public Alvo(Divisao divisao, String tipo) {
        this.divisao = divisao;
        this.tipo = tipo;
    }

    public Divisao getDivisao() {
        return divisao;
    }

    public void setDivisao(Divisao divisao) {
        this.divisao = divisao;
    }

    public String getTipo() {
        return tipo;
    }

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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Alvo alvo = (Alvo) o;

        if (!divisao.equals(alvo.divisao)) return false;
        return tipo.equals(alvo.tipo);
    }

    @Override
    public int hashCode() {
        int result = divisao.hashCode();
        result = 31 * result + tipo.hashCode();
        return result;
    }
}
