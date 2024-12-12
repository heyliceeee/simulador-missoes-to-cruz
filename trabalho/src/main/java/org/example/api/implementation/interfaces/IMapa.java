package org.example.api.implementation.interfaces;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.implementation.models.Ligacao;
import org.example.api.implementation.models.ToCruz;
import org.example.collections.implementation.ArrayUnorderedList;
import org.example.collections.implementation.LinkedList;

public interface IMapa {
    void adicionarDivisao(String nomeDivisao);

    void adicionarLigacao(String nomeDivisao1, String nomeDivisao2);

    ArrayUnorderedList<Ligacao> getLigacoes();

    void adicionarInimigo(String nomeDivisao, IInimigo inimigo);

    void adicionarItem(String nomeDivisao, IItem item);

    void adicionarEntradaSaida(String nomeDivisao);

    IDivisao getDivisaoPorNome(String nomeDivisao);

    boolean podeMover(String divisao1, String divisao2);

    void definirAlvo(String nomeDivisao, String tipo);

    void removerAlvo();

    IAlvo getAlvo();

    ArrayUnorderedList<IDivisao> obterConexoes(IDivisao divisao);

    ArrayUnorderedList<IDivisao> getDivisoes();

    ArrayUnorderedList<String> getEntradasSaidasNomes();

    ArrayUnorderedList<IDivisao> getEntradasSaidas();

    void moverInimigos(ToCruz toCruz, ICombateService combateService) throws ElementNotFoundException;

    void mostrarMapa();

    IDivisao getPrimeiroVertice();

    ArrayUnorderedList<IDivisao> expandirConexoes(IDivisao divisaoAtual);

    ArrayUnorderedList<IDivisao> calcularMelhorCaminho(IDivisao origem, IDivisao destino);

    IDivisao encontrarKitMaisProximo(IDivisao origem) throws ElementNotFoundException;

    ArrayUnorderedList<IItem> getItensPorTipo(String kitDeVida);
}
