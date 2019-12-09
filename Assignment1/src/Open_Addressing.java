import java.io.*;
import java.util.*;

public class Open_Addressing {
    public int m; // number of SLOTS AVAILABLE
    public int A; // the default random number
    int w;
    int r;
    public int[] Table;

    protected Open_Addressing(int w, int seed, int A) {

        this.w = w;
        this.r = (int) (w - 1) / 2 + 1;
        this.m = power2(r);
        if (A == -1) {
            this.A = generateRandom((int) power2(w - 1), (int) power2(w), seed);
        } else {
            this.A = A;
        }
        this.Table = new int[m];
        for (int i = 0; i < m; i++) {
            Table[i] = -1;
        }
    }

    /**
     * Calculate 2^w
     */
    public static int power2(int w) {
        return (int) Math.pow(2, w);
    }

    public static int generateRandom(int min, int max, int seed) {
        Random generator = new Random();
        if (seed >= 0) {
            generator.setSeed(seed);
        }
        int i = generator.nextInt(max - min - 1);
        return i + min + 1;
    }

    /**
     * Implements the hash function g(k)
     */
    public int probe(int key, int i) {
        //TODO: implement this function and change the return statement.
        return ((((this.A * key) % power2(this.w)) >> (this.w - this.r)) + i) % power2(r);
    }


    /**
     * Inserts key k into hash table. Returns the number of collisions encountered
     */
    public int insertKey(int key) {
        //TODO : implement this and change the return statement.
        int i = 0;
        int hashcode = probe(key, i);
        while (Table[hashcode] != -1 && Table[hashcode] != -2)//if -1(never used) or -2(used but freed now), it's available, can be stored
        {
            i += 1;
            hashcode = probe(key, i);
        }
        Table[hashcode] = key;
        return i;//!!! when to give up
    }

    /**
     * Sequentially inserts a list of keys into the HashTable. Outputs total number of collisions
     */
    public int insertKeyArray(int[] keyArray) {
        //TODO
        int collision = 0;
        for (int key : keyArray) {
            collision += insertKey(key);
        }
        return collision;
    }

    /**
     * Inserts key k into hash table. Returns the number of collisions encountered
     */
    public int removeKey(int key) {
        //TODO: implement this and change the return statement
        int i = 0;
        int hashcode = probe(key, i);
        while (Table[hashcode] != -1) {//while never used
            if (Table[hashcode] == key) {
                Table[hashcode] = -2;//marked as deleted
                return i;
            }
            i += 1;
            hashcode = probe(key, i);
        }
        return i;
    }
}
