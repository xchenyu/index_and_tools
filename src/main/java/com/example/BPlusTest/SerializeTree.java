package com.example.BPlusTest;

import java.util.LinkedList;
import java.util.Queue;

public class SerializeTree {
    public String serialize(BPlusTree.BPlusNode root){
        if(root==null)return "";
        Queue<BPlusTree.Node> q=new LinkedList<>();
        q.add(root);
        String ret="";
        while (!q.isEmpty()){
            int keylen=q.peek().keys.length;
            BPlusTree.Node temp=q.peek();
            for(int i=0;i<keylen;i++){
                ret+=String.valueOf(temp.keys[i]);
            }
        }
        return ret;
    }

}
