package org.example.api.implementation.interfaces;

public interface IMissao {
    /**
     * Obtém o código da missão.
     *
     * @return O código da missão.
     */
    String getCodMissao();

    /**
     * Obtém a versão da missão.
     *
     * @return A versão da missão.
     */
    int getVersao();

    /**
     * Obtém o mapa associado à missão.
     *
     * @return O mapa da missão.
     */
    IMapa getMapa();
}
