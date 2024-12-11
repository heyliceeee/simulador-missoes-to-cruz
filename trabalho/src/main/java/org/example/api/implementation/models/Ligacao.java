package org.example.api.implementation.models;

import org.example.api.implementation.interfaces.IDivisao;

/**
 * Classe auxiliar para representar uma conexão entre duas divisões.
 */
public class Ligacao {
    private IDivisao divisao1;
    private IDivisao divisao2;

    public Ligacao(IDivisao divisao1, IDivisao divisao2) {
        this.divisao1 = divisao1;
        this.divisao2 = divisao2;
    }

    public IDivisao getDivisao1() {
        return divisao1;
    }

    public IDivisao getDivisao2() {
        return divisao2;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        Ligacao ligacao = (Ligacao) obj;
        return (divisao1.equals(ligacao.divisao1) && divisao2.equals(ligacao.divisao2)) ||
                (divisao1.equals(ligacao.divisao2) && divisao2.equals(ligacao.divisao1));
    }

    /**
     * Retorna true se as 2 divisoes tem ligacao, caso contrario false
     * 
     * @param d1 uma divisao
     * @param d2 outra divisao
     * @return true se as 2 divisoes tem ligacao
     */
    public boolean conecta(IDivisao d1, IDivisao d2) {
        return (divisao1.equals(d1) && divisao2.equals(d2)) ||
                (divisao1.equals(d2) && divisao2.equals(d1));
    }

    /**
     * Obter outra divisao ligada
     * 
     * @param divisao
     * @return
     */
    public IDivisao getOutraDivisao(IDivisao divisao) {
        if (divisao.equals(divisao1)) {
            return divisao2;
        } else if (divisao.equals(divisao2)) {
            return divisao1;
        }
        return null;
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
