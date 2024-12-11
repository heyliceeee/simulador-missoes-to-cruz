package org.example.api.implementation.models;

import org.example.api.implementation.interfaces.Mapa;
import org.example.api.implementation.interfaces.Missao;

public class MissaoImpl implements Missao {
    private String codMissao;
    private int versao;
    private Mapa mapa;

    public MissaoImpl(String codMissao, int versao, Mapa mapa) {
        this.codMissao = codMissao;
        this.versao = versao;
        this.mapa = mapa;
    }

    @Override
    public String getCodMissao() {
        return codMissao;
    }

    @Override
    public int getVersao() {
        return versao;
    }

    @Override
    public Mapa getMapa() {
        return mapa;
    }
}
