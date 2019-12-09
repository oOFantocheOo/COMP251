
public class KaratsubaTester {
    public static void main(String[] args){
        int product = Multiply.karatsuba(1, 8, 7)[0];
        int cost = Multiply.karatsuba(1, 8, 7)[1];

        if (product == 0) {
            System.out.println("Product is a success!");
        } else {
            System.out.println("Product has error!");
        }

        if (cost == 1) {
            System.out.println("Cost is a success!");
        } else {
            System.out.println("Cost has error!");
        }
    }
}
