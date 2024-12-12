package org.example.api.implementation.interfaces;

import org.example.collections.implementation.ArrayUnorderedList;

/**
 * Interface para exportação de resultados e relatórios de simulações.
 */
public interface Exportador {

    /**
     * Exporta o resultado de uma simulação para um arquivo JSON.
     *
     * @param resultado      O resultado da simulação.
     * @param caminhoArquivo O caminho completo para o arquivo de destino.
     * @param mapa           O mapa usado na simulação para vincular informações adicionais.
     */
    void exportarParaJson(ResultadoSimulacao resultado, String caminhoArquivo, Mapa mapa);

    /**
     * Filtra uma lista de strings para remover valores nulos.
     *
     * @param lista Lista original a ser filtrada.
     * @return Uma nova lista sem valores nulos.
     */
    default ArrayUnorderedList<String> filtrarLista(ArrayUnorderedList<String> lista) {
        ArrayUnorderedList<String> filtrada = new ArrayUnorderedList<>();
        for (int i = 0; i < lista.size(); i++) {
            String elemento = lista.getElementAt(i);
            if (elemento != null) {
                filtrada.addToRear(elemento);
            }
        }
        return filtrada;
    }
}
