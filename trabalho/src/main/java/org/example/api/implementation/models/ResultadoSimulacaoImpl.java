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

    /**
     * Construtor completo.
     *
     * @param id             Identificador único do resultado.
     * @param divisaoInicial Divisão inicial da simulação.
     * @param divisaoFinal   Divisão final da simulação.
     * @param status         Status da simulação ("SUCESSO" ou "FALHA").
     * @param vidaRestante   Vida restante do personagem ao final da simulação.
     * @param trajeto        Lista de divisões percorridas na simulação.
     * @param entradasSaidas Lista de divisões marcadas como entrada/saída.
     * @param codigoMissao   Código identificador da missão.
     * @param versaoMissao   Versão da missão.
     * @throws IllegalArgumentException se algum parâmetro obrigatório for nulo ou
     *                                  inválido.
     */
    public ResultadoSimulacaoImpl(String id, String divisaoInicial, String divisaoFinal, String status,
            int vidaRestante,
            ArrayUnorderedList<String> trajeto, ArrayUnorderedList<String> entradasSaidas,
            String codigoMissao, int versaoMissao) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID não pode ser nulo ou vazio.");
        }
        if (divisaoInicial == null || divisaoInicial.trim().isEmpty()) {
            throw new IllegalArgumentException("Divisão inicial não pode ser nula ou vazia.");
        }
        if (status == null || (!status.equalsIgnoreCase("SUCESSO") && !status.equalsIgnoreCase("FALHA"))) {
            throw new IllegalArgumentException("Status inválido. Deve ser 'SUCESSO' ou 'FALHA'.");
        }
        if (vidaRestante < 0) {
            throw new IllegalArgumentException("Vida restante não pode ser negativa.");
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
     * Obtém o identificador do resultado.
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
            throw new IllegalArgumentException("ID não pode ser nulo ou vazio.");
        }
        this.id = id;
    }

    /**
     * Obtém a divisão inicial da simulação.
     *
     * @return Divisão inicial.
     */
    public String getDivisaoInicial() {
        return divisaoInicial;
    }

    /**
     * Define a divisão inicial da simulação.
     *
     * @param divisaoInicial Nova divisão inicial.
     * @throws IllegalArgumentException se a divisão inicial for nula ou vazia.
     */
    public void setDivisaoInicial(String divisaoInicial) {
        if (divisaoInicial == null || divisaoInicial.trim().isEmpty()) {
            throw new IllegalArgumentException("Divisão inicial não pode ser nula ou vazia.");
        }
        this.divisaoInicial = divisaoInicial;
    }

    /**
     * Obtém a divisão final da simulação.
     *
     * @return Divisão final.
     */
    public String getDivisaoFinal() {
        return divisaoFinal;
    }

    /**
     * Define a divisão final da simulação.
     *
     * @param divisaoFinal Nova divisão final.
     */
    public void setDivisaoFinal(String divisaoFinal) {
        this.divisaoFinal = divisaoFinal;
    }

    /**
     * Obtém o status da simulação.
     *
     * @return Status da simulação.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Define o status da simulação.
     *
     * @param status Novo status ("SUCESSO" ou "FALHA").
     * @throws IllegalArgumentException se o status for inválido.
     */
    public void setStatus(String status) {
        if (status == null || (!status.equalsIgnoreCase("SUCESSO") && !status.equalsIgnoreCase("FALHA"))) {
            throw new IllegalArgumentException("Status inválido. Deve ser 'SUCESSO' ou 'FALHA'.");
        }
        this.status = status;
    }

    /**
     * Obtém a vida restante ao final da simulação.
     *
     * @return Vida restante.
     */
    public int getVidaRestante() {
        return vidaRestante;
    }

    /**
     * Define a vida restante ao final da simulação.
     *
     * @param vidaRestante Nova vida restante.
     * @throws IllegalArgumentException se a vida restante for negativa.
     */
    public void setVidaRestante(int vidaRestante) {
        if (vidaRestante < 0) {
            throw new IllegalArgumentException("Vida restante não pode ser negativa.");
        }
        this.vidaRestante = vidaRestante;
    }

    /**
     * Obtém a lista de divisões percorridas na simulação.
     *
     * @return Lista de divisões percorridas.
     */
    public ArrayUnorderedList<String> getTrajeto() {
        return trajeto;
    }

    /**
     * Define a lista de divisões percorridas na simulação.
     *
     * @param trajeto Novo trajeto.
     */
    public void setTrajeto(ArrayUnorderedList<String> trajeto) {
        this.trajeto = trajeto != null ? trajeto : new ArrayUnorderedList<>();
    }

    /**
     * Obtém a lista de divisões marcadas como entrada/saída.
     *
     * @return Lista de divisões de entrada/saída.
     */
    public ArrayUnorderedList<String> getEntradasSaidas() {
        return entradasSaidas;
    }

    /**
     * Define a lista de divisões marcadas como entrada/saída.
     *
     * @param entradasSaidas Nova lista de divisões de entrada/saída.
     */
    public void setEntradasSaidas(ArrayUnorderedList<String> entradasSaidas) {
        this.entradasSaidas = entradasSaidas != null ? entradasSaidas : new ArrayUnorderedList<>();
    }

    /**
     * Obtém o código da missão.
     *
     * @return Código da missão.
     */
    public String getCodigoMissao() {
        return codigoMissao;
    }

    /**
     * Define o código da missão.
     *
     * @param codigoMissao Novo código da missão.
     */
    public void setCodigoMissao(String codigoMissao) {
        this.codigoMissao = codigoMissao;
    }

    /**
     * Obtém a versão da missão.
     *
     * @return Versão da missão.
     */
    public int getVersaoMissao() {
        return versaoMissao;
    }

    /**
     * Define a versão da missão.
     *
     * @param versaoMissao Nova versão da missão.
     */
    public void setVersaoMissao(int versaoMissao) {
        this.versaoMissao = versaoMissao;
    }

    /**
     * Retorna uma representação em string do resultado da simulação.
     *
     * @return String representando o resultado da simulação.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ResultadoSimulacao {\n");
        sb.append("  Código Missão: ").append(codigoMissao).append(",\n");
        sb.append("  Versão Missão: ").append(versaoMissao).append(",\n");
        sb.append("  ID: ").append(id).append(",\n");
        sb.append("  Divisão Inicial: ").append(divisaoInicial).append(",\n");
        sb.append("  Divisão Final: ").append(divisaoFinal != null ? divisaoFinal : "N/A").append(",\n");
        sb.append("  Status: ").append(status).append(",\n");
        sb.append("  Vida Restante: ").append(vidaRestante).append(",\n");
        sb.append("  Trajeto: ").append(trajeto).append(",\n");
        sb.append("  Entradas/Saídas: ").append(entradasSaidas).append("\n");
        sb.append("}");
        return sb.toString();
    }
}
