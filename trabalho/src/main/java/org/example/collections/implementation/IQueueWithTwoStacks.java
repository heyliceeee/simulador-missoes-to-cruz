package org.example.collections.implementation;

import org.example.collections.exceptions.EmptyCollectionException;
import org.example.collections.interfaces.IQueueADT;
import org.example.collections.interfaces.IStackADT;

public class IQueueWithTwoStacks<T> implements IQueueADT<T>
{
    /**
     * enqueue da stack
     */
    private IStackADT<T> stack1;

    /**
     * dequeue da stack
     */
    private IStackADT<T> stack2;


    public IQueueWithTwoStacks()
    {
        stack1 = (IStackADT<T>) new Stack<T>();
        stack2 = (IStackADT<T>) new Stack<T>();
    }


    @Override
    public void enqueue(T element)
    {
        stack1.push(element);
    }

    @Override
    public T dequeue()
    {
        if(isEmpty())
        {
            throw new EmptyCollectionException("Stack");
        }

        if (stack2.isEmpty())
        {
            while (!stack1.isEmpty())
            {
                stack2.push(stack1.pop());
            }
        }

        return stack2.pop();
    }

    @Override
    public T first()
    {
        if(isEmpty())
        {
            throw new EmptyCollectionException("Stack");
        }

        if(stack2.isEmpty())
        {
            while (!stack1.isEmpty())
            {
                stack2.push(stack1.pop());
            }
        }

        return stack2.peek();
    }

    @Override
    public boolean isEmpty()
    {
        return stack1.isEmpty() && stack2.isEmpty();
    }

    @Override
    public int size()
    {
        return stack1.size() + stack2.size();
    }
}

