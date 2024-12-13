package org.example.api.implementation.interfaces;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.collections.implementation.ArrayUnorderedList;
import org.example.collections.implementation.LinkedList;

/**
 * Representa uma divisao no mapa de um edificio.
 *
 * <p>
 * A divisao armazena informacoes sobre inimigos, itens e se e uma entrada ou
 * saida.
 * Ela serve como uma unidade basica dentro do mapa, permitindo a interacao com
 * inimigos e itens e indicando se e acessivel como ponto de entrada ou saida.
 * </p>
 */
public interface IDivisao {
    /**
     * Obtem o nome da divisao.
     *
     * @return O nome da divisao.
     */
    String getNomeDivisao();

    void setNomeDivisao(String nomeDivisao);

    /**
     * Adiciona um inimigo a divisao.
     *
     * @param inimigo O inimigo a ser adicionado.
     * @throws IllegalArgumentException Se o inimigo for nulo.
     */
    void adicionarInimigo(IInimigo inimigo);

    /**
     * Remove um inimigo da divisao.
     *
     * @param inimigo O inimigo a ser removido.
     * @throws ElementNotFoundException Se o inimigo nao estiver presente na
     *                                  divisao.
     * @throws IllegalArgumentException Se o inimigo for nulo.
     */
    void removerInimigo(IInimigo inimigo) throws ElementNotFoundException;

    /**
     * Adiciona um item a divisao.
     *
     * @param item O item a ser adicionado.
     * @throws IllegalArgumentException Se o item for nulo.
     */
    void adicionarItem(IItem item);

    /**
     * Remove um item da divisao.
     *
     * @param item O item a ser removido.
     * @throws ElementNotFoundException Se o item nao estiver presente na divisao.
     * @throws IllegalArgumentException Se o item for nulo.
     */
    void removerItem(IItem item) throws ElementNotFoundException;

    /**
     * Verifica se a divisao e uma entrada ou saida.
     *
     * @return {@code true} se for uma entrada ou saida, {@code false} caso
     *         contrario.
     */
    boolean isEntradaSaida();

    /**
     * Define se a divisao e uma entrada ou saida.
     *
     * @param entradaSaida {@code true} para marcar como entrada ou saida,
     *                     {@code false} caso contrario.
     */
    void setEntradaSaida(boolean entradaSaida);

    /**
     * Obtem a lista de itens presentes na divisao.
     *
     * @return Uma lista de itens presentes na divisao.
     */
    ArrayUnorderedList<IItem> getItensPresentes();

    void setItensPresentes(ArrayUnorderedList<IItem> itensPresentes);

    /**
     * Obtem a lista de inimigos presentes na divisao.
     *
     * @return Uma lista de inimigos presentes na divisao.
     */
    ArrayUnorderedList<IInimigo> getInimigosPresentes();

    void setInimigosPresentes(ArrayUnorderedList<IInimigo> inimigosPresentes);

    boolean temKit();
}
