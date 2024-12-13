package org.example.collections.implementation;

import org.example.collections.exceptions.EmptyCollectionException;
import org.example.collections.interfaces.StackADT;

/**
 * Implementação de uma pilha (stack) genérica utilizando uma lista encadeada.
 * <p>
 * Esta classe implementa a interface {@code StackADT} e fornece operações básicas
 * como adicionar, remover e consultar elementos no topo da pilha.
 * </p>
 *
 * @param <T> O tipo de elementos armazenados na pilha.
 */
public class LinkedStack<T> implements StackADT<T> {

    /**
     * Nó que representa o elemento no topo da pilha.
     */
    private LinkedListNode<T> top;

    /**
     * Inteiro que representa o tamanho atual da pilha.
     */
    private int size;

    /**
     * Cria uma pilha vazia.
     * <p>
     * A pilha é inicializada com {@code top} nulo e tamanho 0.
     * </p>
     */
    public LinkedStack() {
        this.top = null;
        this.size = 0;
    }

    /**
     * Cria uma pilha com elementos predefinidos.
     *
     * @param top  O nó que será o topo inicial da pilha.
     * @param size O tamanho inicial da pilha.
     */
    public LinkedStack(LinkedListNode<T> top, int size) {
        this.top = top;
        this.size = size;
    }

    /**
     * Adiciona um elemento ao topo da pilha.
     *
     * @param element O elemento a ser adicionado.
     */
    @Override
    public void push(T element) {
        LinkedListNode<T> newNode = new LinkedListNode<>(element);
        newNode.setNext(top); // O próximo do novo nó aponta para o nó atual no topo
        top = newNode;        // Atualiza o topo para o novo nó
        size++;
    }

    /**
     * Remove o elemento do topo da pilha e o retorna.
     *
     * @return O elemento removido do topo da pilha.
     * @throws EmptyCollectionException Se a remoção for tentada em uma pilha vazia.
     */
    @Override
    public T pop() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Stack");
        }

        T result = top.getElement(); // Obtém o elemento no topo
        top = top.getNext();         // Move o topo para o próximo nó
        size--;
        return result;
    }

    /**
     * Retorna o elemento no topo da pilha sem removê-lo.
     *
     * @return O elemento no topo da pilha.
     * @throws EmptyCollectionException Se a consulta for tentada em uma pilha vazia.
     */
    @Override
    public T peek() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Stack");
        }

        return top.getElement();
    }

    /**
     * Verifica se a pilha está vazia.
     *
     * @return {@code true} se a pilha não contiver elementos, {@code false} caso contrário.
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Retorna o número de elementos na pilha.
     *
     * @return O tamanho da pilha.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Retorna uma representação em string da pilha.
     *
     * @return Uma string contendo informações sobre o topo e o tamanho da pilha.
     */
    @Override
    public String toString() {
        return "LinkedStack{" +
                "top=" + top +
                ", size=" + size +
                '}';
    }
}
