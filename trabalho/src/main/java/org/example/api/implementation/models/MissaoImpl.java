package org.example.api.implementation.models;

import org.example.api.implementation.interfaces.IMapa;
import org.example.api.implementation.interfaces.IMissao;

public class MissaoImpl implements IMissao {
    private String codMissao;
    private int versao;
    private IMapa mapa;

    public MissaoImpl(String codMissao, int versao, IMapa mapa) {
        this.codMissao = codMissao;
        this.versao = versao;
        this.mapa = mapa;
    }

    /**
     * Obtém o código da missão.
     *
     * @return O código da missão.
     */
    @Override
    public String getCodMissao() {
        return codMissao;
    }

    /**
     * Obtém a versão da missão.
     *
     * @return A versão da missão.
     */
    @Override
    public int getVersao() {
        return versao;
    }

    /**
     * Obtém o mapa associado à missão.
     *
     * @return O mapa da missão.
     */
    @Override
    public IMapa getMapa() {
        return mapa;
    }
}
