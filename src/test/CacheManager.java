package test;

import java.util.HashSet;

public class CacheManager {
    private final int _cacheSize;
    private CacheReplacementPolicy _crp;
    private HashSet<String> _wordsInCache;

    public CacheManager(int size, CacheReplacementPolicy policy) {
        _cacheSize = size;
        _crp = policy;
        _wordsInCache = new HashSet<String>();
    }

    public boolean query(String word) { // Checks for the word in cache
        if (_wordsInCache.contains(word)) {
            return true;
        }
        return false;
    }

    public void add(String word) { // Adds to cache
        if (_wordsInCache.size() == _cacheSize) {
            _wordsInCache.remove(_crp.remove());
        }
        _wordsInCache.add(word);
        _crp.add(word);
    }

}
