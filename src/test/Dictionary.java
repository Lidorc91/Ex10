package test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Dictionary {
    private CacheManager _existsingWords;
    private CacheManager _nonExistingWords;
    private BloomFilter _bf;
    private ParIOSearcher _searcher;
    private String[] _files;

    public Dictionary(String... fileNames){
        _existsingWords = new CacheManager(400,new LRU());
        _nonExistingWords = new CacheManager(100, new LFU());
        _bf = new BloomFilter(256, "MD5" , "SHA1");
        _searcher = new ParIOSearcher();
        _files = fileNames;
        // add words to bloom filter
        for (String name : fileNames) {
            try {
                FileReader file = new FileReader(name);
                Scanner s = new Scanner(file);
                try {
                    while (s.hasNext()) {                        
                        String[] splitWord = s.next().split(",|\\.");
                        for (String w : splitWord) {
                            if (w.length()>1 || w.equals("a") || w.equals("I")) {
                                _bf.add(w);
                            }
                        }
                    }
                } finally {
                    s.close();
                    try {
                        file.close();
                    } catch (IOException e) {                        
                    }
                }
            } catch (FileNotFoundException e) {                
            }            
        }
    }

    public boolean query(String word){
        if(_existsingWords.query(word)){
            return true;
        }else if(_nonExistingWords.query(word)){
            return false;
        }else{
            if(_bf.contains(word)){
                _existsingWords.add(word);
                return true;
            }else{
                _nonExistingWords.add(word);
                return false;
            }
        }
    }

    public boolean challenge(String word) {
        boolean found = _searcher.search(word, _files);
        if(found){
            _existsingWords.add(word);
        }else{
            _nonExistingWords.add(word);
        }
        return found;
    }

    public void close() {
        _searcher.stop();
    }
}
