package org.example.collections.implementation;

import org.example.collections.exceptions.EmptyCollectionException;
import org.example.collections.interfaces.StackADT;

public class ArrayStack<T> implements StackADT<T> {

    /**
     * constante para representar a capacidade default do array
     */
    protected final int DEFAULT_CAPACITY = 10;

    /**
     * int que representa o número de elementos e a seguinte posição disponível no array
     */
    protected int top;

    /**
     * array de elementos genéricos que representam a stack
     */
    protected T[] stack;


    /**
     * cria uma stack vazia utilizando a capacidade default
     */
    public ArrayStack(){
        top = 0;
        stack = (T[]) (new Object[DEFAULT_CAPACITY]);
    }

    /**
     * cria uma stack vazia utilizando uma capacidade específica
     * @param initialCapacity representa a capacidade específica
     */
    public ArrayStack(int initialCapacity){
        top = 0;
        stack = (T[]) (new Object[initialCapacity]);
    }


    /**
     * adiciona um elemento específico ao top da stack, expandindo a capacidade da stack se necessário
     * @param element elemento genérico a ser colocado na stack
     */
    @Override
    public void push(T element) {
        if(size() == stack.length) { // se a stack já atingiu a capacidade máxima
            expandCapacity();
        }

        stack[top] = element; // o novo elemento fica no top
        top++; // top aponta para o elemento seguinte (vazio)
    }

    /**
     * expande a capacidade da stack
     */
    private void expandCapacity() {
        T[] newStack = (T[]) new Object[stack.length * 2]; // cria uma nova stack com o dobro da capacidade

        // copiar os elementos da stack anterior para a nova stack
        for(int i = 0; i < stack.length; i++) {
            newStack[i] = stack[i];
        }

        stack = newStack; // a stack agora é a stack expandida
    }

    /**
     * remove o elemento do top da stack e retorna a referência dele
     * lança uma EmptyCollectionException se a stack estiver vazia
     * @return T elemento removido do top da stack
     * @throws EmptyCollectionException se a remoção foi tentada numa stack vazia
     */
    @Override
    public T pop() throws EmptyCollectionException {
        if(isEmpty()) {
            throw new EmptyCollectionException("Stack");
        }

        top--; // top aponta para o elemento anterior

        T result = stack[top]; // elemento top
        stack[top] = null; // elemento top removido

        return result;
    }

    /**
     * retorna uma referência ao elemento do top da stack. O elemento não é removido da stack
     * lança uma EmptyCollectionException se a stack estiver vazia
     * @return T elemento do top da stack
     * @throws EmptyCollectionException se a observação foi tentada numa stack vazia
     */
    @Override
    public T peek() throws EmptyCollectionException {
        if(isEmpty()) {
            throw new EmptyCollectionException("Stack");
        }

        return stack[top - 1]; // elemento top
    }

    /**
     * retorna true se a stack não contiver elementos
     * @return boolean dependendo se a stack está vazia
     */
    @Override
    public boolean isEmpty() {
        return top == 0;
    }

    /**
     * retorna o número de elementos da stack
     * @return int número de elementos da stack
     */
    @Override
    public int size() {
        return top;
    }

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
