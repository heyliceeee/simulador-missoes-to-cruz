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

     // Verifica se a lista esta vazia
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

    /**
     * Define o valor de um elemento na posicao especificada.
     *
     * @param index O indice do elemento a ser atualizado.
     * @param element O novo valor do elemento.
     * @throws IndexOutOfBoundsException Se o indice estiver fora dos limites da lista.
     */
    public void setElementAt(int index, T element) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Indice fora dos limites: " + index);
        }

        Node<T> current = head; // Comeca pelo primeiro elemento
        int currentIndex = 0;

        while (current != null) {
            if (currentIndex == index) {
                current.setData(element); // Atualiza o elemento
                return;
            }
            current = current.getNext(); // Avanca para o proximo no
            currentIndex++;
        }

        throw new IllegalStateException("Erro inesperado ao procurar o indice.");
    }

    /**
     * Obtem o elemento na posicao especificada.
     *
     * @param index O indice do elemento desejado.
     * @return O elemento na posicao especificada.
     * @throws IndexOutOfBoundsException Se o indice estiver fora dos limites da lista.
     */
    public T getElementAt(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("indice fora dos limites: " + index);
        }

        Node<T> current = head; // Comeca pelo primeiro elemento
        int currentIndex = 0;

        while (current != null) {
            if (currentIndex == index) {
                return current.getData();
            }
            current = current.getNext(); // Avanca para o proximo no
            currentIndex++;
        }

        // Esta linha nao deve ser alcancada devido à verificacao do indice acima
        throw new IllegalStateException("Erro inesperado ao procurar o indice.");
    }


    /**
     * Verifica se a lista contem o elemento especificado.
     *
     * @param element O elemento a ser procurado.
     * @return true se o elemento estiver presente, false caso contrario.
     */
    // Verifica se um elemento esta na lista
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

    // Retorna o indice de um elemento, ou -1 se nao for encontrado
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

    // Retorna o elemento em uma posicao especifica
    public T get(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Indice fora do intervalo");
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
            throw new EmptyListException("A lista esta vazia");
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
            throw new RuntimeException("Erro: O elemento nao foi encontrado na lista.");
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

    // Retorna o numero de elementos na lista
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

    // Implementacao do metodo iterator()
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
