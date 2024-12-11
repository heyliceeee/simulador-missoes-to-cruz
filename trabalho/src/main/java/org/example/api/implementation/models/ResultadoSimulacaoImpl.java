package org.example.api.implementation.models;

import org.example.api.implementation.interfaces.ResultadoSimulacao;
import org.example.collections.implementation.ArrayUnorderedList;

/**
 * Representa o resultado de uma simulação.
 */
public class ResultadoSimulacaoImpl implements ResultadoSimulacao {
    private String id;
    private String divisaoInicial;
    private String divisaoFinal;
    private String status; // Exemplo: "SUCESSO" ou "FALHA"
    private int vidaRestante;
    private ArrayUnorderedList<String> trajeto;
    private ArrayUnorderedList<String> entradasSaidas;
    private String codigoMissao;
    private int versaoMissao;

    // Construtor completo
    public ResultadoSimulacaoImpl(String id, String divisaoInicial, String divisaoFinal, String status, int vidaRestante,
                                  ArrayUnorderedList<String> trajeto, ArrayUnorderedList<String> entradasSaidas,
                                  String codigoMissao, int versaoMissao) {
        this.id = id;
        this.divisaoInicial = divisaoInicial;
        this.divisaoFinal = divisaoFinal;
        this.status = status;
        this.vidaRestante = vidaRestante;
        this.trajeto = trajeto;
        this.entradasSaidas = entradasSaidas;
        this.codigoMissao = codigoMissao;
        this.versaoMissao = versaoMissao;
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDivisaoInicial() {
        return divisaoInicial;
    }

    public void setDivisaoInicial(String divisaoInicial) {
        this.divisaoInicial = divisaoInicial;
    }

    public String getDivisaoFinal() {
        return divisaoFinal;
    }

    public void setDivisaoFinal(String divisaoFinal) {
        this.divisaoFinal = divisaoFinal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getVidaRestante() {
        return vidaRestante;
    }

    public void setVidaRestante(int vidaRestante) {
        this.vidaRestante = vidaRestante;
    }

    public ArrayUnorderedList<String> getTrajeto() {
        return trajeto;
    }

    public void setTrajeto(ArrayUnorderedList<String> trajeto) {
        this.trajeto = trajeto;
    }

    public ArrayUnorderedList<String> getEntradasSaidas() {
        return entradasSaidas;
    }

    public void setEntradasSaidas(ArrayUnorderedList<String> entradasSaidas) {
        this.entradasSaidas = entradasSaidas;
    }

    public String getCodigoMissao() {
        return codigoMissao;
    }

    public void setCodigoMissao(String codigoMissao) {
        this.codigoMissao = codigoMissao;
    }

    public int getVersaoMissao() {
        return versaoMissao;
    }

    public void setVersaoMissao(int versaoMissao) {
        this.versaoMissao = versaoMissao;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ResultadoSimulacao {\n");
        sb.append("  Código Missão: ").append(codigoMissao).append(",\n");
        sb.append("  Versão Missão: ").append(versaoMissao).append(",\n");
        sb.append("  ID: ").append(id).append(",\n");
        sb.append("  Divisão Inicial: ").append(divisaoInicial).append(",\n");
        sb.append("  Divisão Final: ").append(divisaoFinal).append(",\n");
        sb.append("  Status: ").append(status).append(",\n");
        sb.append("  Vida Restante: ").append(vidaRestante).append(",\n");
        sb.append("  Trajeto: ").append(trajeto.toString()).append(",\n");
        sb.append("  Entradas/Saídas: ").append(entradasSaidas.toString()).append("\n");
        sb.append("}");
        return sb.toString();
    }
}
