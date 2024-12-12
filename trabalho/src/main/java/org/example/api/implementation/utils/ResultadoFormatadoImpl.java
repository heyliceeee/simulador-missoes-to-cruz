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

    public ResultadoFormatadoImpl(IResultadoSimulacao resultado, IMapa mapa) {
        this.id = resultado.getId();
        this.divisaoInicial = resultado.getDivisaoInicial();
        this.divisaoFinal = resultado.getDivisaoFinal();
        this.status = resultado.getStatus();
        this.vidaRestante = resultado.getVidaRestante();
        this.trajeto = toJavaArray(resultado.getTrajeto());
        this.entradasSaidas = filtrarEntradasSaidas(mapa.getEntradasSaidasNomes(), resultado.getTrajeto());
        this.codigoMissao = resultado.getCodigoMissao();
        this.versaoMissao = resultado.getVersaoMissao();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDivisaoInicial() {
        return divisaoInicial;
    }

    @Override
    public String getDivisaoFinal() {
        return divisaoFinal;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public int getVidaRestante() {
        return vidaRestante;
    }

    @Override
    public String[] getTrajeto() {
        return trajeto;
    }

    @Override
    public String[] getEntradasSaidas() {
        return entradasSaidas;
    }

    @Override
    public String getCodigoMissao() {
        return codigoMissao;
    }

    @Override
    public int getVersaoMissao() {
        return versaoMissao;
    }

    private String[] toJavaArray(ArrayUnorderedList<String> unorderedList) {
        String[] array = new String[unorderedList.size()];
        for (int i = 0; i < unorderedList.size(); i++) {
            array[i] = unorderedList.getElementAt(i);
        }
        return array;
    }

    private String[] filtrarEntradasSaidas(ArrayUnorderedList<String> entradasSaidas,
            ArrayUnorderedList<String> trajeto) {
        ArrayUnorderedList<String> relevantes = new ArrayUnorderedList<>();
        for (int i = 0; i < entradasSaidas.size(); i++) {
            String entradaSaida = entradasSaidas.getElementAt(i);
            if (trajeto.contains(entradaSaida)) {
                relevantes.addToRear(entradaSaida);
            }
        }
        return toJavaArray(relevantes);
    }
}
