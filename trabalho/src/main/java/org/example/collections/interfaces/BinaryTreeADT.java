package org.example.collections.interfaces;

import java.util.Iterator;

public interface BinaryTreeADT<T>
{
    /**
     * retorna uma referencia do elemento root
     * @return uma referencia do elemento root
     */
    public T getRoot();

    /**
     * retorna true se a arvore binaria estiver vazia, caso contrario retorna falso
     * @return true se a arvore binaria estiver vazia
     */
    public boolean isEmpty();

    /**
     * retorna o numero de elementos da arvore binaria
     * @return o numero de elementos da arvore
     */
    public int size();

    /**
     * retorna true se a arvore binaria conter o elemento procurado, caso contrario retorna false
     * @param targetElement o elemento a ser procurado
     * @return true se a arvore conter o elemento procurado
     */
    public boolean contains(T targetElement);

    /**
     * retorna uma referencia de um especifico elemento se este for encontrado na arvore binaria, caso o elemento nao for encontrado, retorna uma excecao
     * @param targetElement o elemento a ser procurado na arvore
     * @return uma referencia de um especifico elemento se este for encontrado na arvore
     */
    public T find(T targetElement);

    /**
     * retorna a arvore binaria
     * @return a arvore
     */
    public String toString();

    /**
     * executa uma travessia inorder na arvore binaria chamando um metodo inorder recursivo sobrecarregando que comeca com a root
     * @return um iterador sobre os elementos da arvore
     */
    public Iterator<T> iteratorInOrder();

   /**
     * executa uma travessia preorder na arvore binaria chamando um metodo preorder recursivo sobrecarregando que comeca com a root
     * @return um iterador sobre os elementos da arvore
     */
    public Iterator<T> iteratorPreOrder();

    /**
     * executa uma travessia postorder na arvore binaria chamando um metodo postorder recursivo sobrecarregado que comeca com a root
     * @return um iterador sobre os elementos da arvore
     */
    public Iterator<T> iteratorPostOrder();


    /**
     * executa uma travessia levelorder na arvore binaria utilizando uma queue
     * @return um iterador sobre os elementos da arvore
     */
    public Iterator<T> iteratorLevelOrder();
}
