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
     * Obtem o codigo da missao.
     *
     * @return O codigo da missao.
     */
    @Override
    public String getCodMissao() {
        return codMissao;
    }

    /**
     * Obtem a versao da missao.
     *
     * @return A versao da missao.
     */
    @Override
    public int getVersao() {
        return versao;
    }

    /**
     * Obtem o mapa associado à missao.
     *
     * @return O mapa da missao.
     */
    @Override
    public IMapa getMapa() {
        return mapa;
    }
}
