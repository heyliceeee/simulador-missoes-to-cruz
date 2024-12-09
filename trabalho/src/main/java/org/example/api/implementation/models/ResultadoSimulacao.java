package org.example.api.implementation.models;

import org.example.collections.implementation.LinkedList;

/**
 * Representa o resultado de uma simulação.
 */
public class ResultadoSimulacao {
    private String id;
    private String divisaoInicial;
    private String divisaoFinal;
    private String status; // Exemplo: "SUCESSO" ou "FALHA"
    private int vidaRestante;
    private LinkedList<String> trajeto;
    private LinkedList<String> entradasSaidas;

    // Construtor completo
    public ResultadoSimulacao(String id, String divisaoInicial, String divisaoFinal, String status, int vidaRestante, LinkedList<String> trajeto, LinkedList<String> entradasSaidas) {
        this.id = id;
        this.divisaoInicial = divisaoInicial;
        this.divisaoFinal = divisaoFinal;
        this.status = status;
        this.vidaRestante = vidaRestante;
        this.trajeto = trajeto;
        this.entradasSaidas = entradasSaidas;
    }

    // Construtor padrão (opcional, mas recomendado para desserialização)
    public ResultadoSimulacao() {
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public String getDivisaoInicial() {
        return divisaoInicial;
    }

    public String getDivisaoFinal() {
        return divisaoFinal;
    }

    public String getStatus() {
        return status;
    }

    public int getVidaRestante() {
        return vidaRestante;
    }

    public LinkedList<String> getTrajeto() {
        return trajeto;
    }

    public LinkedList<String> getEntradasSaidas() {
        return entradasSaidas;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDivisaoInicial(String divisaoInicial) {
        this.divisaoInicial = divisaoInicial;
    }

    public void setDivisaoFinal(String divisaoFinal) {
        this.divisaoFinal = divisaoFinal;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setVidaRestante(int vidaRestante) {
        this.vidaRestante = vidaRestante;
    }

    public void setTrajeto(LinkedList<String> trajeto) {
        this.trajeto = trajeto;
    }

    public void setEntradasSaidas(LinkedList<String> entradasSaidas) {
        this.entradasSaidas = entradasSaidas;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ResultadoSimulacao {\n");
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
