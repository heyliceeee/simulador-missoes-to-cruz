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
 * Classe utilitaria para exportar os resultados das simulacoes e trajetos
 * usando Gson.
 */
public class ExportarResultados implements IExportador {

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

    @Override
    public void exportarRelatorioSimulacoes(ResultadoSimulacao simulacaoAutomatica, ResultadoSimulacao simulacaoManual,
            Mapa mapa, String caminhoArquivo) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        RelatorioSimulacoes relatorio = new RelatorioSimulacoesImpl(
                transformarResultadoSimulacao(simulacaoAutomatica, mapa),
                transformarResultadoSimulacao(simulacaoManual, mapa));

        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            gson.toJson(relatorio, writer); // Serializar o relatório para JSON
            System.out.println("Relatório de simulações exportado com sucesso para: " + caminhoArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao exportar o relatório: " + e.getMessage());
        }
    }

    private ResultadoFormatado transformarResultadoSimulacao(ResultadoSimulacao resultado, Mapa mapa) {
        return new ResultadoFormatadoImpl(resultado, mapa);
    }

}
