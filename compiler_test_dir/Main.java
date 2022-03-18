public class Main {

    static int field = 1;

    public static int getField() {
        return field;
    }

    public static void main(String[] args) {
        int sum = 0;
        for(int i = 0; i < 1000000000; i++) sum += getField();
        System.out.println(sum);
    }
}
