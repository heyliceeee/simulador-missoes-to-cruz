package org.example.api.implementation.utils;

import org.example.api.implementation.interfaces.RelatorioSimulacoes;
import org.example.api.implementation.interfaces.ResultadoFormatado;

/**
 * Implementação de RelatorioSimulacoes.
 */
public class RelatorioSimulacoesImpl implements RelatorioSimulacoes {
    private final ResultadoFormatado simulacaoAutomatica;
    private final ResultadoFormatado simulacaoManual;

    public RelatorioSimulacoesImpl(ResultadoFormatado simulacaoAutomatica, ResultadoFormatado simulacaoManual) {
        this.simulacaoAutomatica = simulacaoAutomatica;
        this.simulacaoManual = simulacaoManual;
    }

    @Override
    public ResultadoFormatado getSimulacaoAutomatica() {
        return simulacaoAutomatica;
    }

    @Override
    public ResultadoFormatado getSimulacaoManual() {
        return simulacaoManual;
    }
}
