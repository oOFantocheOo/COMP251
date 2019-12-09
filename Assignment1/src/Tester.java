import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Tester {

    public static void main(String[] args) {
        System.out.println("### This is the assignment tester ###");
        System.out.println("This is in no way a definitive test for correctness of your assignment");
        System.out.println("Please add extra tests here to test your own code");

        System.out.println("Running checks");
        Open_Addressing openMap = new Open_Addressing(10, 0, -1);
        Chaining chainingMap = new Chaining(10, 0, -1);


        int chain = chainingMap.chain(1);
        System.out.println("Chaining chain check:" + (chain == 30));

        chainingMap.insertKey(1);
        System.out.println("Chaining insert check:" + (chainingMap.Table.get(30).size() == 1));

        int probe = openMap.probe(1, 1);
        System.out.println("Open Addressing probe check:" + (probe == 31));

        openMap.insertKey(0);
        System.out.println("Open Addressing insert check:" + (openMap.Table[0] == 0));

        openMap.insertKey(1);
        openMap.removeKey(1);
        System.out.println("Open Addressing remove check:" + (openMap.Table[30] != 1));


        int[] Table = new int[100];

    }

}
