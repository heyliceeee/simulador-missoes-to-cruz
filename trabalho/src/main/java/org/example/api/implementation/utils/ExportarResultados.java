package org.example.api.implementation.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.api.implementation.interfaces.Exportador;
import org.example.api.implementation.interfaces.Mapa;
import org.example.api.implementation.interfaces.RelatorioSimulacoes;
import org.example.api.implementation.interfaces.ResultadoFormatado;
import org.example.api.implementation.interfaces.ResultadoSimulacao;
import org.example.collections.implementation.ArrayUnorderedList;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe para exportar os resultados das simulações e trajetos usando Gson.
 */
public class ExportarResultados implements Exportador {

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
