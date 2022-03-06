public class Main {

    public static int add(int a, int b) {
        return a + b;
    }

    public static void main(String[] args) {
        int x = 0;
        for(int i = 0; i < 1000000000; i++) {
            x += add(i, i + 1);
        }
        System.out.println(x);
    }
}
