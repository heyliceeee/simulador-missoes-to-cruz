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
public interface Exportador {

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
    void exportarParaJson(ResultadoSimulacao resultado, String caminhoArquivo, Mapa mapa);

    /**
     * Filtra uma lista de strings para remover valores nulos.
     *
     * @param lista A lista original a ser filtrada.
     * @return Uma nova lista contendo apenas os elementos não nulos da lista
     *         original.
     *         Se a lista original estiver vazia ou nula, retornará uma lista vazia.
     */
    default ArrayUnorderedList<String> filtrarLista(ArrayUnorderedList<String> lista) {
        ArrayUnorderedList<String> filtrada = new ArrayUnorderedList<>();
        if (lista != null) {
            for (int i = 0; i < lista.size(); i++) {
                String elemento = lista.getElementAt(i);
                if (elemento != null) {
                    filtrada.addToRear(elemento);
                }
            }
        }
        return filtrada;
    }
}
