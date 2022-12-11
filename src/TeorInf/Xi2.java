package TeorInf;

//Xi^2 for a = 0.05 and k from 1 to 30
public class Xi2 {
    private final double[] xiKrArr;
    public Xi2() {
        xiKrArr = new double[] { 3.8, 6.0, 7.8, 9.5, 11.1, 12.6, 14.1, 15.5, 16.9, 18.3,
                19.7, 21.0, 22.4, 23.7, 25.0, 26.3, 27.6, 28.9, 30.1, 31.4, 32.7, 33.9,
                35.2, 36.4, 37.7, 38.9, 40.1, 41.3, 42.6, 43.8};
    }

    public double getXi2Kr(int k) {
        return xiKrArr[k-1];
    }
}
