package org.example.api.implementation.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.example.api.implementation.interfaces.Exportador;
import org.example.api.implementation.interfaces.ResultadoSimulacao;
import org.example.collections.implementation.ArrayUnorderedList;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe utilitária para exportar os resultados das simulações e trajetos
 * usando Gson.
 */
public class ExportarResultados implements Exportador {

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
            // Filtrar resultados para remover nulos
            ArrayUnorderedList<ResultadoSimulacao> resultadosFiltrados = new ArrayUnorderedList<>();
            for (int i = 0; i < resultados.size(); i++) {
                ResultadoSimulacao resultado = resultados.getElementAt(i);
                if (resultado != null) {
                    resultadosFiltrados.addToRear(resultado);
                }
            }
            gson.toJson(resultadosFiltrados, writer); // Exporta a lista filtrada
            System.out.println("Resultados exportados com sucesso para: " + caminhoArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao exportar resultados: " + e.getMessage());
        }
    }

    /**
     * Ordenar de forma descrescente os resultados, com base nos pontos de vida
     * restantes
     *
     * @param resultados
     */
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
