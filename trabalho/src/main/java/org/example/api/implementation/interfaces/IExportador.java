package org.example.api.implementation.interfaces;

import org.example.collections.implementation.ArrayUnorderedList;

/**
 * Interface para exportacao de resultados e relatorios de simulacoes.
 *
 * <p>
 * A interface define metodos para exportar os resultados de uma simulacao
 * para um arquivo JSON, alem de oferecer funcionalidades auxiliares, como
 * a filtragem de listas de strings.
 * </p>
 */
public interface IExportador {

    /**
     * Exporta o resultado de uma simulacao para um arquivo no formato JSON.
     *
     * @param resultado      O resultado da simulacao que sera exportado.
     * @param caminhoArquivo O caminho completo para o arquivo de destino onde os
     *                       dados serao salvos.
     * @param mapa           O mapa usado na simulacao, fornecendo informacoes
     *                       adicionais
     *                       para serem incluidas no arquivo exportado.
     * @throws IllegalArgumentException Se o resultado, caminhoArquivo ou mapa for
     *                                  nulo.
     */
    void exportarParaJson(IResultadoSimulacao resultado, String caminhoArquivo, IMapa mapa);

    /**
     * Exporta um relatorio detalhado das simulacoes automatica e manual.
     *
     * @param simulacaoAutomatica Resultado da simulacao automatica.
     * @param simulacaoManual     Resultado da simulacao manual.
     * @param mapa                O mapa usado nas simulacoes.
     * @param caminhoArquivo      Caminho do arquivo de destino para exportacao.
     */
    void exportarRelatorioSimulacoes(IResultadoSimulacao simulacaoAutomatica, IResultadoSimulacao simulacaoManual,
            IMapa mapa, String caminhoArquivo);
}
