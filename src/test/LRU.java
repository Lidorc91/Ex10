package test;

import java.util.LinkedList;
import java.util.Queue;

public class LRU implements CacheReplacementPolicy {
    private Queue<String> _queue;

    public LRU() {
        _queue = new LinkedList<String>();
    }

    @Override
    public void add(String word) {
       if(_queue.contains(word)){
           _queue.remove(word);        
       }else{
        _queue.add(word);
       }        
    }

    @Override
    public String remove() {
        return _queue.remove();
    }
    /* 
    public boolean contains(String word){
        if(_queue.contains(word)){
            return true;
        }
        return false;
    }
    */

}
