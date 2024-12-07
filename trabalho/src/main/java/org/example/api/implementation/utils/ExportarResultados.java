package org.example.api.implementation.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.api.implementation.models.ResultadoSimulacao;
import org.example.collections.implementation.LinkedList;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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
        ArrayList<ResultadoSimulacao> lista = new ArrayList<>();

        for (int i = 0; i < resultados.getSize(); i++) {
            lista.add(resultados.getElementAt(i));
        }

        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            gson.toJson(lista, writer);
            System.out.println("Resultados exportados com sucesso para: " + caminhoArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao exportar resultados: " + e.getMessage());
        }
    }
}
