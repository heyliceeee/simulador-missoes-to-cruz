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
     * @throws IllegalArgumentException se a divisão for nula ou o tipo for nulo ou
     *                                  vazio.
     */
    public AlvoImpl(Divisao divisao, String tipo) {
        if (divisao == null) {
            throw new IllegalArgumentException("A divisão do alvo não pode ser nula.");
        }
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("O tipo do alvo não pode ser nulo ou vazio.");
        }
        this.divisao = divisao;
        this.tipo = tipo;
    }

    /**
     * Obtém a divisão onde o alvo está localizado.
     *
     * @return Divisão do alvo.
     */
    @Override
    public Divisao getDivisao() {
        return divisao;
    }

    /**
     * Define a divisão do alvo.
     *
     * @param divisao Nova divisão do alvo.
     * @throws IllegalArgumentException se a divisão for nula.
     */
    @Override
    public void setDivisao(Divisao divisao) {
        if (divisao == null) {
            throw new IllegalArgumentException("A divisão do alvo não pode ser nula.");
        }
        this.divisao = divisao;
    }

    /**
     * Obtém o tipo do alvo.
     *
     * @return Tipo do alvo.
     */
    @Override
    public String getTipo() {
        return tipo;
    }

    /**
     * Define o tipo do alvo.
     *
     * @param tipo Novo tipo do alvo.
     * @throws IllegalArgumentException se o tipo for nulo ou vazio.
     */
    @Override
    public void setTipo(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("O tipo do alvo não pode ser nulo ou vazio.");
        }
        this.tipo = tipo;
    }

    /**
     * Retorna uma representação em string do alvo.
     *
     * @return String representando o alvo.
     */
    @Override
    public String toString() {
        return "Alvo{" +
                "divisao=" + divisao +
                ", tipo='" + tipo + '\'' +
                '}';
    }

    /**
     * Verifica a igualdade entre dois objetos do tipo AlvoImpl.
     *
     * @param o Objeto a ser comparado.
     * @return true se os objetos forem iguais, false caso contrário.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        AlvoImpl alvo = (AlvoImpl) o;

        if (!divisao.equals(alvo.divisao))
            return false;
        return tipo.equals(alvo.tipo);
    }

    /**
     * Retorna o hash code do objeto baseado na divisão e tipo.
     *
     * @return Hash code do objeto.
     */
    @Override
    public int hashCode() {
        int result = divisao.hashCode();
        result = 31 * result + tipo.hashCode();
        return result;
    }
}
