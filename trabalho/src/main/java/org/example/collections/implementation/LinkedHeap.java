package org.example.collections.implementation;

import org.example.collections.exceptions.EmptyCollectionException;
import org.example.collections.interfaces.HeapADT;

public class LinkedHeap<T> extends LinkedBinaryTree<T> implements HeapADT<T>
{
    public HeapNode<T> lastNode;


    public LinkedHeap()
    {
        super();
    }


    /**
     * adiciona o elemento especifico no heap na posicao apropriado de acordo com key
     * nota que os elementos iguais serao adicionados a direita
     * @param obj o elemento a ser adicionado ao heap
     */
    @Override
    public void addElement(T obj)
    {
        HeapNode<T> node = new HeapNode<>(obj);

        if (root == null)
        {
            root = node;
        }
        else
        {
            HeapNode<T> next_parent = getNextParentAdd();

            if (next_parent.left == null)
            {
                next_parent.setLeft(node);
            }
            else
            {
                next_parent.setRight(node);
            }

            node.parent = next_parent;
        }

        lastNode = node;
        count++;

        if (count > 1)
        {
            heapifyAdd();
        }
    }


    /**
     * retorna o node que ira ser parent do novo node
     * @return o node que ira ser parent do novo node
     */
    private HeapNode<T> getNextParentAdd()
    {
        HeapNode<T> result = lastNode;

        //percorre a heap ate ao ultimo node
        while ((result != root) && (result.parent.left != result))
        {
            result = result.parent;
        }

        if (result != root)
        {
            if (result.parent.right == null)
            {
                result = result.parent;
            }
            else
            {
                result = (HeapNode<T>) result.parent.right;

                while (result.left != null)
                {
                    result = (HeapNode<T>) result.left;
                }
            }
        }
        else
        {
            while (result.left != null)
            {
                result = (HeapNode<T>) result.left;
            }
        }
        return result;
    }

    @Override
    public T removeMin()
    {
        if (isEmpty())
        {
            throw new EmptyCollectionException("Empty Heap");
        }

        T minElement = root.element;

        if (count == 1)
        {
            root = null;
            lastNode = null;
        }
        else
        {
            HeapNode<T> next_last = getNewLastNode();

            if (lastNode.parent.left == lastNode)
            {
                lastNode.parent.setLeft(null);
            }
            else
            {
                lastNode.parent.setRight(null);
            }

            root.setElement(lastNode.element);
            lastNode = next_last;
            heapifyRemove();
        }
        count--;

        return minElement;
    }

    @Override
    public T findMin()
    {
        if (isEmpty())
        {
            throw new EmptyCollectionException("A lista esta vazia");
        }

        return root.element;
    }


    /**
     * Retorna o node que sera o novo ultimo node apos uma remocao
     *
     * @return o node que sera o novo ultimo node apos uma remocao
     */
    private HeapNode<T> getNewLastNode()
    {
        HeapNode<T> result = lastNode;

        while ((result != root) && (result.parent.left == result))
        {
            result = result.parent;
        }

        if (result != root)
        {
            result = (HeapNode<T>) result.parent.left;
        }

        while (result.right != null)
        {
            result = (HeapNode<T>) result.right;
        }

        return result;
    }


    /**
     * Reordena este heap apos adicionar o elemento raiz
     */
    private void heapifyAdd()
    {
        T temp;
        HeapNode<T> next = this.lastNode;
        temp = next.element;

        while (next != root && ((Comparable) temp).compareTo(next.parent.element) < 0)
        {
            next.setElement(next.parent.element);
            next = next.parent;
        }

        next.setElement(temp);
    }


    /**
     * Reordena este heap apos remover o elemento raiz.
     */
    private void heapifyRemove()
    {
        T temp;
        //variaveis temporarias para a raiz e filhos da arvore
        HeapNode<T> node = (HeapNode<T>) root;
        HeapNode<T> left = (HeapNode<T>) node.left;
        HeapNode<T> right = (HeapNode<T>) node.right;
        HeapNode<T> next;

        if ((left == null) && (right == null))
        {
            next = null;
        }
        else if (left == null)
        {
            next = right;
        }
        else if (right == null)
        {
            next = left;
        }
        else if (((Comparable) left.element).compareTo(right.element) < 0)
        {
            next = left;
        }
        else
        {
            next = right;
        }

        temp = node.element;

        while ((next != null) && (((Comparable) next.element).compareTo(temp) < 0))
        {
            node.setElement(next.element);
            node = next;
            left = (HeapNode<T>) node.left;
            right = (HeapNode<T>) node.right;

            if ((left == null) && (right == null))
            {
                next = null;
            }
            else if (left == null)
            {
                next = right;
            }
            else if (right == null)
            {
                next = left;
            }
            else if (((Comparable) left.element).compareTo(right.element) < 0)
            {
                next = left;
            }
            else
            {
                next = right;
            }
        }
        node.setElement(temp);
    }
}
