package org.example.api.implementation.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.api.implementation.interfaces.Exportador;
import org.example.api.implementation.interfaces.Mapa;
import org.example.api.implementation.interfaces.ResultadoSimulacao;
import org.example.api.implementation.utils.ResultadoFormatadoImpl;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe para exportar os resultados das simulações em formato JSON.
 */
public class ExportarResultados implements IExportador {

    @Override
public void exportarParaJson(ResultadoSimulacao resultado, String caminhoArquivo, Mapa mapa) {
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    try (FileWriter writer = new FileWriter(caminhoArquivo)) {
        // Transformar o resultado em um formato adequado
        ResultadoFormatadoImpl resultadoFormatado = new ResultadoFormatadoImpl(resultado, mapa);
        gson.toJson(resultadoFormatado, writer); // Serializa o resultado formatado para JSON
        System.out.println("Relatório exportado com sucesso para: " + caminhoArquivo);
    } catch (IOException e) {
        System.err.println("Erro ao exportar relatório: " + e.getMessage());
    }
}

    /**
     * Prepara o objeto para exportação em JSON.
     */
    private Object formatarResultado(ResultadoFormatadoImpl resultado) {
        return new Object() {
            public final String id = resultado.getId();
            public final String divisaoInicial = resultado.getDivisaoInicial();
            public final String divisaoFinal = resultado.getDivisaoFinal();
            public final String status = resultado.getStatus();
            public final int vidaRestante = resultado.getVidaRestante();
            public final String[] trajeto = resultado.getTrajeto();
            public final String[] entradasSaidas = resultado.getEntradasSaidas();
            public final String codigoMissao = resultado.getCodigoMissao();
            public final int versaoMissao = resultado.getVersaoMissao();
        };
    }
}
