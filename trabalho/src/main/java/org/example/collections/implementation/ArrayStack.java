package org.example.collections.implementation;

import org.example.collections.exceptions.EmptyCollectionException;
import org.example.collections.interfaces.StackADT;

/**
 * Classe que implementa a interface {@link StackADT} utilizando um array para armazenar elementos.
 * Esta classe oferece operações básicas de uma pilha (stack): empilhar (push), desempilhar (pop),
 * observar o elemento do topo (peek), verificar se está vazia e obter o tamanho. Caso a capacidade
 * do array seja alcançada, o array será redimensionado automaticamente, dobrando seu tamanho.
 *
 * <p><b>Invariantes da classe:</b></p>
 * <ul>
 *   <li>A capacidade do array é sempre maior ou igual à quantidade de elementos armazenados.</li>
 *   <li>O atributo {@code top} aponta para a próxima posição livre no array, ou seja, o índice
 *       logo após o último elemento armazenado.</li>
 *   <li>Quando a pilha está vazia, {@code top == 0}.</li>
 *   <li>Os elementos são armazenados do índice 0 até o índice {@code top-1} do array.</li>
 * </ul>
 *
 * @param <T> o tipo de elementos armazenados na pilha
 */
public class ArrayStack<T> implements StackADT<T> {

    /**
     * Capacidade padrão inicial do array.
     */
    protected final int DEFAULT_CAPACITY = 10;

    /**
     * Índice que representa tanto o número de elementos na pilha quanto a próxima posição livre no array.
     * Quando a pilha está vazia, top = 0.
     * Quando a pilha possui n elementos, top = n.
     */
    protected int top;

    /**
     * Array genérico que representa a pilha internamente.
     * Os elementos são empilhados do índice 0 até top-1.
     */
    protected T[] stack;


    /**
     * Cria uma pilha vazia utilizando a capacidade padrão {@link #DEFAULT_CAPACITY}.
     * Inicialmente, {@code top = 0} e o array possui 10 posições.
     */
    @SuppressWarnings("unchecked")
    public ArrayStack() {
        top = 0;
        stack = (T[]) (new Object[DEFAULT_CAPACITY]);
    }

    /**
     * Cria uma pilha vazia com uma capacidade inicial específica.
     *
     * @param initialCapacity capacidade inicial da pilha.
     *                        Se {@code initialCapacity} for menor ou igual a zero, o comportamento 
     *                        será indefinido (recomendado fornecer um valor positivo).
     */
    @SuppressWarnings("unchecked")
    public ArrayStack(int initialCapacity) {
        top = 0;
        stack = (T[]) (new Object[initialCapacity]);
    }


    /**
     * Empilha (adiciona) um elemento no topo da pilha.
     * Se a pilha estiver cheia, a capacidade será expandida automaticamente para o dobro.
     *
     * @param element elemento a ser empilhado.
     */
    @Override
    public void push(T element) {
        if (size() == stack.length) {
            expandCapacity();
        }

        stack[top] = element;
        top++;
    }

    /**
     * Expande a capacidade da pilha dobrando o tamanho do array interno.
     * Todos os elementos já armazenados são copiados para o novo array.
     */
    @SuppressWarnings("unchecked")
    private void expandCapacity() {
        T[] newStack = (T[]) new Object[stack.length * 2];

        for (int i = 0; i < stack.length; i++) {
            newStack[i] = stack[i];
        }

        stack = newStack;
    }

    /**
     * Desempilha (remove) e retorna o elemento no topo da pilha.
     * Lança uma {@link EmptyCollectionException} se a pilha estiver vazia.
     *
     * @return o elemento que estava no topo da pilha.
     * @throws EmptyCollectionException se a pilha estiver vazia.
     */
    @Override
    public T pop() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Stack");
        }

        top--;
        T result = stack[top];
        stack[top] = null; // Remove a referência para ajudar o coletor de lixo
        return result;
    }

    /**
     * Retorna (mas não remove) o elemento do topo da pilha.
     * Lança uma {@link EmptyCollectionException} se a pilha estiver vazia.
     *
     * @return o elemento do topo da pilha.
     * @throws EmptyCollectionException se a pilha estiver vazia.
     */
    @Override
    public T peek() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Stack");
        }

        return stack[top - 1];
    }

    /**
     * Verifica se a pilha está vazia.
     *
     * @return {@code true} se a pilha não contiver elementos, {@code false} caso contrário.
     */
    @Override
    public boolean isEmpty() {
        return top == 0;
    }

    /**
     * Retorna o número de elementos contidos na pilha.
     *
     * @return a quantidade de elementos na pilha.
     */
    @Override
    public int size() {
        return top;
    }

    /**
     * Retorna uma representação em String da pilha, exibindo valores internos 
     * para fins de debug, incluindo a capacidade padrão, o índice top e todos os elementos do array.
     *
     * @return Uma string representando o estado interno da pilha.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ArrayStack{");
        sb.append("DEFAULT_CAPACITY=").append(DEFAULT_CAPACITY);
        sb.append(", top=").append(top);
        sb.append(", stack=");

        sb.append("[");
        for (int i = 0; i < stack.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(stack[i]);
        }
        sb.append("]");

        sb.append('}');
        return sb.toString();
    }
}
