package org.example.collections.implementation;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.collections.interfaces.ListADT;

/**
 * Classe abstrata que implementa uma lista baseada em array.
 *
 * @param <T> O tipo de elementos armazenados na lista.
 */
public abstract class ArrayList<T> implements ListADT<T> {

    /**
     * Array que armazena os elementos da lista.
     */
    protected T[] list;

    /**
     * Numero de elementos atualmente armazenados na lista.
     */
    protected int count;

    /**
     * Contador de modificacoes para controlar a concorrencia.
     */
    protected int modCount;

    protected final int DEFAULT_CAPACITY = 10;

    /**
     * Construtor padrao que inicializa a lista com a capacidade padrao.
     */
    public ArrayList() {
        list = (T[]) new Object[DEFAULT_CAPACITY];
        count = 0;
        modCount = 0;
    }

    /**
     * Retorna uma copia dos elementos armazenados na lista.
     *
     * @return Um array contendo os elementos da lista.
     */
    public T[] getList() {
        return Arrays.copyOf(list, count);
    }

    /**
     * Retorna o numero de elementos na lista.
     *
     * @return O numero de elementos atualmente armazenados na lista.
     */
    public int getCount() {
        return count;
    }

    /**
     * Remove e retorna o primeiro elemento da lista.
     *
     * @return O primeiro elemento da lista.
     * @throws ElementNotFoundException se a lista estiver vazia.
     */
    @Override
    public T removeFirst() throws ElementNotFoundException {
        if (isEmpty()) {
            throw new ElementNotFoundException("A lista esta vazia.");
        }

        T result = list[0];
        for (int i = 0; i < count - 1; i++) {
            list[i] = list[i + 1];
        }

        list[count - 1] = null;
        count--;
        return result;
    }

    /**
     * Remove e retorna o ultimo elemento da lista.
     *
     * @return O ultimo elemento da lista.
     * @throws ElementNotFoundException se a lista estiver vazia.
     */
    @Override
    public T removeLast() throws ElementNotFoundException {
        if (isEmpty()) {
            throw new ElementNotFoundException("A lista esta vazia.");
        }
        T result = list[count - 1];
        list[count - 1] = null;
        count--;

        return result;
    }

    /**
     * Remove e retorna o elemento especificado na lista.
     *
     * @param element O elemento a ser removido.
     * @return O elemento removido.
     * @throws ElementNotFoundException se o elemento nao for encontrado ou se a
     *                                  lista estiver vazia.
     */
    @Override
    public T remove(T element) throws ElementNotFoundException {
        if (isEmpty()) {
            throw new ElementNotFoundException("A lista esta vazia.");
        }

        int index = -1;
        for (int i = 0; i < count; i++) {
            if (list[i].equals(element)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            throw new ElementNotFoundException("O elemento nao encontrado");
        }

        T removedElement = list[index];

        for (int i = index; i < count - 1; i++) {
            list[i] = list[i + 1];
        }

        list[count - 1] = null;
        count--;

        return removedElement;
    }

    /**
     * Verifica se a lista contem o elemento especificado.
     *
     * @param target O elemento a ser verificado.
     * @return {@code true} se o elemento estiver na lista, {@code false} caso
     *         contrario.
     */
    @Override
    public boolean contains(T target) {
        for (int i = 0; i < count; i++) {
            if (list[i].equals(target)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se a lista esta vazia.
     *
     * @return {@code true} se a lista estiver vazia, {@code false} caso contrario.
     */
    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * Retorna o numero de elementos na lista.
     *
     * @return O numero de elementos na lista.
     */
    @Override
    public int size() {
        return count;
    }

    /**
     * Retorna o primeiro elemento da lista.
     *
     * @return O primeiro elemento da lista ou {@code null} se a lista estiver
     *         vazia.
     */
    @Override
    public T first() {
        if (isEmpty()) {
            return null;
        }

        return list[0];
    }

    /**
     * Retorna o ultimo elemento da lista.
     *
     * @return O ultimo elemento da lista ou {@code null} se a lista estiver vazia.
     */
    @Override
    public T last() {
        if (isEmpty()) {
            return null;
        }

        return list[count - 1];
    }

    /**
     * Retorna um iterador para os elementos da lista.
     *
     * @return Um iterador para os elementos da lista.
     */
    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator();
    }

    /**
     * Classe interna que implementa um iterador para a lista.
     */
    private class ArrayIterator implements Iterator<T> {

        /**
         * indice atual do iterador.
         */
        private int currentIndex = 0;

        /**
         * ModCount esperado para deteccao de modificacoes na lista.
         */
        private int expectedModCount = modCount;

        /**
         * Indica se o metodo {@code next()} foi chamado antes de {@code remove()}.
         */
        private boolean canRemove = false;

        /**
         * Verifica se ha elementos restantes no iterador.
         *
         * @return {@code true} se houver elementos restantes, {@code false} caso
         *         contrario.
         */
        @Override
        public boolean hasNext() {
            return currentIndex < count;
        }

        /**
         * Retorna o proximo elemento no iterador.
         *
         * @return O proximo elemento.
         * @throws ConcurrentModificationException se a lista foi modificada durante a
         *                                         iteracao.
         * @throws NoSuchElementException          se nao houver mais elementos.
         */
        @Override
        public T next() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException("A lista foi modificada.");
            }
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            canRemove = true; // podemos remover apos chamar next()
            return list[currentIndex++];
        }

        /**
         * Remove o ultimo elemento retornado pelo iterador.
         *
         * @throws IllegalStateException           se o metodo {@code next()} nao foi
         *                                         chamado ou {@code remove()} ja foi
         *                                         chamado.
         * @throws ConcurrentModificationException se a lista foi modificada durante a
         *                                         iteracao.
         */
        @Override
        public void remove() {
            if (!canRemove) {
                throw new IllegalStateException("Metodo next() nao foi chamado ou ja foi chamado remove().");
            }
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException("A lista foi modificada.");
            }

            // Remove o elemento atual (currentIndex - 1)
            for (int i = currentIndex - 1; i < count - 1; i++) {
                list[i] = list[i + 1];
            }

            list[count - 1] = null; // limpe a ultima posicao
            count--;
            modCount++; // incrementa o contador de modificacoes
            expectedModCount++; // atualiza o esperado
            canRemove = false; // reseta o controle de remocao
        }
    }

    /**
     * Retorna o elemento no indice especificado.
     *
     * @param index O indice do elemento.
     * @return O elemento no indice especificado.
     * @throws IndexOutOfBoundsException se o indice estiver fora do intervalo
     *                                   valido.
     */
    public T getElementAt(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Indice fora do intervalo: " + index);
        }
        return list[index];
    }

    /**
     * Expande a capacidade do array subjacente, dobrando seu tamanho atual.
     */
    protected void expandCapacity() {
        list = Arrays.copyOf(list, list.length * 2);
    }

}
