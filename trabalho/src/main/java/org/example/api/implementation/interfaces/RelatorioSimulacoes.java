package org.example.api.implementation.interfaces;

/**
 * Interface para encapsular os relatórios das simulações.
 *
 * <p>
 * Esta interface fornece métodos para acessar relatórios formatados de
 * simulações automáticas e manuais.
 * Os relatórios apresentam informações detalhadas sobre os resultados das
 * simulações realizadas.
 * </p>
 */
public interface RelatorioSimulacoes {

    /**
     * Obtém o relatório formatado da simulação automática.
     *
     * @return Um objeto {@code ResultadoFormatado} contendo os detalhes do
     *         resultado
     *         da simulação automática.
     */
    ResultadoFormatado getSimulacaoAutomatica();

    /**
     * Obtém o relatório formatado da simulação manual.
     *
     * @return Um objeto {@code ResultadoFormatado} contendo os detalhes do
     *         resultado
     *         da simulação manual.
     */
    ResultadoFormatado getSimulacaoManual();
}
