package org.example.api.implementation.interfaces;

import org.example.collections.implementation.ArrayUnorderedList;

/**
 * Interface para exportação de resultados e relatórios de simulações.
 *
 * <p>
 * A interface define métodos para exportar os resultados de uma simulação
 * para um arquivo JSON, além de oferecer funcionalidades auxiliares, como
 * a filtragem de listas de strings.
 * </p>
 */
public interface IExportador {

    /**
     * Exporta o resultado de uma simulação para um arquivo no formato JSON.
     *
     * @param resultado      O resultado da simulação que será exportado.
     * @param caminhoArquivo O caminho completo para o arquivo de destino onde os
     *                       dados serão salvos.
     * @param mapa           O mapa usado na simulação, fornecendo informações
     *                       adicionais
     *                       para serem incluídas no arquivo exportado.
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
