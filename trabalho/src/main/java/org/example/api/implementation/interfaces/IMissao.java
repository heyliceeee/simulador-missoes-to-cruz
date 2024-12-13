package org.example.api.implementation.interfaces;


/**
 * Representa uma missão no sistema.
 *
 * <p>
 * Uma missão é composta por um código único, uma versão e um mapa associado que
 * define o ambiente da missão.
 * </p>
 */
public interface IMissao {
    /**
     * Obtém o código da missão.
     *
     * @return O código único da missão.
     */
    String getCodMissao();

    /**
     * Obtém a versão da missão.
     *
     * @return A versão da missão, que identifica alterações ou revisões.
     */
    int getVersao();

    /**
     * Obtém o mapa associado à missão.
     *
     * @return O mapa que descreve as divisões, conexões, inimigos e itens da
     *         missão.
     */
    IMapa getMapa();
}
