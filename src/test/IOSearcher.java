package test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class IOSearcher implements FileSearcher {
    volatile boolean stopSearch;
    Thread t;

    @Override
    public boolean search(String word, String... fileNames) {
        t = new Thread(searchFiles(word, fileNames));
        boolean b = t.start();
    }

    private boolean searchFiles(String word,String... fileNames){
        String searchedWord;
        for (String name : fileNames) {
            try {
                FileReader file = new FileReader(name);
                Scanner s = new Scanner(file);
                try {
                    while (s.hasNext() && !stopSearch) {
                        searchedWord = s.next();
                        String[] splitWord = searchedWord.split(",|\\.");
                        for (String w : splitWord) {
                            if (w == word) {
                                return true;
                            }
                        }
                    }
                } finally {
                    s.close();
                    try {
                        file.close();
                    } catch (IOException e) {
                        return false;
                    }
                }
            } catch (FileNotFoundException e) {
                return false;
            }            
        }
        return false;
    }

    @Override
    public void stop() {
        stopSearch = true;
    }

}
