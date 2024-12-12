package org.example.api.implementation.interfaces;

/**
 * Interface para formatar os resultados de maneira simples.
 */
public interface IResultadoFormatado {

    String getId();

    String getDivisaoInicial();

    String getDivisaoFinal();

    String getStatus();

    int getVidaRestante();

    String[] getTrajeto();

    String[] getEntradasSaidas();

    String getCodigoMissao();

    int getVersaoMissao();
}
