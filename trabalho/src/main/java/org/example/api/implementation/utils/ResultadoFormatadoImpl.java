package org.example.api.implementation.utils;

import org.example.api.implementation.interfaces.IMapa;
import org.example.api.implementation.interfaces.IResultadoFormatado;
import org.example.api.implementation.interfaces.IResultadoSimulacao;
import org.example.collections.implementation.ArrayUnorderedList;

/**
 * Implementacao de ResultadoFormatado.
 */
public class ResultadoFormatadoImpl implements IResultadoFormatado {
    private final String id;
    private final String divisaoInicial;
    private final String divisaoFinal;
    private final String status;
    private final int vidaRestante;
    private final String[] trajeto;
    private final String[] entradasSaidas;
    private final String codigoMissao;
    private final int versaoMissao;

    /**
     * Construtor que formata o resultado da simulação com base nos dados fornecidos.
     *
     * @param resultado O resultado da simulação.
     * @param mapa      O mapa da simulação.
     */
    public ResultadoFormatadoImpl(ResultadoSimulacao resultado, Mapa mapa) {
        this.id = resultado.getId();
        this.divisaoInicial = resultado.getDivisaoInicial();
        this.divisaoFinal = resultado.getDivisaoFinal();
        this.status = resultado.getStatus();
        this.vidaRestante = resultado.getVidaRestante();

        // Garante que as listas não serão nulas
        this.trajeto = resultado.getTrajeto() != null ? toJavaArray(resultado.getTrajeto()) : new String[0];
        this.entradasSaidas = mapa.getEntradasSaidasNomes() != null
                ? filtrarEntradasSaidas(mapa.getEntradasSaidasNomes(), resultado.getTrajeto())
                : new String[0];

        this.codigoMissao = resultado.getCodigoMissao();
        this.versaoMissao = resultado.getVersaoMissao();
    }

    /**
     * Obtém o identificador único do resultado da simulação.
     *
     * @return O identificador único da simulação.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Obtém o nome da divisão inicial onde a simulação começou.
     *
     * @return O nome da divisão inicial.
     */
    @Override
    public String getDivisaoInicial() {
        return divisaoInicial;
    }

    /**
     * Obtém o nome da divisão final onde a simulação terminou.
     *
     * @return O nome da divisão final.
     */
    @Override
    public String getDivisaoFinal() {
        return divisaoFinal;
    }

    /**
     * Obtém o status da simulação, como "SUCESSO" ou "FALHA".
     *
     * @return O status da simulação.
     */
    @Override
    public String getStatus() {
        return status;
    }

    /**
     * Obtém a quantidade de vida restante ao final da simulação.
     *
     * @return O valor da vida restante.
     */
    @Override
    public int getVidaRestante() {
        return vidaRestante;
    }

    /**
     * Obtém o trajeto percorrido durante a simulação, representado como um array de divisões.
     *
     * @return Um array de strings com os nomes das divisões do trajeto.
     */
    @Override
    public String[] getTrajeto() {
        return trajeto;
    }

    /**
     * Obtém os nomes das divisões marcadas como entradas ou saídas no mapa da simulação.
     *
     * @return Um array de strings com os nomes das divisões de entrada/saída.
     */
    @Override
    public String[] getEntradasSaidas() {
        return entradasSaidas;
    }

    /**
     * Obtém o código identificador da missão associada à simulação.
     *
     * @return O código da missão.
     */
    @Override
    public String getCodigoMissao() {
        return codigoMissao;
    }

    /**
     * Obtém a versão da missão associada à simulação.
     *
     * @return A versão da missão.
     */
    @Override
    public int getVersaoMissao() {
        return versaoMissao;
    }

    /**
     * Converte uma lista desordenada para um array.
     *
     * @param unorderedList A lista desordenada.
     * @return Um array de strings.
     */
    private String[] toJavaArray(ArrayUnorderedList<String> unorderedList) {
        if (unorderedList == null || unorderedList.size() == 0) {
            return new String[0];
        }
        String[] array = new String[unorderedList.size()];
        for (int i = 0; i < unorderedList.size(); i++) {
            array[i] = unorderedList.getElementAt(i);
        }
        return array;
    }

    /**
     * Filtra as entradas e saídas relevantes do mapa com base no trajeto da simulação.
     *
     * @param entradasSaidas A lista de entradas e saídas no mapa.
     * @param trajeto        O trajeto percorrido na simulação.
     * @return Um array de strings com as entradas/saídas relevantes.
     */
    private String[] filtrarEntradasSaidas(ArrayUnorderedList<String> entradasSaidas,
                                           ArrayUnorderedList<String> trajeto) {
        if (trajeto == null || trajeto.size() == 0) {
            return new String[0];
        }

        ArrayUnorderedList<String> relevantes = new ArrayUnorderedList<>();
        for (int i = 0; i < entradasSaidas.size(); i++) {
            String entradaSaida = entradasSaidas.getElementAt(i);
            if (entradaSaida != null && trajeto.contains(entradaSaida)) {
                relevantes.addToRear(entradaSaida);
            }
        }
        return toJavaArray(relevantes);
    }
}
