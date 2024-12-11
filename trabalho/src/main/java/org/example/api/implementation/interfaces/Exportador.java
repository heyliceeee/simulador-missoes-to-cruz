package org.example.api.implementation.interfaces;

import org.example.collections.implementation.ArrayUnorderedList;

/**
 * Interface para exportação de resultados e relatórios de simulações.
 */
public interface Exportador {

    /**
     * Exporta os resultados das simulações para um arquivo JSON.
     *
     * @param resultados     Lista de resultados a serem exportados.
     * @param caminhoArquivo Caminho do arquivo de destino para exportação.
     */
    void exportarParaJson(ArrayUnorderedList<ResultadoSimulacao> resultados, String caminhoArquivo);

    /**
     * Exporta um relatório detalhado das simulações automática e manual.
     *
     * @param simulacaoAutomatica Resultado da simulação automática.
     * @param simulacaoManual     Resultado da simulação manual.
     * @param mapa                O mapa usado nas simulações.
     * @param caminhoArquivo      Caminho do arquivo de destino para exportação.
     */
    void exportarRelatorioSimulacoes(ResultadoSimulacao simulacaoAutomatica, ResultadoSimulacao simulacaoManual, Mapa mapa, String caminhoArquivo);
}
