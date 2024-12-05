package org.example.api.implementation.models;

public class Alvo {
    private Divisao divisao;
    private String tipo;

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
}