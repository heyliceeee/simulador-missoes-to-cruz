package org.example.collections.implementation;

import org.example.collections.exceptions.EmptyCollectionException;
import org.example.collections.interfaces.QueueADT;

/**
 * Implementação de uma fila (queue) genérica utilizando uma lista encadeada.
 * <p>
 * Esta classe implementa a interface {@code QueueADT} e oferece operações básicas
 * como inserção, remoção e consulta de elementos na fila.
 * </p>
 *
 * @param <T> O tipo de elementos armazenados na fila.
 */
public class LinkedQueue<T> implements QueueADT<T> {

    /**
     * Nó que representa o elemento na frente da fila.
     */
    private LinkedListNode<T> front;

    /**
     * Nó que representa o elemento na traseira da fila.
     */
    private LinkedListNode<T> rear;

    /**
     * Inteiro que representa o tamanho atual da fila.
     */
    private int size;

    /**
     * Cria uma fila vazia.
     * <p>
     * A fila é inicializada com {@code front} e {@code rear} nulos e tamanho 0.
     * </p>
     */
    public LinkedQueue() {
        this.front = this.rear = null;
        this.size = 0;
    }

    /**
     * Cria uma fila com elementos predefinidos.
     *
     * @param front O nó que será o primeiro elemento da fila.
     * @param rear  O nó que será o último elemento da fila.
     * @param size  O tamanho inicial da fila.
     */
    public LinkedQueue(LinkedListNode<T> front, LinkedListNode<T> rear, int size) {
        this.front = front;
        this.rear = rear;
        this.size = size;
    }

    /**
     * Adiciona um elemento à traseira da fila.
     *
     * @param element O elemento a ser adicionado.
     */
    @Override
    public void enqueue(T element) {
        LinkedListNode<T> newNode = new LinkedListNode<>(element);

        if (isEmpty()) { // Se a fila está vazia
            front = newNode;
            rear = newNode;
        } else {
            rear.setNext(newNode); // O próximo do nó traseiro é o novo nó
            rear = newNode;       // Atualiza o nó traseiro
        }

        size++;
    }

    /**
     * Remove e retorna o elemento na frente da fila.
     *
     * @return O elemento na frente da fila.
     * @throws EmptyCollectionException Se a fila estiver vazia.
     */
    @Override
    public T dequeue() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Queue");
        }

        T result = front.getElement(); // Obtém o elemento do nó frontal
        front = front.getNext();       // Avança o nó frontal para o próximo

        size--;

        if (isEmpty()) { // Se a fila está vazia após a remoção
            rear = null;
        }

        return result;
    }

    /**
     * Retorna o elemento na frente da fila sem removê-lo.
     *
     * @return O elemento na frente da fila.
     * @throws EmptyCollectionException Se a fila estiver vazia.
     */
    @Override
    public T first() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Queue");
        }

        return front.getElement();
    }

    /**
     * Verifica se a fila está vazia.
     *
     * @return {@code true} se a fila estiver vazia, {@code false} caso contrário.
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Retorna o número de elementos na fila.
     *
     * @return O tamanho da fila.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Retorna uma representação em string da fila.
     *
     * @return Uma string contendo os elementos da fila.
     */
    @Override
    public String toString() {
        return "LinkedQueue{" +
                "front=" + front +
                ", rear=" + rear +
                ", size=" + size +
                '}';
    }
}
