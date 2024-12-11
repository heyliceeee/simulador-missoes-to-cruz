package org.example.api.implementation.interfaces;

import org.example.collections.implementation.ArrayUnorderedList;

/**
 * Interface para exportar resultados de simulações.
 */
public interface IExportador {

    /**
     * Exporta os resultados das simulações para um arquivo JSON.
     *
     * @param resultados     Lista de resultados a serem exportados.
     * @param caminhoArquivo Caminho do arquivo para exportar.
     */
    void exportarParaJson(ArrayUnorderedList<IResultadoSimulacao> resultados, String caminhoArquivo);
}
