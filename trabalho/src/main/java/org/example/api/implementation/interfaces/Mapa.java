package org.example.api.implementation.interfaces;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.implementation.models.ToCruz;
import org.example.collections.implementation.ArrayUnorderedList;

public interface Mapa {
    void adicionarDivisao(String nomeDivisao);
    void adicionarLigacao(String nomeDivisao1, String nomeDivisao2);
    void adicionarInimigo(String nomeDivisao, Inimigo inimigo);
    void adicionarItem(String nomeDivisao, Item item);
    void adicionarEntradaSaida(String nomeDivisao);
    Divisao getDivisaoPorNome(String nomeDivisao);
    boolean podeMover(String divisao1, String divisao2);
    void definirAlvo(String nomeDivisao, String tipo);
    void removerAlvo();
    Alvo getAlvo();
    ArrayUnorderedList<Divisao> obterConexoes(Divisao divisao);
    ArrayUnorderedList<Divisao> getDivisoes();
    ArrayUnorderedList<String> getEntradasSaidasNomes();
    public void moverInimigos(ToCruz toCruz, CombateService combateService) throws ElementNotFoundException;
    void mostrarMapa();
}
