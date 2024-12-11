package org.example.collections.interfaces;

public interface BinarySearchTreeADT<T> extends BinaryTreeADT<T>
{
    /**
     * adiciona o elemento especifico no local adequado na árvore.
     * @param element o elemento a ser adicionado a árvore
     */
    public void addElement(T element);


    /**
     * remove e retorna o elemento especifico da árvore
     * @param targetElement o elemento a ser removido da árvore
     * @return o elemento da árvore
     */
    public T removeElement(T targetElement);


    /**
     * remove todas as ocorrencias do elemento especifico da árvore
     * @param targetElement o elemento que todas as ocorrencias serao removidas
     */
    public void removeAllOcurrences(T targetElement);


    /**
     * remove e retorna o elemento mais pequeno da árvore
     * @return o elemento mais pequeno da árvore
     */
    public T removeMin();


    /**
     * remove e retorna o elemento maior da árvore
     * @return o elemento maior da árvore
     */
    public T removeMax();


    /**
     * retorna uma referencia do elemento mais pequeno da árvore
     * @return uma referencia do elemento mais pequeno da árvore
     */
    public T findMin();


    /**
     * retorna uma referencia do elemento maior da árvore
     * @return uma referencia do elemento maior da árvore
     */
    public T findMax();
}
