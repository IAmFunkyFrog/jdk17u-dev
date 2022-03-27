public class Main {

    public static int test(int value) {
        if(value == 0) {
            System.out.println("even");
        }
        else {
            System.out.println("odd");
        }
        return value;
    }

    public static void main(String[] args) {
        for(int i = 0; i < 10; i++) {
            System.out.println(test(i % 2));
        }
    }
}
