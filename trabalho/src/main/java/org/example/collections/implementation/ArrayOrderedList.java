package org.example.collections.implementation;

import org.example.collections.interfaces.OrderedListADT;

import java.util.Comparator;

public class ArrayOrderedList<T> extends ArrayList<T> implements OrderedListADT<T> {

    private Comparator<T> comparator;

    public ArrayOrderedList(Comparator<T> comparator) {
        super();
        this.comparator = comparator;
    }

    @Override
    public void add(T element) {
        if (count == list.length) {
            expandCapacity();
        }

        int position = 0;
        while (position < count && comparator.compare(list[position], element) < 0) {
            position++;
        }

        for (int i = count; i > position; i--) {
            list[i] = list[i - 1];
        }

        list[position] = element;
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
