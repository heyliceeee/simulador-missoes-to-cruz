package org.example.api.implementation.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.api.implementation.interfaces.IExportador;
import org.example.api.implementation.interfaces.IMapa;
import org.example.api.implementation.interfaces.IResultadoSimulacao;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe para exportar os resultados das simulações em formato JSON.
 * <p>
 * Esta classe utiliza a biblioteca Gson para serializar objetos relacionados aos resultados
 * de simulações em um arquivo JSON formatado.
 * </p>
 *
 * @see IExportador
 */
public class ExportarResultados implements IExportador {

    /**
     * Exporta os resultados de uma simulação para um arquivo JSON.
     *
     * @param resultado      Os resultados da simulação a serem exportados.
     * @param caminhoArquivo O caminho do arquivo onde os resultados serão salvos.
     * @param mapa           O mapa associado à simulação.
     * @throws IllegalArgumentException se {@code resultado}, {@code caminhoArquivo}, ou {@code mapa} forem nulos.
     */
    @Override
    public void exportarParaJson(IResultadoSimulacao resultado, String caminhoArquivo, IMapa mapa) {
        if (resultado == null || caminhoArquivo == null || mapa == null) {
            throw new IllegalArgumentException("Parâmetros resultado, caminhoArquivo ou mapa não podem ser nulos.");
        }

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            // Criação de uma classe auxiliar para formatar o resultado.
            ResultadoFormatadoImpl resultadoFormatado = new ResultadoFormatadoImpl(resultado, mapa);
            gson.toJson(resultadoFormatado, writer); // Serializa o resultado formatado para JSON.
            System.out.println("Relatório exportado com sucesso para: " + caminhoArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao exportar relatório: " + e.getMessage());
        }
    }

    /**
     * Exporta relatórios comparativos de duas simulações (manual e automática) para um arquivo JSON.
     * <p>
     * Este método deve ser implementado para exportar os resultados das simulações manuais
     * e automáticas em um único relatório JSON.
     * </p>
     *
     * @param simulacaoAutomatica Os resultados da simulação automática.
     * @param simulacaoManual     Os resultados da simulação manual.
     * @param mapa                O mapa associado às simulações.
     * @param caminhoArquivo      O caminho do arquivo onde os relatórios serão salvos.
     * @throws UnsupportedOperationException Este método ainda não foi implementado.
     */
    @Override
    public void exportarRelatorioSimulacoes(IResultadoSimulacao simulacaoAutomatica,
                                            IResultadoSimulacao simulacaoManual,
                                            IMapa mapa, String caminhoArquivo) {
        throw new UnsupportedOperationException("Método exportarRelatorioSimulacoes ainda não foi implementado.");
    }
}
