package org.example.api.implementation.utils;

import org.example.api.implementation.interfaces.IRelatorioSimulacoes;
import org.example.api.implementation.interfaces.IResultadoFormatado;

/**
 * Implementacao de RelatorioSimulacoes.
 */
public class RelatorioSimulacoesImpl implements IRelatorioSimulacoes {
    private final IResultadoFormatado simulacaoAutomatica;
    private final IResultadoFormatado simulacaoManual;

    public RelatorioSimulacoesImpl(IResultadoFormatado simulacaoAutomatica, IResultadoFormatado simulacaoManual) {
        this.simulacaoAutomatica = simulacaoAutomatica;
        this.simulacaoManual = simulacaoManual;
    }

    @Override
    public IResultadoFormatado getSimulacaoAutomatica() {
        return simulacaoAutomatica;
    }

    @Override
    public IResultadoFormatado getSimulacaoManual() {
        return simulacaoManual;
    }
}
