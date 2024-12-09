package org.example.api.implementation.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.api.implementation.models.ResultadoSimulacao;
import org.example.collections.implementation.LinkedList;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe utilitária para exportar os resultados das simulações usando Gson.
 */
public class ExportarResultados {

    /**
     * Exporta os resultados das simulações para um arquivo JSON usando Gson.
     *
     * @param resultados     Lista de resultados a serem exportados.
     * @param caminhoArquivo O caminho do arquivo para exportar.
     */
    public static void exportarParaJson(LinkedList<ResultadoSimulacao> resultados, String caminhoArquivo) {
        ordenarResultados(resultados); // Ordena antes de exportar
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ResultadoSimulacao[] arrayResultados = convertToArray(resultados);

        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            gson.toJson(arrayResultados, writer); // Exporta o array diretamente
            System.out.println("Resultados exportados com sucesso para: " + caminhoArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao exportar resultados: " + e.getMessage());
        }
    }

    /**
     * Converte um LinkedList em um array.
     *
     * @param linkedList O LinkedList a ser convertido.
     * @return Um array contendo os elementos do LinkedList.
     */
    private static ResultadoSimulacao[] convertToArray(LinkedList<ResultadoSimulacao> linkedList) {
        ResultadoSimulacao[] array = new ResultadoSimulacao[linkedList.getSize()];
        for (int i = 0; i < linkedList.getSize(); i++) {
            array[i] = linkedList.getElementAt(i);
        }
        return array;
    }

    public static void ordenarResultados(LinkedList<ResultadoSimulacao> resultados) {
        // Ordena a lista com base nos pontos de vida restantes (maior para menor)
        for (int i = 0; i < resultados.getSize() - 1; i++) {
            for (int j = 0; j < resultados.getSize() - 1 - i; j++) {

                if (resultados.getElementAt(j).getVidaRestante() < resultados.getElementAt(j + 1).getVidaRestante()) {
                    ResultadoSimulacao temp = resultados.getElementAt(j);
                    resultados.setElementAt(j, resultados.getElementAt(j + 1));
                    resultados.setElementAt(j + 1, temp);
                }
            }
        }
    }
}
