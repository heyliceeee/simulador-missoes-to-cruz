package org.example.api.implementation.models;

/**
 * Classe auxiliar para representar uma conexão entre duas divisões.
 */
public class Ligacao {
    private Divisao divisao1;
    private Divisao divisao2;

    public Ligacao(Divisao divisao1, Divisao divisao2) {
        this.divisao1 = divisao1;
        this.divisao2 = divisao2;
    }

    public Divisao getDivisao1() {
        return divisao1;
    }

    public Divisao getDivisao2() {
        return divisao2;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Ligacao ligacao = (Ligacao) obj;
        return (divisao1.equals(ligacao.divisao1) && divisao2.equals(ligacao.divisao2)) ||
                (divisao1.equals(ligacao.divisao2) && divisao2.equals(ligacao.divisao1));
    }

    public boolean conecta(Divisao d1, Divisao d2) {
        return (divisao1.equals(d1) && divisao2.equals(d2)) ||
                (divisao1.equals(d2) && divisao2.equals(d1));
    }

    @Override
    public String toString() {
        return "Ligacao{" +
                "divisao1=" + divisao1 +
                ", divisao2=" + divisao2 +
                '}';
    }

    @Override
    public int hashCode() {
        return divisao1.hashCode() + divisao2.hashCode();
    }
}
