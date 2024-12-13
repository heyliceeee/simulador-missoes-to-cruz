package org.example.api.implementation.models;

import org.example.api.implementation.interfaces.IMapa;
import org.example.api.implementation.interfaces.IMissao;

/**
 * Implementação de uma Missão.
 * Uma missão possui um código único, uma versão e está associada a um mapa.
 */
public class MissaoImpl implements IMissao {
    private String codMissao;
    private int versao;
    private IMapa mapa;

    /**
     * Construtor da Missão.
     *
     * @param codMissao Código único da missão.
     * @param versao    Versão da missão.
     * @param mapa      Mapa associado à missão.
     * @throws IllegalArgumentException se o código da missão for nulo ou vazio,
     *                                  a versão for negativa ou o mapa for nulo.
     */
    public MissaoImpl(String codMissao, int versao, IMapa mapa) {
        validarCodMissao(codMissao);
        validarVersao(versao);
        validarMapa(mapa);

        this.codMissao = codMissao.trim();
        this.versao = versao;
        this.mapa = mapa;
    }

    /**
     * Retorna o código único da missão.
     *
     * @return O código da missão.
     */
    @Override
    public String getCodMissao() {
        return codMissao;
    }

    /**
     * Retorna a versão da missão.
     *
     * @return A versão da missão.
     */
    @Override
    public int getVersao() {
        return versao;
    }

    /**
     * Retorna o mapa associado à missão.
     *
     * @return O mapa associado.
     */
    @Override
    public IMapa getMapa() {
        return mapa;
    }

    /**
     * Valida o código da missão.
     *
     * @param codMissao Código da missão a ser validado.
     * @throws IllegalArgumentException se o código da missão for nulo ou vazio.
     */
    private void validarCodMissao(String codMissao) {
        if (codMissao == null || codMissao.trim().isEmpty()) {
            throw new IllegalArgumentException("O código da missão não pode ser nulo ou vazio.");
        }
    }

    /**
     * Valida a versão da missão.
     *
     * @param versao Versão a ser validada.
     * @throws IllegalArgumentException se a versão for negativa.
     */
    private void validarVersao(int versao) {
        if (versao < 0) {
            throw new IllegalArgumentException("A versão da missão não pode ser negativa.");
        }
    }

    /**
     * Valida o mapa associado à missão.
     *
     * @param mapa Mapa a ser validado.
     * @throws IllegalArgumentException se o mapa for nulo.
     */
    private void validarMapa(IMapa mapa) {
        if (mapa == null) {
            throw new IllegalArgumentException("O mapa da missão não pode ser nulo.");
        }
    }

    /**
     * Retorna uma representação em string da missão.
     *
     * @return Representação da missão no formato string.
     */
    @Override
    public String toString() {
        return String.format("MissaoImpl{codMissao='%s', versao=%d, mapa=%s}",
                codMissao, versao, mapa != null ? "Definido" : "N/A");
    }

    /**
     * Compara esta missão com outro objeto.
     * Duas missões são iguais se possuem o mesmo código.
     *
     * @param o Objeto a ser comparado.
     * @return true se os objetos forem iguais; false caso contrário.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        MissaoImpl missao = (MissaoImpl) o;

        return codMissao.equals(missao.codMissao);
    }

    /**
     * Retorna o hash code baseado no código da missão.
     *
     * @return Hash code da missão.
     */
    @Override
    public int hashCode() {
        return codMissao.hashCode();
    }
}
