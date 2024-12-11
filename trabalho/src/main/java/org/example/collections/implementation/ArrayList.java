package org.example.collections.implementation;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.collections.interfaces.ListADT;

public abstract class ArrayList<T> implements ListADT<T> {

    protected T[] list;
    protected int count;
    protected int modCount;
    protected final int DEFAULT_CAPACITY = 10;

    public ArrayList() {
        list = (T[]) new Object[DEFAULT_CAPACITY];
        count = 0;
        modCount = 0;
    }

    public T[] getList() {
        return Arrays.copyOf(list, count);
    }

    public int getCount() {
        return count;
    }

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

    @Override
    public boolean contains(T target) {
        for (int i = 0; i < count; i++) {
            if (list[i].equals(target)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public T first() {
        if (isEmpty()) {
            return null;
        }

        return list[0];
    }

    @Override
    public T last() {
        if (isEmpty()) {
            return null;
        }

        return list[count - 1];
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<T> {

        private int currentIndex = 0;
        private int expectedModCount = modCount; // captura o modCount atual
        private boolean canRemove = false; // controle para o metodo remove

        @Override
        public boolean hasNext() {
            return currentIndex < count;
        }

        @Override
        public T next() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException("A lista foi modificada.");
            }
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            canRemove = true; // podemos remover após chamar next()
            return list[currentIndex++];
        }

        @Override
        public void remove() {
            if (!canRemove) {
                throw new IllegalStateException("Metodo next() nao foi chamado ou já foi chamado remove().");
            }
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException("A lista foi modificada.");
            }

            // Remove o elemento atual (currentIndex - 1)
            for (int i = currentIndex - 1; i < count - 1; i++) {
                list[i] = list[i + 1];
            }

            list[count - 1] = null; // limpe a última posicao
            count--;
            modCount++; // incrementa o contador de modificacões
            expectedModCount++; // atualiza o esperado
            canRemove = false; // reseta o controle de remocao
        }
    }


    public T getElementAt(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Indice fora do intervalo: " + index);
        }
        return list[index];
    }

    protected void expandCapacity() {
        list = Arrays.copyOf(list, list.length * 2);
    }

}
