package org.example.api.implementation.interfaces;

/**
 * Interface para encapsular os relatorios das simulacoes.
 *
 * <p>
 * Esta interface fornece metodos para acessar relatorios formatados de
 * simulacoes automaticas e manuais.
 * Os relatorios apresentam informacoes detalhadas sobre os resultados das
 * simulacoes realizadas.
 * </p>
 */
public interface IRelatorioSimulacoes {
    /**
     * Obtem o relatorio formatado da simulacao automatica.
     *
     * @return Um objeto {@code ResultadoFormatado} contendo os detalhes do
     *         resultado
     *         da simulacao automatica.
     */
    IResultadoFormatado getSimulacaoAutomatica();

    /**
     * Obtem o relatorio formatado da simulacao manual.
     *
     * @return Um objeto {@code ResultadoFormatado} contendo os detalhes do
     *         resultado
     *         da simulacao manual.
     */
    IResultadoFormatado getSimulacaoManual();
}
