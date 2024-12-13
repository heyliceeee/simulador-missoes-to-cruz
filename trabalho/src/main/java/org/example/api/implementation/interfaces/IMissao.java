package org.example.api.implementation.interfaces;

/**
 * Representa uma missao no sistema.
 *
 * <p>
 * Uma missao e composta por um codigo unico, uma versao e um mapa associado que
 * define o ambiente da missao.
 * </p>
 */
public interface IMissao {
    /**
     * Obtem o codigo da missao.
     *
     * @return O codigo unico da missao.
     */
    String getCodMissao();

    /**
     * Obtem a versao da missao.
     *
     * @return A versao da missao, que identifica alteracoes ou revisoes.
     */
    int getVersao();

    /**
     * Obtem o mapa associado a missao.
     *
     * @return O mapa que descreve as divisoes, conexoes, inimigos e itens da
     *         missao.
     */
    IMapa getMapa();
}
