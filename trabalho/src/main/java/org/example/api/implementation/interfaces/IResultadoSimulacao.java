package org.example.api.implementation.interfaces;

import org.example.collections.implementation.ArrayUnorderedList;

/**
 * Interface que representa o resultado de uma simulação.
 */
public interface IResultadoSimulacao {

    String getId();

    void setId(String id);

    String getDivisaoInicial();

    void setDivisaoInicial(String divisaoInicial);

    String getDivisaoFinal();

    void setDivisaoFinal(String divisaoFinal);

    String getStatus();

    void setStatus(String status);

    int getVidaRestante();

    void setVidaRestante(int vidaRestante);

    ArrayUnorderedList<String> getTrajeto();

    void setTrajeto(ArrayUnorderedList<String> trajeto);

    ArrayUnorderedList<String> getEntradasSaidas();

    void setEntradasSaidas(ArrayUnorderedList<String> entradasSaidas);

    String getCodigoMissao();

    int getVersaoMissao();

    @Override
    String toString();
}
