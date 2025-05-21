package service;

public class MathUtils {
    
    public static int ceilDiv(int a, int b) {
        return a / b + ((a % b == 0) ? 0 : 1);
    }
}
