package test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParIOSearcher implements FileSearcher{
    private IOSearcher _ioSearcher;
    private ExecutorService es;

    public ParIOSearcher(){
        _ioSearcher = new IOSearcher();
        es = Executors.newCachedThreadPool();       
    }

    @Override
    public boolean search(String word, String... fileNames) {
        List<Future<Boolean>> cfs = new ArrayList<>();
        for (String file : fileNames) {
            Future<Boolean> f = es.submit(() ->  _ioSearcher.search(word, file));
            cfs.add(f);                        
        }
        while(!cfs.isEmpty()){
            for (int i = 0; i < cfs.size(); i++) {
                if(cfs.get(i).isDone()){
                    try {
                        if(cfs.get(i).get()){
                            return true;
                        }else{
                            cfs.remove(cfs.remove(i));
                        }
                    } catch (InterruptedException e) {
                    } catch (ExecutionException e) {
                    }
                } 
            }            
        }    
        return false;
    }

    @Override
    public void stop() {
       _ioSearcher.stop();
       es.shutdownNow();        
    }

    public void finalize() throws Throwable{
        es.shutdown();
    }
	
}
