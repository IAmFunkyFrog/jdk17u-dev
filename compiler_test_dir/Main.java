public class Main {

    static long field = 1;

    public static long getField() {
        return field;
    }

    public static void main(String[] args) {
        long sum = 0;
        for(int i = 0; i < 100000000; i++) sum += getField();
        System.out.println(sum);
        sum = 0;
        field = 2;
        for(int i = 0; i < 100000000; i++) sum += getField();
        System.out.println(sum);
    }
}
