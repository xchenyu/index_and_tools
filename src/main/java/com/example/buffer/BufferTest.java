package com.example.buffer;

import java.util.LinkedList;
import java.util.Queue;

public class BufferTest {
    public static void main(String[] args) throws InterruptedException {
        Buffer<Integer> queue=new Buffer<>();
        for(int i=0;i<10;i++){
            queue.add(i);
        }
        Buffer<String> queue2=new Buffer<>();
    }
}
