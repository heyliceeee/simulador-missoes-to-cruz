package org.example.api.implementation.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.example.api.implementation.interfaces.Exportador;
import org.example.api.implementation.interfaces.ResultadoSimulacao;
import org.example.collections.implementation.ArrayUnorderedList;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe utilitária para exportar os resultados das simulações e trajetos usando Gson.
 */
public class ExportarResultados implements Exportador {

    /**
     * Exporta os resultados das simulações para um arquivo JSON usando Gson.
     *
     * @param resultados     Lista de resultados a serem exportados.
     * @param caminhoArquivo O caminho do arquivo para exportar.
     */
    @Override
public void exportarParaJson(ArrayUnorderedList<ResultadoSimulacao> resultados, String caminhoArquivo) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

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

}
