package com.example.buffer;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Buffer<T>{
    Queue<T> queue=new LinkedList<>();
    public Buffer(){
        this.queue=new LinkedList<>();

    }
     public boolean add(T i){
        return this.queue.add(i);
     }
     public T poll(){
        T t=this.queue.poll();
        return t;
     }
    public T peek(){
        T t=this.queue.peek();
        return t;
    }
    public boolean isEmpty(){
        return this.queue.isEmpty();
    }
}
