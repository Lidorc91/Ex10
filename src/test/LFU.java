package test;

import java.util.Comparator;
import java.util.PriorityQueue;

public class LFU implements CacheReplacementPolicy {

    private PriorityQueue<Node> _queue;
    private Comparator<Node> _nodeComparator = new Comparator<Node>() {        
        public int compare(Node n1, Node n2) {
            return n1._frequency - n2._frequency;
        }        
    };

    public LFU(){
        _queue = new PriorityQueue<Node>(_nodeComparator);
    }

    @Override
    public void add(String word) {
        for (Node node : _queue) {
            if(node._word == word){
                Node n = node; 
                _queue.remove(node);
                n._frequency++;
                _queue.add(n);
                return;    
            }
        }
        _queue.add(new Node(word));        
    }

    @Override
    public String remove() {        
        String w = _queue.peek()._word;
        _queue.poll();
        return w;
    }
    
    public static class Node{
        private final String _word;
        private int _frequency =0;

        public Node(String w){
            _word = w;
        }        

    }
}
