package org.example.collections.interfaces;

import java.util.Iterator;
import org.example.api.exceptions.ElementNotFoundException;

/**
 * A interface ListADT define as operações gerais para uma Lista (List) abstrata.
 * Uma lista é uma coleção ordenada de elementos, onde cada elemento possui uma posição.
 * Esta interface fornece métodos para remoção, acesso a elementos específicos 
 * (primeiro, último), verificação de conteúdo, e obtenção de tamanho, além de 
 * permitir a iteração sobre seus elementos.
 *
 * <p>As implementações específicas (como listas encadeadas, listas dinâmicas, etc.)
 * podem estabelecer suas próprias complexidades e comportamentos específicos,
 * mas todas devem seguir o contrato definido por esta interface.</p>
 *
 * @param <T> o tipo dos elementos armazenados na lista.
 */
public interface ListADT<T> extends Iterable<T> {

    /**
     * Remove e retorna o primeiro elemento desta lista.
     *
     * @return o primeiro elemento da lista
     * @throws ElementNotFoundException se a lista estiver vazia ou não for possível encontrar o elemento.
     */
    public T removeFirst() throws ElementNotFoundException;

    /**
     * Remove e retorna o último elemento desta lista.
     *
     * @return o último elemento da lista
     * @throws ElementNotFoundException se a lista estiver vazia ou não for possível encontrar o elemento.
     */
    public T removeLast() throws ElementNotFoundException;

    /**
     * Remove e retorna o elemento especificado desta lista.
     * Se o elemento aparecer múltiplas vezes, a implementação pode remover a primeira ocorrência
     * ou todas as ocorrências, dependendo da especificação.
     *
     * @param element o elemento a ser removido da lista
     * @return o elemento removido da lista
     * @throws ElementNotFoundException se o elemento não for encontrado na lista.
     */
    public T remove(T element) throws ElementNotFoundException;

    /**
     * Retorna uma referência ao primeiro elemento desta lista, sem removê-lo.
     *
     * @return a referência ao primeiro elemento da lista
     * @throws ElementNotFoundException se a lista estiver vazia.
     */
    public T first() throws ElementNotFoundException;

    /**
     * Retorna uma referência ao último elemento desta lista, sem removê-lo.
     *
     * @return a referência ao último elemento da lista
     * @throws ElementNotFoundException se a lista estiver vazia.
     */
    public T last() throws ElementNotFoundException;

    /**
     * Verifica se a lista contém o elemento especificado.
     *
     * @param target o elemento a ser procurado na lista
     * @return true se a lista contiver o elemento, false caso contrário.
     */
    public boolean contains(T target);

    /**
     * Verifica se a lista está vazia (não contém elementos).
     *
     * @return true se a lista não contiver elementos, false caso contrário.
     */
    public boolean isEmpty();

    /**
     * Retorna o número de elementos contidos nesta lista.
     *
     * @return a quantidade de elementos na lista.
     */
    public int size();

    /**
     * Retorna um iterador para os elementos nesta lista, permitindo a iteração 
     * sequencial de seus elementos sem removê-los.
     *
     * @return um iterador sobre os elementos da lista.
     */
    @Override
    public Iterator<T> iterator();

    /**
     * Retorna uma representação em String desta lista, geralmente listando 
     * todos os elementos em ordem. O formato exato da string depende da implementação.
     *
     * @return uma representação textual da lista.
     */
    @Override
    public String toString();

}
