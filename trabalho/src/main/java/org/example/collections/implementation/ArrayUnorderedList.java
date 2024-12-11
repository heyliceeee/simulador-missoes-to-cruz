package org.example.collections.implementation;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.collections.interfaces.UnorderedListADT;

public class ArrayUnorderedList<T> extends ArrayList<T> implements UnorderedListADT<T> {

    @Override
    public void addToFront(T element) {
        if (element == null) {
            throw new IllegalArgumentException("O valor nao pode ser null.");
        }
        if (count == list.length) {
            expandCapacity();
        }

        // Desloca todos os elementos uma posicao para a direita
        for (int i = count; i > 0; i--) {
            list[i] = list[i - 1];
        }

        // Adiciona o novo elemento na frente
        list[0] = element;
        count++;
    }

    @Override
    public void addToRear(T element) {
        if (element == null) {
            throw new IllegalArgumentException("O valor nao pode ser null.");
        }
        if (count == list.length) {
            expandCapacity();
        }

        list[count] = element;
        count++;

    }

    @Override
    public void addAfter(T element, T target) throws ElementNotFoundException {

        if (isEmpty()) {
            throw new ElementNotFoundException("A lista está vazia.");
        }

        int index = -1;
        for (int i = 0; i < count; i++) {
            if (list[i].equals(target)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            throw new ElementNotFoundException("Elemento alvo nao encontrado.");
        }

        if (count == list.length) {
            expandCapacity();
        }

        for (int i = count; i > index + 1; i--) {
            list[i] = list[i - 1];
        }

        // Adiciona o novo elemento após o elemento alvo
        list[index + 1] = element;
        count++;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < count; i++) {
            sb.append(list[i]);
            if (i < count - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public void setElementAt(int index, T element) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("O indice está fora dos limites da lista.");
        }
        if (element == null) {
            throw new IllegalArgumentException("O valor nao pode ser null.");
        }
        list[index] = element;
    }

    @Override
    public int indexOf(T element) {
        if (element == null) {
            throw new IllegalArgumentException("O valor nao pode ser null.");
        }

        for (int i = 0; i < count; i++) {
            if (list[i].equals(element)) {
                return i; // Retorna o indice do elemento
            }
        }

        return -1; // Retorna -1 se o elemento nao for encontrado
    }
}
