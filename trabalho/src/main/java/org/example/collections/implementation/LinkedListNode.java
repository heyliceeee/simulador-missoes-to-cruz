package org.example.collections.implementation;

/**
 * Representa um nó em uma lista encadeada genérica.
 * <p>
 * Esta classe encapsula um elemento armazenado e uma referência para o próximo nó na lista.
 * </p>
 *
 * @param <T> O tipo de dado armazenado no nó.
 */
public class LinkedListNode<T> {

    private T element;
    private LinkedListNode<T> next;

    /**
     * Construtor padrão que cria um nó vazio.
     * <p>
     * O elemento é inicializado como {@code null} e o próximo nó também é {@code null}.
     * </p>
     */
    public LinkedListNode() {
        element = null;
        next = null;
    }

    /**
     * Construtor que cria um nó com um elemento especificado.
     *
     * @param element O elemento a ser armazenado no nó.
     */
    public LinkedListNode(T element) {
        this.element = element;
        this.next = null;
    }

    /**
     * Obtém o elemento armazenado neste nó.
     *
     * @return O elemento armazenado no nó.
     */
    public T getElement() {
        return element;
    }

    /**
     * Define o elemento a ser armazenado neste nó.
     *
     * @param element O elemento a ser armazenado no nó.
     */
    public void setElement(T element) {
        this.element = element;
    }

    /**
     * Obtém a referência para o próximo nó na lista.
     *
     * @return O próximo nó na lista, ou {@code null} se este for o último nó.
     */
    public LinkedListNode<T> getNext() {
        return next;
    }

    /**
     * Define o próximo nó na lista.
     *
     * @param next O próximo nó na lista.
     */
    public void setNext(LinkedListNode<T> next) {
        this.next = next;
    }

    /**
     * Retorna uma representação em string do nó.
     *
     * @return Uma string contendo o elemento armazenado no nó.
     */
    @Override
    public String toString() {
        return "{element = " + element + '}';
    }
}
