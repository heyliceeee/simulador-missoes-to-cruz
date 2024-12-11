package org.example.collections.implementation;

public class Node <T> {
   private T data;
   private  Node<T> next;

    public Node(T data){
        this.data = data;
        this.next = null;
    }

    public T getData(){
        return (T) this.data;
    }
    
    public Node<T> getNext(){
        return this.next;
    }
    
    public void setNext(Node<T> next){
        this.next = next;
    }
    
    @Override
    public boolean equals(Object obj){
        if(obj == this){
            return true;
        }
        if(obj == null || getClass() != obj.getClass()){
            return false;
        }
        
        Node<?> other = (Node<?>) obj;
        return this.data.equals(other.data);
    }
}
