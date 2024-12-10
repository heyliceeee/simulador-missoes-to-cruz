package org.example.collections.implementation;

import java.util.Iterator;

import org.example.collections.exceptions.EmptyListException;

public class LinkedList<T> implements Iterable<T> {
    private Node<T> head;
    private Node<T> tail;
    private int count;

    public LinkedList() {
        this.head = null;
        this.tail = null;
        this.count = 0;
    }

    // Adiciona um elemento à lista
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

     // Verifica se a lista está vazia
     public boolean isEmpty() {
        return count == 0;
    }

    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.setNext(head);
        head = newNode;
    
        if (tail == null) {
            tail = newNode;
        }
    
        count++;
    }
    

    // Verifica se um elemento está na lista
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

    // Retorna o índice de um elemento, ou -1 se não for encontrado
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

    // Retorna o elemento em uma posição específica
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

    // Remove um elemento da lista
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

    // Retorna o número de elementos na lista
    public int size() {
        return count;
    }

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

    // Implementação do método iterator()
    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    // Classe interna para o iterador
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
