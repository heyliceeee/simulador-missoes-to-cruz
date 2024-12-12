package org.example.api.implementation.interfaces;

import org.example.collections.implementation.ArrayUnorderedList;

/**
 * Interface para exportacao de resultados e relatorios de simulacoes.
 */
public interface IExportador {

    /**
     * Exporta os resultados das simulacoes para um arquivo JSON.
     *
     * @param resultados     Lista de resultados a serem exportados.
     * @param caminhoArquivo Caminho do arquivo de destino para exportacao.
     */
    void exportarParaJson(ArrayUnorderedList<IResultadoSimulacao> resultados, String caminhoArquivo);

    /**
     * Exporta um relatorio detalhado das simulacoes automatica e manual.
     *
     * @param simulacaoAutomatica Resultado da simulacao automatica.
     * @param simulacaoManual     Resultado da simulacao manual.
     * @param mapa                O mapa usado nas simulacoes.
     * @param caminhoArquivo      Caminho do arquivo de destino para exportacao.
     */
    void exportarRelatorioSimulacoes(IResultadoSimulacao simulacaoAutomatica, IResultadoSimulacao simulacaoManual, IMapa mapa, String caminhoArquivo);
}
