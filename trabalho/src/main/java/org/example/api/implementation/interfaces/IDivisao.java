package org.example.api.implementation.interfaces;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.collections.implementation.ArrayUnorderedList;
import org.example.collections.implementation.LinkedList;

public interface IDivisao {
    String getNomeDivisao();

    void setNomeDivisao(String nomeDivisao);

    void adicionarInimigo(IInimigo inimigo);

    void removerInimigo(IInimigo inimigo) throws ElementNotFoundException;

    void adicionarItem(IItem item);

    void removerItem(IItem item) throws ElementNotFoundException;

    boolean isEntradaSaida();

    void setEntradaSaida(boolean entradaSaida);

    ArrayUnorderedList<IItem> getItensPresentes();

    void setItensPresentes(ArrayUnorderedList<IItem> itensPresentes);

    ArrayUnorderedList<IInimigo> getInimigosPresentes();

    void setInimigosPresentes(ArrayUnorderedList<IInimigo> inimigosPresentes);

    boolean temKit();
}
