package org.example.api.implementation.utils;

import org.example.api.implementation.models.ResultadoSimulacao;
import org.example.collections.implementation.LinkedList;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe utilitária para exportar os resultados das simulações.
 */
public class ExportarResultados {

    /**
     * Exporta os resultados das simulações para um arquivo JSON.
     *
     * @param resultados Lista de resultados a serem exportados.
     * @param caminhoArquivo O caminho do arquivo para exportar.
     */
    public static void exportarParaJson(LinkedList<ResultadoSimulacao> resultados, String caminhoArquivo) {
        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            writer.write("[\n");

            for (int i = 0; i < resultados.getSize(); i++) {
                ResultadoSimulacao resultado = resultados.getElementAt(i);
                writer.write(resultadoParaJson(resultado));

                // Adiciona uma vírgula entre os objetos, exceto no último
                if (i < resultados.getSize() - 1) {
                    writer.write(",\n");
                }
            }

            writer.write("\n]");
            System.out.println("Resultados exportados com sucesso para: " + caminhoArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao exportar resultados: " + e.getMessage());
        }
    }

    /**
     * Converte um objeto ResultadoSimulacao para uma representação JSON.
     *
     * @param resultado O resultado da simulação.
     * @return Uma string no formato JSON representando o resultado.
     */
    private static String resultadoParaJson(ResultadoSimulacao resultado) {
        return "  {\n" +
                "    \"id\": \"" + resultado.getId() + "\",\n" +
                "    \"divisaoInicial\": \"" + resultado.getDivisaoInicial() + "\",\n" +
                "    \"divisaoFinal\": \"" + resultado.getDivisaoFinal() + "\",\n" +
                "    \"status\": \"" + resultado.getStatus() + "\",\n" +
                "    \"vidaRestante\": " + resultado.getVidaRestante() + ",\n" +
                "    \"trajeto\": " + listaParaJson(resultado.getTrajeto()) + ",\n" +
                "    \"entradasSaidas\": " + listaParaJson(resultado.getEntradasSaidas()) + "\n" +
                "  }";
    }


    /**
     * Converte uma lista de divisões para uma representação JSON.
     *
     * @param trajeto Lista de divisões.
     * @return Uma string no formato JSON representando o trajeto.
     */
    private static String listaParaJson(LinkedList<String> trajeto) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < trajeto.getSize(); i++) {
            json.append("\"").append(trajeto.getElementAt(i)).append("\"");
            if (i < trajeto.getSize() - 1) {
                json.append(", ");
            }
        }
        json.append("]");
        return json.toString();
    }
}
