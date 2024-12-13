package org.example.collections.interfaces;

/**
 * A interface StackADT define o contrato para uma Pilha (Stack) abstrata.
 * Uma pilha é uma coleção linear de elementos baseada no princípio LIFO (Last In, First Out),
 * em que o último elemento inserido é o primeiro a ser removido.
 *
 * Esta interface fornece operações básicas de empilhamento, desempilhamento, acesso ao
 * elemento do topo, verificação do tamanho e se a pilha está vazia, além de gerar
 * uma representação textual da pilha.
 *
 * @param <T> o tipo de elementos armazenados na pilha
 */
public interface StackADT<T> {

    /**
     * Empilha (adiciona) um elemento no topo da pilha.
     * 
     * @param element o elemento a ser adicionado ao topo da pilha.
     *                Não deve ser nulo. Caso seja nulo, o comportamento depende da implementação.
     */
    void push(T element);

    /**
     * Desempilha (remove) e retorna o elemento no topo da pilha.
     * Caso a pilha esteja vazia, o comportamento depende da implementação:
     * - Pode lançar uma exceção, por exemplo, EmptyCollectionException.
     *
     * @return o elemento que estava no topo da pilha.
     * @throws RuntimeException se a pilha estiver vazia (a exceção específica depende da implementação).
     */
    T pop();

    /**
     * Retorna, sem remover, o elemento que está no topo da pilha.
     * Caso a pilha esteja vazia, o comportamento também depende da implementação:
     * - Pode lançar uma exceção, por exemplo, EmptyCollectionException.
     *
     * @return o elemento no topo da pilha.
     * @throws RuntimeException se a pilha estiver vazia (a exceção específica depende da implementação).
     */
    T peek();

    /**
     * Verifica se a pilha está vazia.
     *
     * @return true se a pilha não contiver elementos, false caso contrário.
     */
    boolean isEmpty();

    /**
     * Retorna o número de elementos contidos na pilha.
     *
     * @return a quantidade de elementos na pilha.
     */
    int size();

    /**
     * Retorna uma representação em String dos elementos da pilha, geralmente 
     * listando do topo até a base. O formato exato da string depende da implementação.
     *
     * @return uma representação textual da pilha.
     */
    @Override
    String toString();
}
