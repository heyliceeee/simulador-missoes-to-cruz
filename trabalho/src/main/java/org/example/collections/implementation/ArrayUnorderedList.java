package org.example.collections.implementation;

import org.example.api.exceptions.ElementNotFoundException;
import org.example.collections.interfaces.UnorderedListADT;

public class ArrayUnorderedList<T> extends ArrayList<T> implements UnorderedListADT<T> {

    @Override
    public void addToFront(T element) {
        if (element == null) {
            throw new IllegalArgumentException("O valor não pode ser null.");
        }
        if (count == list.length) {
            expandCapacity();
        }

        // Desloca todos os elementos uma posição para a direita
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
            throw new IllegalArgumentException("O valor não pode ser null.");
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
            throw new ElementNotFoundException("Elemento alvo não encontrado.");
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

}
