package Main;

public class Main {
    public static void main(String[] args) {
        int size = 10_000;
        double lambda = 2;
        int numbOfInterv = 20;

        GeneratorCheck gc = new GeneratorCheck();
        gc.checkExp(size, lambda, numbOfInterv);
    }

}