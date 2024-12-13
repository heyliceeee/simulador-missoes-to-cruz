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
     * Construtor que formata o resultado da simulacao com base nos dados
     * fornecidos.
     *
     * @param resultado O resultado da simulacao.
     * @param mapa      O mapa da simulacao.
     */
    public ResultadoFormatadoImpl(IResultadoSimulacao resultado, IMapa mapa) {
        this.id = resultado.getId();
        this.divisaoInicial = resultado.getDivisaoInicial();
        this.divisaoFinal = resultado.getDivisaoFinal();
        this.status = resultado.getStatus();
        this.vidaRestante = resultado.getVidaRestante();

        // Garante que as listas nao serao nulas
        this.trajeto = resultado.getTrajeto() != null ? toJavaArray(resultado.getTrajeto()) : new String[0];
        this.entradasSaidas = mapa.getEntradasSaidasNomes() != null
                ? filtrarEntradasSaidas(mapa.getEntradasSaidasNomes(), resultado.getTrajeto())
                : new String[0];

        this.codigoMissao = resultado.getCodigoMissao();
        this.versaoMissao = resultado.getVersaoMissao();
    }

    /**
     * Obtem o identificador unico do resultado da simulacao.
     *
     * @return O identificador unico da simulacao.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Obtem o nome da divisao inicial onde a simulacao comecou.
     *
     * @return O nome da divisao inicial.
     */
    @Override
    public String getDivisaoInicial() {
        return divisaoInicial;
    }

    /**
     * Obtem o nome da divisao final onde a simulacao terminou.
     *
     * @return O nome da divisao final.
     */
    @Override
    public String getDivisaoFinal() {
        return divisaoFinal;
    }

    /**
     * Obtem o status da simulacao, como "SUCESSO" ou "FALHA".
     *
     * @return O status da simulacao.
     */
    @Override
    public String getStatus() {
        return status;
    }

    /**
     * Obtem a quantidade de vida restante ao final da simulacao.
     *
     * @return O valor da vida restante.
     */
    @Override
    public int getVidaRestante() {
        return vidaRestante;
    }

    /**
     * Obtem o trajeto percorrido durante a simulacao, representado como um array de
     * divisoes.
     *
     * @return Um array de strings com os nomes das divisoes do trajeto.
     */
    @Override
    public String[] getTrajeto() {
        return trajeto;
    }

    /**
     * Obtem os nomes das divisoes marcadas como entradas ou saidas no mapa da
     * simulacao.
     *
     * @return Um array de strings com os nomes das divisoes de entrada/saida.
     */
    @Override
    public String[] getEntradasSaidas() {
        return entradasSaidas;
    }

    /**
     * Obtem o codigo identificador da missao associada a simulacao.
     *
     * @return O codigo da missao.
     */
    @Override
    public String getCodigoMissao() {
        return codigoMissao;
    }

    /**
     * Obtem a versao da missao associada a simulacao.
     *
     * @return A versao da missao.
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
     * Filtra as entradas e saidas relevantes do mapa com base no trajeto da
     * simulacao.
     *
     * @param entradasSaidas A lista de entradas e saidas no mapa.
     * @param trajeto        O trajeto percorrido na simulacao.
     * @return Um array de strings com as entradas/saidas relevantes.
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
