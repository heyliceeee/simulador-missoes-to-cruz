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

    public ResultadoSimulacao(String id, String divisaoInicial, String divisaoFinal, String status, int vidaRestante, LinkedList<String> trajeto, LinkedList<String> entradasSaidas) {
        this.id = id;
        this.divisaoInicial = divisaoInicial;
        this.divisaoFinal = divisaoFinal;
        this.status = status;
        this.vidaRestante = vidaRestante;
        this.trajeto = trajeto;
        this.entradasSaidas = entradasSaidas;
    }

    public LinkedList<String> getEntradasSaidas() {
        return entradasSaidas;
    }

    public void setEntradasSaidas(LinkedList<String> entradasSaidas) {
        this.entradasSaidas = entradasSaidas;
    }

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
}
