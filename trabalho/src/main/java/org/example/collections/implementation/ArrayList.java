package org.example.collections.implementation;

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
     * Número de elementos atualmente armazenados na lista.
     */
    protected int count;

    /**
     * Contador de modificações para controlar a concorrência.
     */
    protected int modCount;

    protected final int DEFAULT_CAPACITY = 10;

    /**
     * Construtor padrão que inicializa a lista com a capacidade padrão.
     */
    public ArrayList() {
        list = (T[]) new Object[DEFAULT_CAPACITY];
        count = 0;
        modCount = 0;
    }

    /**
     * Retorna uma cópia dos elementos armazenados na lista.
     *
     * @return Um array contendo os elementos da lista.
     */
    public T[] getList() {
        T[] copy = (T[]) new Object[count];
        System.arraycopy(list, 0, copy, 0, count);
        return copy;
    }

    /**
     * Retorna o número de elementos na lista.
     *
     * @return O número de elementos atualmente armazenados na lista.
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
            throw new ElementNotFoundException("A lista está vazia.");
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
     * Remove e retorna o último elemento da lista.
     *
     * @return O último elemento da lista.
     * @throws ElementNotFoundException se a lista estiver vazia.
     */
    @Override
    public T removeLast() throws ElementNotFoundException {
        if (isEmpty()) {
            throw new ElementNotFoundException("A lista está vazia.");
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
     * @throws ElementNotFoundException se o elemento não for encontrado ou se a
     *                                  lista estiver vazia.
     */
    @Override
    public T remove(T element) throws ElementNotFoundException {
        if (isEmpty()) {
            throw new ElementNotFoundException("A lista está vazia.");
        }

        int index = -1;
        for (int i = 0; i < count; i++) {
            if (list[i].equals(element)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            throw new ElementNotFoundException("O elemento não foi encontrado.");
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
     * Verifica se a lista contém o elemento especificado.
     *
     * @param target O elemento a ser verificado.
     * @return {@code true} se o elemento estiver na lista, {@code false} caso
     *         contrário.
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
     * Verifica se a lista está vazia.
     *
     * @return {@code true} se a lista estiver vazia, {@code false} caso contrário.
     */
    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * Retorna o número de elementos na lista.
     *
     * @return O número de elementos na lista.
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
     * Retorna o último elemento da lista.
     *
     * @return O último elemento da lista ou {@code null} se a lista estiver vazia.
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
         * Índice atual do iterador.
         */
        private int currentIndex = 0;

        /**
         * ModCount esperado para detecção de modificações na lista.
         */
        private int expectedModCount = modCount;

        /**
         * Indica se o método {@code next()} foi chamado antes de {@code remove()}.
         */
        private boolean canRemove = false;

        /**
         * Verifica se há elementos restantes no iterador.
         *
         * @return {@code true} se houver elementos restantes, {@code false} caso
         *         contrário.
         */
        @Override
        public boolean hasNext() {
            return currentIndex < count;
        }

        /**
         * Retorna o próximo elemento no iterador.
         *
         * @return O próximo elemento.
         * @throws ConcurrentModificationException se a lista foi modificada durante a
         *                                         iteração.
         * @throws NoSuchElementException          se não houver mais elementos.
         */
        @Override
        public T next() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException("A lista foi modificada.");
            }
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            canRemove = true;
            return list[currentIndex++];
        }

        /**
         * Remove o último elemento retornado pelo iterador.
         *
         * @throws IllegalStateException           se o método {@code next()} não foi
         *                                         chamado ou {@code remove()} já foi
         *                                         chamado.
         * @throws ConcurrentModificationException se a lista foi modificada durante a
         *                                         iteração.
         */
        @Override
        public void remove() {
            if (!canRemove) {
                throw new IllegalStateException("Método next() não foi chamado ou remove() já foi executado.");
            }
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException("A lista foi modificada.");
            }

            // Remove o elemento atual (currentIndex - 1)
            for (int i = currentIndex - 1; i < count - 1; i++) {
                list[i] = list[i + 1];
            }

            list[count - 1] = null;
            count--;
            modCount++;
            expectedModCount++;
            canRemove = false;
            currentIndex--;
        }
    }

    /**
     * Retorna o elemento no índice especificado.
     *
     * @param index O índice do elemento.
     * @return O elemento no índice especificado.
     * @throws IndexOutOfBoundsException se o índice estiver fora do intervalo
     *                                   válido.
     */
    public T getElementAt(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Índice fora do intervalo: " + index);
        }
        return list[index];
    }

    /**
     * Expande a capacidade do array subjacente, dobrando seu tamanho atual.
     */
    protected void expandCapacity() {
        T[] newArray = (T[]) new Object[list.length * 2];
        System.arraycopy(list, 0, newArray, 0, count);
        list = newArray;
    }

}
