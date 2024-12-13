package org.example.collections.interfaces;

public interface BinarySearchTreeADT<T> extends BinaryTreeADT<T>
{
    /**
     * adiciona o elemento especifico no local adequado na arvore.
     * @param element o elemento a ser adicionado a arvore
     */
    public void addElement(T element);


    /**
     * remove e retorna o elemento especifico da arvore
     * @param targetElement o elemento a ser removido da arvore
     * @return o elemento da arvore
     */
    public T removeElement(T targetElement);


    /**
     * remove todas as ocorrencias do elemento especifico da arvore
     * @param targetElement o elemento que todas as ocorrencias serao removidas
     */
    public void removeAllOcurrences(T targetElement);


    /**
     * remove e retorna o elemento mais pequeno da arvore
     * @return o elemento mais pequeno da arvore
     */
    public T removeMin();


    /**
     * remove e retorna o elemento maior da arvore
     * @return o elemento maior da arvore
     */
    public T removeMax();


    /**
     * retorna uma referencia do elemento mais pequeno da arvore
     * @return uma referencia do elemento mais pequeno da arvore
     */
    public T findMin();


    /**
     * retorna uma referencia do elemento maior da arvore
     * @return uma referencia do elemento maior da arvore
     */
    public T findMax();
}
