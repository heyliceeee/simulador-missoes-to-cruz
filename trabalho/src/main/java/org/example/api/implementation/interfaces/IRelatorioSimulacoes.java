package org.example.api.implementation.interfaces;

/**
 * Interface para encapsular os relatorios das simulacoes.
 */
public interface IRelatorioSimulacoes {

    IResultadoFormatado getSimulacaoAutomatica();

    IResultadoFormatado getSimulacaoManual();
}
