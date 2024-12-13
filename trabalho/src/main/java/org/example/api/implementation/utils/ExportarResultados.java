package org.example.api.implementation.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.api.implementation.interfaces.IExportador;
import org.example.api.implementation.interfaces.IMapa;
import org.example.api.implementation.interfaces.IResultadoSimulacao;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe para exportar os resultados das simulacoes em formato JSON.
 */
public class ExportarResultados implements IExportador {

    @Override
    public void exportarParaJson(IResultadoSimulacao resultado, String caminhoArquivo, IMapa mapa) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            ResultadoFormatadoImpl resultadoFormatado = new ResultadoFormatadoImpl(resultado, mapa);
            gson.toJson(resultadoFormatado, writer); // Serializa o resultado formatado para JSON
            System.out.println("Relatorio exportado com sucesso para: " + caminhoArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao exportar relatorio: " + e.getMessage());
        }
    }

    @Override
    public void exportarRelatorioSimulacoes(IResultadoSimulacao simulacaoAutomatica,
            IResultadoSimulacao simulacaoManual, IMapa mapa, String caminhoArquivo) {

    }
}
