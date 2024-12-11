package org.example.api.implementation.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.api.implementation.interfaces.IExportador;
import org.example.api.implementation.interfaces.IResultadoSimulacao;
import org.example.collections.implementation.ArrayUnorderedList;
import org.example.collections.implementation.LinkedList;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe utilitária para exportar os resultados das simulações e trajetos
 * usando Gson.
 */
public class ExportarResultados implements IExportador {

    /**
     * Exporta os resultados das simulações para um arquivo JSON usando Gson.
     *
     * @param resultados     Lista de resultados a serem exportados.
     * @param caminhoArquivo O caminho do arquivo para exportar.
     */
    @Override
    public void exportarParaJson(ArrayUnorderedList<IResultadoSimulacao> resultados, String caminhoArquivo) {
        ordenarResultados(resultados); // Ordena antes de exportar
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        IResultadoSimulacao[] arrayResultados = convertToArray(resultados);

        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            // Filtrar resultados para remover nulos
            ArrayUnorderedList<IResultadoSimulacao> resultadosFiltrados = new ArrayUnorderedList<>();
            for (int i = 0; i < resultados.size(); i++) {
                IResultadoSimulacao resultado = resultados.getElementAt(i);
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
     * Converte um LinkedList em um array.
     *
     * @param linkedList O LinkedList a ser convertido.
     * @return Um array contendo os elementos do LinkedList.
     */
    private static IResultadoSimulacao[] convertToArray(ArrayUnorderedList<IResultadoSimulacao> linkedList) {
        IResultadoSimulacao[] array = new IResultadoSimulacao[linkedList.size()];
        for (int i = 0; i < linkedList.size(); i++) {
            array[i] = linkedList.getElementAt(i);
        }
        return array;
    }

    /**
     * Ordenar de forma descrescente os resultados, com base nos pontos de vida
     * restantes
     *
     * @param resultados
     */
    public static void ordenarResultados(ArrayUnorderedList<IResultadoSimulacao> resultados) {
        // Ordena a lista com base nos pontos de vida restantes (maior para menor)
        for (int i = 0; i < resultados.size() - 1; i++) {
            for (int j = 0; j < resultados.size() - 1 - i; j++) {

                if (resultados.getElementAt(j).getVidaRestante() < resultados.getElementAt(j + 1).getVidaRestante()) {
                    IResultadoSimulacao temp = resultados.getElementAt(j);
                    resultados.setElementAt(j, resultados.getElementAt(j + 1));
                    resultados.setElementAt(j + 1, temp);
                }
            }
        }
    }
}
