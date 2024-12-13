package org.example.api.implementation.models;

import org.example.api.implementation.interfaces.IMapa;
import org.example.api.implementation.interfaces.IMissao;

/**
 * Implementacao de uma Missao.
 * Uma missao possui um codigo unico, uma versao e esta associada a um mapa.
 */
public class MissaoImpl implements IMissao {
    private String codMissao;
    private int versao;
    private IMapa mapa;

    /**
     * Construtor da Missao.
     *
     * @param codMissao Codigo unico da missao.
     * @param versao    Versao da missao.
     * @param mapa      Mapa associado a missao.
     * @throws IllegalArgumentException se o codigo da missao for nulo ou vazio,
     *                                  a versao for negativa ou o mapa for nulo.
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
     * Retorna o codigo unico da missao.
     *
     * @return O codigo da missao.
     */
    @Override
    public String getCodMissao() {
        return codMissao;
    }

    /**
     * Retorna a versao da missao.
     *
     * @return A versao da missao.
     */
    @Override
    public int getVersao() {
        return versao;
    }

    /**
     * Retorna o mapa associado a missao.
     *
     * @return O mapa associado.
     */
    @Override
    public IMapa getMapa() {
        return mapa;
    }

    /**
     * Valida o codigo da missao.
     *
     * @param codMissao Codigo da missao a ser validado.
     * @throws IllegalArgumentException se o codigo da missao for nulo ou vazio.
     */
    private void validarCodMissao(String codMissao) {
        if (codMissao == null || codMissao.trim().isEmpty()) {
            throw new IllegalArgumentException("O codigo da missao nao pode ser nulo ou vazio.");
        }
    }

    /**
     * Valida a versao da missao.
     *
     * @param versao Versao a ser validada.
     * @throws IllegalArgumentException se a versao for negativa.
     */
    private void validarVersao(int versao) {
        if (versao < 0) {
            throw new IllegalArgumentException("A versao da missao nao pode ser negativa.");
        }
    }

    /**
     * Valida o mapa associado a missao.
     *
     * @param mapa Mapa a ser validado.
     * @throws IllegalArgumentException se o mapa for nulo.
     */
    private void validarMapa(IMapa mapa) {
        if (mapa == null) {
            throw new IllegalArgumentException("O mapa da missao nao pode ser nulo.");
        }
    }

    /**
     * Retorna uma representacao em string da missao.
     *
     * @return Representacao da missao no formato string.
     */
    @Override
    public String toString() {
        return String.format("MissaoImpl{codMissao='%s', versao=%d, mapa=%s}",
                codMissao, versao, mapa != null ? "Definido" : "N/A");
    }

    /**
     * Compara esta missao com outro objeto.
     * Duas missoes sao iguais se possuem o mesmo codigo.
     *
     * @param o Objeto a ser comparado.
     * @return true se os objetos forem iguais; false caso contrario.
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
     * Retorna o hash code baseado no codigo da missao.
     *
     * @return Hash code da missao.
     */
    @Override
    public int hashCode() {
        return codMissao.hashCode();
    }
}
