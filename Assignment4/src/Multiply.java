
import java.util.*;
import java.io.*;

public class Multiply {

    private static int randomInt(int size) {
        Random rand = new Random();
        int maxval = (1 << size) - 1;
        return rand.nextInt(maxval + 1);
    }

    private static int assign(double n) {//hardcode to fit in overflowed negative int
        while (n > 4294967295.0)
            n -= 4294967295.0;
        if (n <= Integer.MAX_VALUE)
            return (int) n;
        else {
            int tmp = (int) (n - 2147483647);//amount overflow
            tmp += Integer.MIN_VALUE;
            return tmp - 1;
        }
    }

    public static int[] naive(int size, int x, int y) {

        // YOUR CODE GOES HERE  (Note: Change return statement)

        int[] res = new int[2];
        boolean negative = false;
        if (x < 0) {
            negative = !negative;
            x = -x;
        }
        if (y < 0) {
            negative = !negative;
            y = -y;
        }
        if (size == 1) {
            int cur_bit_x = x % 2;
            int cur_bit_y = y % 2;
            res[0] = cur_bit_x * cur_bit_y;
            res[1] += 1;
            if (negative)
                res[0] = -res[0];
            return res;
        } else {
            int m = assign(Math.ceil((double) size / 2));
            int p = assign(Math.pow(2, m));
            int a = assign(Math.floor((double) x / p));
            int b = x % p;
            int c = assign(Math.floor((double) y / p));
            int d = y % p;
            int[] tmp1 = naive(m, a, c);
            int[] tmp2 = naive(m, b, d);
            int[] tmp3 = naive(m, b, c);
            int[] tmp4 = naive(m, a, d);
            int e = tmp1[0];
            int f = tmp2[0];
            int g = tmp3[0];
            int h = tmp4[0];
            res[0] = assign((Math.pow(2, 2 * m) * e + p * (g + h) + f));
            res[1] += (tmp1[1] + tmp2[1] + tmp3[1] + tmp4[1]);
            res[1] += 3 * m;
            if (negative)
                res[0] = -res[0];
            return res;
        }

    }

    public static int[] karatsuba(int size, int x, int y) {

        // YOUR CODE GOES HERE  (Note: Change return statement)
        int[] res = new int[2];
        res[1] = 0;
        res[0] = 0;
        boolean negative = false;
        if (x < 0) {
            negative = !negative;
            x = -x;
        }
        if (y < 0) {
            negative = !negative;
            y = -y;
        }
        if (size == 1) {
            int cur_bit_x = x % 2;
            int cur_bit_y = y % 2;
            res[0] = cur_bit_x * cur_bit_y;
            res[1] += 1;
            if (negative)
                res[0] = -res[0];
            return res;
        } else {
            int m = assign(Math.ceil((double) size / 2));
            int a = assign(Math.floor((double) x / Math.pow(2, m)));
            int b = assign((x % Math.pow(2, m)));
            int c = assign(Math.floor((double) y / Math.pow(2, m)));
            int d = assign((y % Math.pow(2, m)));
            int[] tmp1 = karatsuba(m, a, c);
            int[] tmp2 = karatsuba(m, b, d);
            int[] tmp3 = karatsuba(m, a - b, c - d);
            int e = tmp1[0];
            int f = tmp2[0];
            int g = tmp3[0];
            res[0] = assign((Math.pow(2, 2 * m) * e + Math.pow(2, m) * (e + f - g) + f));
            if (negative)
                res[0] = -res[0];
            res[1] += (tmp1[1] + tmp2[1] + tmp3[1]);
            res[1] += 6 * m;
            return res;
        }

    }

    public static void main(String[] args) {

        try {
            int maxRound = 20;
            int maxIntBitSize = 16;
            for (int size = 1; size <= maxIntBitSize; size++) {
                int sumOpNaive = 0;
                int sumOpKaratsuba = 0;
                for (int round = 0; round < maxRound; round++) {
                    int x = randomInt(size);
                    int y = randomInt(size);
                    int[] resNaive = naive(size, x, y);
                    int[] resKaratsuba = karatsuba(size, x, y);


                    if (resNaive[0] != resKaratsuba[0]) {
                        throw new Exception("Return values do not match! (x=" + x + "; y=" + y + "; Naive=" + resNaive[0] + "; Karatsuba=" + resKaratsuba[0] + ")");
                    }

                    if (resNaive[0] != (x * y)) {
                        int myproduct = x * y;
                        throw new Exception("Evaluation is wrong! (x=" + x + "; y=" + y + "; Your result=" + resNaive[0] + "; True value=" + myproduct + ")");
                    }

                    sumOpNaive += resNaive[1];
                    sumOpKaratsuba += resKaratsuba[1];
                }
                int avgOpNaive = sumOpNaive / maxRound;
                int avgOpKaratsuba = sumOpKaratsuba / maxRound;
                System.out.println(size + "," + avgOpNaive + "," + avgOpKaratsuba);
            }


        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
