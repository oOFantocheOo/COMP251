import javax.swing.text.AsyncBoxView;
import java.io.*;
import java.util.*;

//No collaborators
public class main {


    public static void main(String[] args) {
        //TODO:build the hash table and insert keys using the insertKeyArray function.

        int A_listX = 683;
        int[] listX = {12, 14, 77, 74, 63, 21, 69, 13, 84, 93, 35, 89, 45, 60, 15, 57, 51, 18, 42, 62};

        int A_listY = 554;
        int[] listY = {86, 85, 6, 97, 19, 66, 26, 14, 15, 49, 75, 64, 35, 54, 31, 9, 82, 29, 81, 13};

        int w = 0;
        while (Math.pow(2, w) < A_listX)
            w += 1;

        Chaining c_listX = new Chaining(w, 0, A_listX);
        int c_collision_listX = c_listX.insertKeyArray(listX);

        Open_Addressing o_listX = new Open_Addressing(w, 0, A_listX);
        int o_collision_listX = o_listX.insertKeyArray(listX);

        w = 0;
        while (Math.pow(2, w) < A_listY)
            w += 1;

        Chaining c_listY = new Chaining(w, 0, A_listY);
        int c_collision_listY = c_listY.insertKeyArray(listY);

        Open_Addressing o_listY = new Open_Addressing(w, 0, A_listY);
        int o_collision_listY = o_listY.insertKeyArray(listY);


        System.out.println("listX collision using Chaining: " + c_collision_listX);
        System.out.println("listY collision using Chaining: " + c_collision_listY);
        System.out.println("listX collision using Open Addressing: " + o_collision_listX);
        System.out.println("listY collision using Open Addressing: " + o_collision_listY);

    }
}