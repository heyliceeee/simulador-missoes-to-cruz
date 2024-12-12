package org.example.api.implementation.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.api.implementation.interfaces.*;
import org.example.collections.implementation.ArrayUnorderedList;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe utilitaria para exportar os resultados das simulacoes e trajetos
 * usando Gson.
 */
public class ExportarResultados implements IExportador {

    @Override
    public void exportarParaJson(ArrayUnorderedList<IResultadoSimulacao> resultados, String caminhoArquivo) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

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

    @Override
    public void exportarRelatorioSimulacoes(IResultadoSimulacao simulacaoAutomatica, IResultadoSimulacao simulacaoManual,
                                            IMapa mapa, String caminhoArquivo) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        IRelatorioSimulacoes relatorio = new RelatorioSimulacoesImpl(
                transformarResultadoSimulacao(simulacaoAutomatica, mapa),
                transformarResultadoSimulacao(simulacaoManual, mapa));

        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            gson.toJson(relatorio, writer); // Serializar o relatorio para JSON
            System.out.println("Relatorio de simulacoes exportado com sucesso para: " + caminhoArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao exportar o relatorio: " + e.getMessage());
        }
    }

    private IResultadoFormatado transformarResultadoSimulacao(IResultadoSimulacao resultado, IMapa mapa) {
        return new ResultadoFormatadoImpl(resultado, mapa);
    }
}
