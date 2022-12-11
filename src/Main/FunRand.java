package Main;

public  class FunRand {
    public static double exp(double lambda) {
        double n = 0;
        while (n == 0) {
            n = Math.random();
        }
        return -(1/lambda) * Math.log(n);
    }
}
