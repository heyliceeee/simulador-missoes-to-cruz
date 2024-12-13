package org.example.api.implementation.models;

import org.example.api.implementation.interfaces.IResultadoSimulacao;
import org.example.collections.implementation.ArrayUnorderedList;

/**
 * Representa o resultado de uma simulacao.
 */
public class ResultadoSimulacaoImpl implements IResultadoSimulacao {
    private String id;
    private String divisaoInicial;
    private String divisaoFinal;
    private String status; // Exemplo: "SUCESSO" ou "FALHA"
    private int vidaRestante;
    private ArrayUnorderedList<String> trajeto;
    private ArrayUnorderedList<String> entradasSaidas;
    private String codigoMissao;
    private int versaoMissao;

    /**
     * Construtor completo.
     *
     * @param id             Identificador unico do resultado.
     * @param divisaoInicial Divisao inicial da simulacao.
     * @param divisaoFinal   Divisao final da simulacao.
     * @param status         Status da simulacao ("SUCESSO" ou "FALHA").
     * @param vidaRestante   Vida restante do personagem ao final da simulacao.
     * @param trajeto        Lista de divisoes percorridas na simulacao.
     * @param entradasSaidas Lista de divisoes marcadas como entrada/saida.
     * @param codigoMissao   Codigo identificador da missao.
     * @param versaoMissao   Versao da missao.
     * @throws IllegalArgumentException se algum parametro obrigatorio for nulo ou
     *                                  invalido.
     */
    public ResultadoSimulacaoImpl(String id, String divisaoInicial, String divisaoFinal, String status,
            int vidaRestante,
            ArrayUnorderedList<String> trajeto, ArrayUnorderedList<String> entradasSaidas,
            String codigoMissao, int versaoMissao) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID nao pode ser nulo ou vazio.");
        }
        if (divisaoInicial == null || divisaoInicial.trim().isEmpty()) {
            throw new IllegalArgumentException("Divisao inicial nao pode ser nula ou vazia.");
        }
        if (status == null || (!status.equalsIgnoreCase("SUCESSO") && !status.equalsIgnoreCase("FALHA"))) {
            throw new IllegalArgumentException("Status invalido. Deve ser 'SUCESSO' ou 'FALHA'.");
        }
        if (vidaRestante < 0) {
            throw new IllegalArgumentException("Vida restante nao pode ser negativa.");
        }

        this.id = id;
        this.divisaoInicial = divisaoInicial;
        this.divisaoFinal = divisaoFinal;
        this.status = status;
        this.vidaRestante = vidaRestante;
        this.trajeto = trajeto != null ? trajeto : new ArrayUnorderedList<>();
        this.entradasSaidas = entradasSaidas != null ? entradasSaidas : new ArrayUnorderedList<>();
        this.codigoMissao = codigoMissao;
        this.versaoMissao = versaoMissao;
    }

    /**
     * Obtem o identificador do resultado.
     *
     * @return ID do resultado.
     */
    public String getId() {
        return id;
    }

    /**
     * Define o identificador do resultado.
     *
     * @param id Novo ID.
     * @throws IllegalArgumentException se o ID for nulo ou vazio.
     */
    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID nao pode ser nulo ou vazio.");
        }
        this.id = id;
    }

    /**
     * Obtem a divisao inicial da simulacao.
     *
     * @return Divisao inicial.
     */
    public String getDivisaoInicial() {
        return divisaoInicial;
    }

    /**
     * Define a divisao inicial da simulacao.
     *
     * @param divisaoInicial Nova divisao inicial.
     * @throws IllegalArgumentException se a divisao inicial for nula ou vazia.
     */
    public void setDivisaoInicial(String divisaoInicial) {
        if (divisaoInicial == null || divisaoInicial.trim().isEmpty()) {
            throw new IllegalArgumentException("Divisao inicial nao pode ser nula ou vazia.");
        }
        this.divisaoInicial = divisaoInicial;
    }

    /**
     * Obtem a divisao final da simulacao.
     *
     * @return Divisao final.
     */
    public String getDivisaoFinal() {
        return divisaoFinal;
    }

    /**
     * Define a divisao final da simulacao.
     *
     * @param divisaoFinal Nova divisao final.
     */
    public void setDivisaoFinal(String divisaoFinal) {
        this.divisaoFinal = divisaoFinal;
    }

    /**
     * Obtem o status da simulacao.
     *
     * @return Status da simulacao.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Define o status da simulacao.
     *
     * @param status Novo status ("SUCESSO" ou "FALHA").
     * @throws IllegalArgumentException se o status for invalido.
     */
    public void setStatus(String status) {
        if (status == null || (!status.equalsIgnoreCase("SUCESSO") && !status.equalsIgnoreCase("FALHA"))) {
            throw new IllegalArgumentException("Status invalido. Deve ser 'SUCESSO' ou 'FALHA'.");
        }
        this.status = status;
    }

    /**
     * Obtem a vida restante ao final da simulacao.
     *
     * @return Vida restante.
     */
    public int getVidaRestante() {
        return vidaRestante;
    }

    /**
     * Define a vida restante ao final da simulacao.
     *
     * @param vidaRestante Nova vida restante.
     * @throws IllegalArgumentException se a vida restante for negativa.
     */
    public void setVidaRestante(int vidaRestante) {
        if (vidaRestante < 0) {
            throw new IllegalArgumentException("Vida restante nao pode ser negativa.");
        }
        this.vidaRestante = vidaRestante;
    }

    /**
     * Obtem a lista de divisoes percorridas na simulacao.
     *
     * @return Lista de divisoes percorridas.
     */
    public ArrayUnorderedList<String> getTrajeto() {
        return trajeto;
    }

    /**
     * Define a lista de divisoes percorridas na simulacao.
     *
     * @param trajeto Novo trajeto.
     */
    public void setTrajeto(ArrayUnorderedList<String> trajeto) {
        this.trajeto = trajeto != null ? trajeto : new ArrayUnorderedList<>();
    }

    /**
     * Obtem a lista de divisoes marcadas como entrada/saida.
     *
     * @return Lista de divisoes de entrada/saida.
     */
    public ArrayUnorderedList<String> getEntradasSaidas() {
        return entradasSaidas;
    }

    /**
     * Define a lista de divisoes marcadas como entrada/saida.
     *
     * @param entradasSaidas Nova lista de divisoes de entrada/saida.
     */
    public void setEntradasSaidas(ArrayUnorderedList<String> entradasSaidas) {
        this.entradasSaidas = entradasSaidas != null ? entradasSaidas : new ArrayUnorderedList<>();
    }

    /**
     * Obtem o codigo da missao.
     *
     * @return Codigo da missao.
     */
    public String getCodigoMissao() {
        return codigoMissao;
    }

    /**
     * Define o codigo da missao.
     *
     * @param codigoMissao Novo codigo da missao.
     */
    public void setCodigoMissao(String codigoMissao) {
        this.codigoMissao = codigoMissao;
    }

    /**
     * Obtem a versao da missao.
     *
     * @return Versao da missao.
     */
    public int getVersaoMissao() {
        return versaoMissao;
    }

    /**
     * Define a versao da missao.
     *
     * @param versaoMissao Nova versao da missao.
     */
    public void setVersaoMissao(int versaoMissao) {
        this.versaoMissao = versaoMissao;
    }

    /**
     * Retorna uma representacao em string do resultado da simulacao.
     *
     * @return String representando o resultado da simulacao.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ResultadoSimulacao {\n");
        sb.append("  Codigo Missao: ").append(codigoMissao).append(",\n");
        sb.append("  Versao Missao: ").append(versaoMissao).append(",\n");
        sb.append("  ID: ").append(id).append(",\n");
        sb.append("  Divisao Inicial: ").append(divisaoInicial).append(",\n");
        sb.append("  Divisao Final: ").append(divisaoFinal != null ? divisaoFinal : "N/A").append(",\n");
        sb.append("  Status: ").append(status).append(",\n");
        sb.append("  Vida Restante: ").append(vidaRestante).append(",\n");
        sb.append("  Trajeto: ").append(trajeto).append(",\n");
        sb.append("  Entradas/Saidas: ").append(entradasSaidas).append("\n");
        sb.append("}");
        return sb.toString();
    }
}
