package org.example.api.implementation.interfaces;

public interface IMissao {
    /**
     * Obtem o codigo da missao.
     *
     * @return O codigo da missao.
     */
    String getCodMissao();

    /**
     * Obtem a versao da missao.
     *
     * @return A versao da missao.
     */
    int getVersao();

    /**
     * Obtem o mapa associado à missao.
     *
     * @return O mapa da missao.
     */
    IMapa getMapa();
}
