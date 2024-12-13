package org.example.collections.interfaces;

/**
 * A interface QueueADT define o contrato para uma Fila (Queue) abstrata.
 * Uma fila é uma coleção linear de elementos que segue o princípio FIFO (First In, First Out),
 * em que o primeiro elemento inserido é o primeiro a ser removido.
 * 
 * Esta interface fornece operações básicas de enfileiramento, desenfileiramento,
 * inspeção do primeiro elemento, verificação de tamanho e se a fila está vazia, 
 * além de gerar uma representação textual da fila.
 *
 * @param <T> o tipo de elementos armazenados na fila
 */
public interface QueueADT<T> {

    /**
     * Enfileira (adiciona) um elemento ao final (rear) da fila.
     * 
     * @param element o elemento a ser adicionado na parte traseira da fila.
     *                Não deve ser nulo. Se for nulo, o comportamento depende da implementação.
     */
    void enqueue(T element);

    /**
     * Desenfileira (remove) e retorna o elemento na frente (front) da fila.
     * Caso a fila esteja vazia, o comportamento depende da implementação:
     * - Poderá lançar uma exceção (por exemplo, EmptyCollectionException).
     * 
     * @return o elemento que estava na frente da fila.
     * @throws RuntimeException se a fila estiver vazia (a exceção específica depende da implementação).
     */
    T dequeue();

    /**
     * Retorna, sem remover, o elemento que está na frente (front) da fila.
     * Caso a fila esteja vazia, o comportamento também depende da implementação:
     * - Poderá lançar uma exceção (por exemplo, EmptyCollectionException).
     *
     * @return o primeiro elemento da fila.
     * @throws RuntimeException se a fila estiver vazia (a exceção específica depende da implementação).
     */
    T first();

    /**
     * Verifica se a fila está vazia, ou seja, sem elementos.
     *
     * @return true se a fila não contiver elementos, false caso contrário.
     */
    boolean isEmpty();

    /**
     * Retorna o número de elementos contidos na fila.
     *
     * @return a quantidade de elementos na fila.
     */
    int size();

    /**
     * Retorna uma representação em String dos elementos da fila, geralmente 
     * listando do primeiro até o último elemento. O formato exato da string 
     * depende da implementação.
     *
     * @return uma representação textual da fila.
     */
    @Override
    String toString();
}
