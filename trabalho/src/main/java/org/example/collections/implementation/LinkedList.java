package org.example.collections.implementation;

import java.util.Iterator;
import org.example.collections.exceptions.EmptyListException;

/**
 * Implementação de uma lista encadeada genérica.
 * <p>
 * Esta classe fornece funcionalidades básicas para manipulação de uma lista
 * encadeada, como adição, remoção, verificação de elementos e iteração.
 * </p>
 *
 * @param <T> O tipo de dados armazenados na lista.
 */
public class LinkedList<T> implements Iterable<T> {

    private Node<T> head;
    private Node<T> tail;
    private int count;

    /**
     * Construtor padrão que inicializa uma lista encadeada vazia.
     */
    public LinkedList() {
        this.head = null;
        this.tail = null;
        this.count = 0;
    }

    /**
     * Adiciona um elemento à lista no início.
     *
     * @param data O elemento a ser adicionado.
     */
    public void add(T data) {
        Node<T> newNode = new Node<>(data);

        if (this.head == null) {
            this.tail = newNode;
        } else {
            newNode.setNext(this.head);
        }

        this.head = newNode;
        this.count++;
    }

    /**
     * Adiciona um elemento no início da lista.
     *
     * @param data O elemento a ser adicionado.
     */
    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.setNext(head);
        head = newNode;

        if (tail == null) {
            tail = newNode;
        }

        count++;
    }

    /**
     * Verifica se a lista está vazia.
     *
     * @return {@code true} se a lista estiver vazia, {@code false} caso contrário.
     */
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * Define o valor de um elemento na posição especificada.
     *
     * @param index   O índice do elemento a ser atualizado.
     * @param element O novo valor do elemento.
     * @throws IndexOutOfBoundsException Se o índice estiver fora dos limites da lista.
     */
    public void setElementAt(int index, T element) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Índice fora dos limites: " + index);
        }

        Node<T> current = head;
        int currentIndex = 0;

        while (current != null) {
            if (currentIndex == index) {
                current.setData(element);
                return;
            }
            current = current.getNext();
            currentIndex++;
        }

        throw new IllegalStateException("Erro inesperado ao procurar o índice.");
    }

    /**
     * Obtém o elemento na posição especificada.
     *
     * @param index O índice do elemento desejado.
     * @return O elemento na posição especificada.
     * @throws IndexOutOfBoundsException Se o índice estiver fora dos limites da lista.
     */
    public T getElementAt(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Índice fora dos limites: " + index);
        }

        Node<T> current = head;
        int currentIndex = 0;

        while (current != null) {
            if (currentIndex == index) {
                return current.getData();
            }
            current = current.getNext();
            currentIndex++;
        }

        throw new IllegalStateException("Erro inesperado ao procurar o índice.");
    }

    /**
     * Verifica se a lista contém o elemento especificado.
     *
     * @param element O elemento a ser procurado.
     * @return {@code true} se o elemento estiver presente, {@code false} caso contrário.
     */
    public boolean contains(T element) {
        Node<T> current = head;
        while (current != null) {
            if (current.getData().equals(element)) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    /**
     * Retorna o índice de um elemento, ou -1 se não for encontrado.
     *
     * @param element O elemento a ser procurado.
     * @return O índice do elemento, ou -1 se não for encontrado.
     */
    public int indexOf(T element) {
        Node<T> current = head;
        int index = 0;
        while (current != null) {
            if (current.getData().equals(element)) {
                return index;
            }
            current = current.getNext();
            index++;
        }
        return -1;
    }

    /**
     * Obtém o elemento em uma posição específica.
     *
     * @param index O índice do elemento desejado.
     * @return O elemento na posição especificada.
     * @throws IndexOutOfBoundsException Se o índice estiver fora do intervalo.
     */
    public T get(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Índice fora do intervalo");
        }

        Node<T> current = head;
        int currentIndex = 0;
        while (currentIndex < index) {
            current = current.getNext();
            currentIndex++;
        }
        return current.getData();
    }

    /**
     * Remove um elemento da lista.
     *
     * @param element O elemento a ser removido.
     * @return O elemento removido.
     * @throws EmptyListException Se a lista estiver vazia.
     * @throws RuntimeException   Se o elemento não for encontrado.
     */
    public T remove(T element) throws EmptyListException {
        if (this.head == null) {
            throw new EmptyListException("A lista está vazia");
        }

        boolean found = false;
        Node<T> current = this.head;
        Node<T> previous = null;

        while (current != null && !found) {
            if (element.equals(current.getData())) {
                found = true;
            } else {
                previous = current;
                current = current.getNext();
            }
        }

        if (!found) {
            throw new RuntimeException("Erro: O elemento não foi encontrado na lista.");
        }

        if (current == this.head) {
            this.head = current.getNext();
        } else {
            previous.setNext(current.getNext());
        }

        if (current == this.tail) {
            this.tail = previous;
        }

        this.count--;
        return current.getData();
    }

    /**
     * Retorna o número de elementos na lista.
     *
     * @return O número de elementos na lista.
     */
    public int size() {
        return count;
    }

    /**
     * Retorna uma representação em formato de string da lista.
     *
     * @return Uma string contendo os elementos da lista separados por espaços.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        Node<T> current = this.head;
        while (current != null) {
            result.append(current.getData()).append(" ");
            current = current.getNext();
        }
        return result.toString();
    }

    /**
     * Retorna um iterador para a lista.
     *
     * @return Um iterador para os elementos da lista.
     */
    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    /**
     * Classe interna para implementação do iterador da lista encadeada.
     */
    private class LinkedListIterator implements Iterator<T> {
        private Node<T> current;

        public LinkedListIterator() {
            this.current = head;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new RuntimeException("Fim da lista.");
            }
            T data = current.getData();
            current = current.getNext();
            return data;
        }
    }
}
