package test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.BitSet;

public class BloomFilter {
    private BitSet _bitSet;
    private int _setSize;
    private String[] _algs;

    public BloomFilter(int size, String... algs) {
        _bitSet = new BitSet(_setSize);
        _setSize = size;
        _algs = algs;
    }

    public void add(String word) {
        for (Integer value : hashToInt(word)) {
            _bitSet.set(value);
        }
    }

    public boolean contains(String word) {
        for (Integer value : hashToInt(word)) {
            if (!_bitSet.get(value)) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        char[] c = new char[_bitSet.length()];
        for (int i = 0; i < _bitSet.length(); i++) {
            if (_bitSet.get(i)) {
                c[i] = '1';
            } else {
                c[i] = '0';
            }
        }
        return String.copyValueOf(c);
    }

    private ArrayList<Integer> hashToInt(String word) {
        ArrayList<Integer> values = new ArrayList<Integer>(_algs.length);
        for (String alg : _algs) {
            MessageDigest md;
            try {
                md = MessageDigest.getInstance(alg);
                byte[] bts = md.digest(word.getBytes());
                BigInteger bg = new BigInteger(bts);
                int num = Math.abs(bg.intValue()) % _setSize;
                values.add(num);
            } catch (NoSuchAlgorithmException e) {
            }
        }
        return values;
    }

}
