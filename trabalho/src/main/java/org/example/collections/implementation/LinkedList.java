package org.example.collections.implementation;

import java.util.Iterator;

public class LinkedList<T> implements Iterable<T>
{
    private LinkedListNode<T> head;
    private LinkedListNode<T> tail;

    private SentinelNode<T> sentinel;
    private int size;

    public LinkedList(){
        this.size = 0;
        this.head = null;
        sentinel = new SentinelNode<T>(null);
    }


    public int getSize()
    {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    /**
     * adicionar a lista utilizando node sentinela
     * @param element
     */
    public void addSentinel(T element){
        SentinelNode<T> newNode = new SentinelNode<T>(element); //criar um novo node com os dados
        SentinelNode<T> current = sentinel; //elemento

        while (current.getNext() != null){ //vai correr a lista até á cauda (último elemento)
            current = current.getNext(); //vai para o seguinte elemento
        }

        current.setNext(newNode); //adicionar um novo node á cauda (último elemento)
        size++;
    }

    public void add(T element){
        LinkedListNode<T> newNode = new LinkedListNode<T>(element); //criar um novo node com os dados

        if(head == null){ //se a lista estiver vazia
            head = newNode; //o novo node fica na cabeca (torna-se o primeiro)
            tail = newNode; //o novo node fica na cauda (torna-se o último)

            size++;

        } else { //se a lista NAO estiver vazia
            tail.setNext(newNode); //adicionar um novo node ao próximo elemento de tail
            tail = newNode; //tail fica com o valor do novo node

            size++;
        }
    }

    /**
     * Define o valor de um elemento na posição especificada.
     *
     * @param index O índice do elemento a ser atualizado.
     * @param element O novo valor do elemento.
     * @throws IndexOutOfBoundsException Se o índice estiver fora dos limites da lista.
     */
    public void setElementAt(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Indice fora dos limites: " + index);
        }

        LinkedListNode<T> current = head; // Começa pelo primeiro elemento
        int currentIndex = 0;

        while (current != null) {
            if (currentIndex == index) {
                current.setElement(element); // Atualiza o elemento
                return;
            }
            current = current.getNext(); // Avança para o próximo nó
            currentIndex++;
        }

        throw new IllegalStateException("Erro inesperado ao procurar o indice.");
    }


    /**
     * remover node sentinela
     * @param element
     * @return
     */
    public boolean removeSentinel(T element) {
        //remover um elemento do meio ou da cauda da lista
        SentinelNode<T> current = sentinel;  //elemento

        while (current.getNext() != null && !current.getNext().getElement().equals(element)){ //vai correr a lista até ao último elemento, se for necessário, até encontrar o elemento
            current = current.getNext(); //vai para o seguinte elemento
        }

        //se encontrou o elemento
        if(current.getNext() != null) {
            current.setNext(current.getNext().getNext()); //o elemento seguinte será o seguinte do seguinte elemento
            return true;
        }

        return false; //elemento não encontrado, ou seja, não removido
    }

    /**
     * Obtém o elemento na posição especificada.
     *
     * @param index O índice do elemento desejado.
     * @return O elemento na posição especificada.
     * @throws IndexOutOfBoundsException Se o índice estiver fora dos limites da lista.
     */
    public T getElementAt(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("indice fora dos limites: " + index);
        }

        LinkedListNode<T> current = head; // Começa pelo primeiro elemento
        int currentIndex = 0;

        while (current != null) {
            if (currentIndex == index) {
                return current.getElement();
            }
            current = current.getNext(); // Avança para o próximo nó
            currentIndex++;
        }

        // Esta linha não deve ser alcançada devido à verificação do índice acima
        throw new IllegalStateException("Erro inesperado ao procurar o indice.");
    }


    public T remove(T element) {

        if(head == null || size == 0){ //lista vazia
            return null;
        }

        if(head.getElement().equals(element)){ //remover o primeiro elemento

            T removedElement = head.getElement();

            head = head.getNext(); //cabeca fica com o valor do segundo elemento (agora é o primeiro elemento porque o primeiro elemento foi removido)

            size--;

            return removedElement; //elemento removido
        }

        //remover um elemento do meio ou da cauda da lista
        LinkedListNode<T> current = head;  //elemento da cabeca (primeiro elemento)
        while (current.getNext() != null && !current.getNext().getElement().equals(element)){  //vai correr a lista até á cauda (último elemento) se for necessário até encontrar o elemento
            current = current.getNext(); //vai para o seguinte elemento
        }

        //se encontrou o elemento
        if(current.getNext() != null) {

            T removedElement = current.getNext().getElement();

            current.setNext(current.getNext().getNext()); //o elemento seguinte será o seguinte do seguinte elemento

            size--;

            return removedElement;
        }

        return null; //elemento não encontrado, ou seja, não removido
    }

    /**
     * Verifica se a lista contém o elemento especificado.
     *
     * @param element O elemento a ser procurado.
     * @return true se o elemento estiver presente, false caso contrário.
     */
    public boolean contains(T element) {
        if (head == null) { // Verifica se a lista está vazia
            return false;
        }

        LinkedListNode<T> current = head; // Inicia no primeiro nó
        while (current != null) {
            if (current.getElement().equals(element)) { // Compara o elemento atual com o procurado
                return true;
            }
            current = current.getNext(); // Avança para o próximo nó
        }

        return false; // Elemento não encontrado
    }

    /**
     * Remove todos os elementos da lista.
     */
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }


    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private LinkedListNode<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                T element = current.getElement();
                current = current.getNext();
                return element;
            }
        };
    }

    /**
     * mostrar
     */
    public void printSentinel() {
        SentinelNode<T> current = sentinel; //elemento da cabeca (primeiro elemento)

        System.out.print("LinkedList Sentinel [");

        while (current != null){ //corre a lista toda
            System.out.print(current.getElement() + ", ");
            current = current.getNext(); //vai para o seguinte elemento
        }

        System.out.print("]\n");
    }


       /**
     * Verifica se a lista está vazia.
     *
     * @return true se a lista estiver vazia, false caso contrário.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * mostrar
     */
    public void print() {
        LinkedListNode<T> current = head; //elemento da cabeca (primeiro elemento)

        System.out.print("LinkedList [");

        while (current != null){ //corre a lista toda
            System.out.print(current.getElement() + ", ");
            current = current.getNext(); //vai para o seguinte elemento
        }

        System.out.print("]\n");
    }


    @Override
    public String toString() {
        return "LinkedList{" +
                "head=" + head +
                ", tail=" + tail +
                ", sentinel=" + sentinel +
                ", size=" + size +
                '}';
    }

    /**
     * Retorna o índice da primeira ocorrência de um elemento na lista.
     *
     * @param element O elemento a ser procurado.
     * @return O índice do elemento, ou -1 se o elemento não for encontrado.
     */
    public int indexOf(T element) {
        LinkedListNode<T> current = head; // Inicia no primeiro nó
        int index = 0;

        while (current != null) { // Percorre todos os nós
            if (current.getElement().equals(element)) { // Compara o elemento atual com o procurado
                return index;
            }
            current = current.getNext(); // Avança para o próximo nó
            index++;
        }

        return -1; // Retorna -1 se o elemento não for encontrado
    }
}

