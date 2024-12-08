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
}
