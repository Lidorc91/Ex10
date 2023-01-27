package test;


public class Dictionary {
    private CacheManager _existsingWords;
    private CacheManager _nonExistingWords;
    private BloomFilter _bf;
    
    public Dictionary(String... fileNames){
        _existsingWords = new CacheManager(400,new LRU());
        _nonExistingWords = new CacheManager(100, new LFU());
        _bf = new BloomFilter(256, "MD5" , "SHA1");
        _bf.add(null);
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
}
