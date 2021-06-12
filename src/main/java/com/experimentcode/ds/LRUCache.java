package com.experimentcode.ds;

import java.util.HashMap;
import java.util.Map;

class LRUCache {
    Map<Integer, Node> map = new HashMap<>();
    Node head;
    Node tail;
    int capacity;
    public LRUCache(int capacity) {
        this.capacity=capacity;
    }
    
    public int get(int key) {
        Node n = map.get(key);
        if(n==null)
            return -1;
        delete(n);
        add(n);
        return n.val;
    }
    
    private void delete(Node n){
        if(n.prev!=null){
            n.prev.next=n.next;
        }else{
            head= n.next;
        }
        if(n.next!=null){
            n.next.prev=n.prev;
        }else{
            tail=n.prev;
        }
    }
    private void add(Node n){
        n.next=head;
        if(head!=null){
            head.prev=n;
            if(head.next==null){
                tail=head;
            }
        }
         head=n;
        if(tail==null)
            tail=head;
        n.prev=null;
    }
    public void put(int key, int value) {
        Node n = new Node();
        n.key=key;
        n.val=value;
        if(map.containsKey(key))
        {
            delete(map.get(key));
            add(n);
            map.put(key,n);
        } else{
            if(map.size()==capacity){
                map.remove(tail.key);
                tail=tail.prev;
                if(tail!=null)
                tail.next=null;
                //System.out.println(tail.key+":"+tail.val+":"+map.size());
            }
            add(n);
            map.put(key,n);
        }
    }
}


class Node{
    int key,val;
    Node next,prev;
}
