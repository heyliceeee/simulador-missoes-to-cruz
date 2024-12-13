package org.example.api.implementation.interfaces;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.collections.implementation.ArrayUnorderedList;

/**
 * Representa uma divisão no mapa de um edifício.
 * 
 * <p>
 * A divisão armazena informações sobre inimigos, itens e se é uma entrada ou
 * saída.
 * Ela serve como uma unidade básica dentro do mapa, permitindo a interação com
 * inimigos e itens e indicando se é acessível como ponto de entrada ou saída.
 * </p>
 */
public interface Divisao {

    /**
     * Obtém o nome da divisão.
     *
     * @return O nome da divisão.
     */
    String getNomeDivisao();

    /**
     * Adiciona um inimigo à divisão.
     *
     * @param inimigo O inimigo a ser adicionado.
     * @throws IllegalArgumentException Se o inimigo for nulo.
     */
    void adicionarInimigo(Inimigo inimigo);

    /**
     * Remove um inimigo da divisão.
     *
     * @param inimigo O inimigo a ser removido.
     * @throws ElementNotFoundException Se o inimigo não estiver presente na
     *                                  divisão.
     * @throws IllegalArgumentException Se o inimigo for nulo.
     */
    void removerInimigo(Inimigo inimigo) throws ElementNotFoundException;

    /**
     * Adiciona um item à divisão.
     *
     * @param item O item a ser adicionado.
     * @throws IllegalArgumentException Se o item for nulo.
     */
    void adicionarItem(Item item);

    /**
     * Remove um item da divisão.
     *
     * @param item O item a ser removido.
     * @throws ElementNotFoundException Se o item não estiver presente na divisão.
     * @throws IllegalArgumentException Se o item for nulo.
     */
    void removerItem(Item item) throws ElementNotFoundException;

    /**
     * Verifica se a divisão é uma entrada ou saída.
     *
     * @return {@code true} se for uma entrada ou saída, {@code false} caso
     *         contrário.
     */
    boolean isEntradaSaida();

    /**
     * Define se a divisão é uma entrada ou saída.
     *
     * @param entradaSaida {@code true} para marcar como entrada ou saída,
     *                     {@code false} caso contrário.
     */
    void setEntradaSaida(boolean entradaSaida);

    /**
     * Obtém a lista de itens presentes na divisão.
     *
     * @return Uma lista de itens presentes na divisão.
     */
    ArrayUnorderedList<Item> getItensPresentes();

    /**
     * Obtém a lista de inimigos presentes na divisão.
     *
     * @return Uma lista de inimigos presentes na divisão.
     */
    ArrayUnorderedList<Inimigo> getInimigosPresentes();
}
