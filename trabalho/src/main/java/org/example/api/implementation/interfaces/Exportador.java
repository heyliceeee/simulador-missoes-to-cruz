package org.example.api.implementation.interfaces;

import org.example.collections.implementation.ArrayUnorderedList;

/**
 * Interface para exportar resultados de simulações.
 */
public interface Exportador {

    /**
     * Exporta os resultados das simulações para um arquivo JSON.
     *
     * @param resultados     Lista de resultados a serem exportados.
     * @param caminhoArquivo Caminho do arquivo para exportar.
     */
    void exportarParaJson(ArrayUnorderedList<ResultadoSimulacao> resultados, String caminhoArquivo);
}
