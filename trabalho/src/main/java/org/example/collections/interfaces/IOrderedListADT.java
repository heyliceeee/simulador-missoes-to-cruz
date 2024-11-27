package org.example.collections.interfaces;

public interface IOrderedListADT<T> extends IListADT<T>
{
    /**
     * adiciona um elemento especifico na lista
     * @param element o elemento a ser adicionado
     */
    public void add(T element);
}
