package org.example.api.implementation.interfaces;

/**
 * Interface para encapsular os relatórios das simulações.
 */
public interface RelatorioSimulacoes {

    ResultadoFormatado getSimulacaoAutomatica();

    ResultadoFormatado getSimulacaoManual();
}
