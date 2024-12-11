package org.example.api.implementation.interfaces;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.collections.implementation.ArrayUnorderedList;

public interface Divisao {
    String getNomeDivisao();
    void adicionarInimigo(Inimigo inimigo);
    void removerInimigo(Inimigo inimigo) throws ElementNotFoundException;
    void adicionarItem(Item item);
    void removerItem(Item item) throws ElementNotFoundException;
    boolean isEntradaSaida();
    void setEntradaSaida(boolean entradaSaida);
    ArrayUnorderedList<Item> getItensPresentes();
    ArrayUnorderedList<Inimigo> getInimigosPresentes();

}
