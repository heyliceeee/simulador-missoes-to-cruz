package org.example.api.implementation.interfaces;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.api.implementation.models.ToCruz;
import org.example.collections.implementation.ArrayUnorderedList;

public interface Mapa {
    void adicionarDivisao(String nomeDivisao);
    void adicionarLigacao(String nomeDivisao1, String nomeDivisao2);
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
    public void moverInimigos(ToCruz toCruz, ICombateService combateService) throws ElementNotFoundException;
    public ArrayUnorderedList<IDivisao> calcularMelhorCaminho(IDivisao origem, IDivisao destino) throws ElementNotFoundException;
    public ArrayUnorderedList<IItem> getItensPorTipo(String tipo);
    void mostrarMapa();
}
