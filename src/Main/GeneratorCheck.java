package Main;

import TeorInf.Xi2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeneratorCheck {
    public void checkExp(int size, double lambda, int numbOfInterv) {

        List<Double> genArr = generateNumbersFirst(size, lambda);

        System.out.println("Min: " + Collections.min(genArr));
        System.out.println("Max: " + Collections.max(genArr));
        System.out.println();

        Show sh = new Show();
        List<Double> intervals = getIntervals(genArr, numbOfInterv);
        sh.showList(intervals, "intervals");

        List<Double> hits = getHitsInIntervals(genArr, intervals);
        sh.showList(hits, "hits");

        List<Double> midIntervals = getMiddleOfIntervals(intervals);
        sh.showList(midIntervals, "middle of each interval");

        double ev = calcAverage(hits, midIntervals);
        System.out.println("Ev = " + ev);

        double disp = calcDispersion(hits, midIntervals, ev);
        System.out.println("Disp = " + disp);

        double expLambda = 1.0/((ev + Math.sqrt(disp))/2.0);

        System.out.println(expLambda);

        List<Double> teorProb = getTeorProb(intervals, expLambda);
        sh.showList(teorProb, "test probability");

        List<Double> teorHits = getTeorHits(teorProb, size);
        sh.showList(teorHits, "teor hits");
        System.out.println();
        List<List<Double>> fHList = getFixedHits(hits, teorHits);
        sh.showList(fHList.get(0), "fixed hits");
        sh.showList(fHList.get(1), "fixed teor hits");

        double expXi2 = calcExpXi2(fHList.get(0), fHList.get(1));
        double xi2Kr = new Xi2().getXi2Kr(fHList.get(0).size()-2);

        System.out.println("Exp xi2: " + expXi2);
        System.out.println("xi2Kr: " + xi2Kr);
        if(expXi2 < xi2Kr) {
            System.out.println("Success");
        }
    }

    public static List<Double> generateNumbersFirst(int size, double lambda) {
        List<Double> genArr = new ArrayList<>(size);
        for(int i=0; i<size; i++) {
            genArr.add(FunRand.exp(lambda));
        }
        return genArr;
    }

    public static List<Double> getIntervals(List<Double> genArr, int numbOfInterv) {
        List<Double> intervals = new ArrayList<>(numbOfInterv);
        final double min = Collections.min(genArr);
        final double max = Collections.max(genArr);
        final double step = (max - min)/(double) numbOfInterv;
        double pos = min;

        for(int i=0; i<numbOfInterv; i++) {
            intervals.add(pos);
            pos += step;
        }
        intervals.add(pos);
        return intervals;
    }

    public static List<Double> getHitsInIntervals(List<Double> genArr, List<Double> intervals) {
        List<Double> hits = new ArrayList<>(Collections.nCopies(intervals.size()-1, 0.0));
        final double step = intervals.get(1) - intervals.get(0);
        final double min = Collections.min(intervals);

        for(Double el: genArr) {
            int pos = (int) ((el - min) / step);
            if (pos >= intervals.size() - 1)
                pos = intervals.size() - 2;
            hits.set(pos, hits.get(pos) + 1);
        }
        return hits;
    }

    public static List<Double> getMiddleOfIntervals(List<Double> intervals) {
        List<Double> midIntervals = new ArrayList<>();
        for(int i=0; i<intervals.size()-1; i++) {
            double ev = (intervals.get(i+1)+intervals.get(i))/2.0;
            midIntervals.add(ev);
        }
        return midIntervals;
    }

    public static double calcAverage(List<Double> hits, List<Double> midIntervals) {
        double sum = 0.0;
        int allN = 0;
        for(int i=0; i<midIntervals.size(); i++) {
            sum += hits.get(i)*midIntervals.get(i);
            allN += hits.get(i);
        }
        return sum / allN;
    }

    public static double calcDispersion(List<Double> hits, List<Double> midIntervals, double ev) {
        double sum = 0;
        int allN = 0;
        for(int i=0; i<midIntervals.size(); i++){
            sum += (Math.pow(ev-midIntervals.get(i), 2)*hits.get(i));
            allN += hits.get(i);
        }
        return sum*(1.0/(allN-1));
    }

    public static List<Double> getTeorProb(List<Double> intervals, double lambda) {
        List<Double> teorProb = new ArrayList<>();
        for(int i=0; i<intervals.size()-1; i++) {
            double p = Math.exp(-lambda*intervals.get(i)) - Math.exp(-lambda*intervals.get(i+1));
            teorProb.add(p);
        }
        return teorProb;
    }

    public static List<Double> getTeorHits(List<Double> teorProb, int n) {
        List<Double> teorHits = new ArrayList<>(teorProb);
        teorHits.replaceAll(aDouble -> aDouble * n);
        return teorHits;
    }

    public static List<List<Double>> getFixedHits(List<Double> hits, List<Double> teorHits) {
        List<Double> fHits = new ArrayList<>(Collections.nCopies(hits.size(), 0.0));
        List<Double> fTeorHits = new ArrayList<>(Collections.nCopies(hits.size(), 0.0));

        int k = 0;

        for(int i=0; i<hits.size(); i++) {
            if(hits.get(i)<5 || teorHits.get(i)<5) {
                if(fHits.get(k)>=5 && fTeorHits.get(k)>=5 && i<hits.size()-1){
                    k++;
                }
                fHits.set(k, fHits.get(k) + hits.get(i));
                fTeorHits.set(k, fTeorHits.get(k) + teorHits.get(i));

                if(i == hits.size()-1 && (fHits.get(k)<5 || fTeorHits.get(k)<5)){
                    k--;
                    fHits.set(k, fHits.get(k) + fHits.get(k+1));
                    fTeorHits.set(k, fTeorHits.get(k) + fTeorHits.get(k+1));
                }
            }
            else {
                fHits.set(k, fHits.get(k) + hits.get(i));
                fTeorHits.set(k, fTeorHits.get(k) + teorHits.get(i));

                if(i<hits.size()-1) {
                    k++;
                }
            }
        }

        fHits = fHits.subList(0, k+1);
        fTeorHits = fTeorHits.subList(0, k+1);
        List<List<Double>> f = new ArrayList<>();
        f.add(fHits);
        f.add(fTeorHits);
        return f;
    }



    public static double calcExpXi2(List<Double> hits, List<Double> teorHits) {
        double sum = 0.0;
        for(int i=0; i<hits.size(); i++) {
            sum += (Math.pow(hits.get(i)-teorHits.get(i), 2))/teorHits.get(i);
        }
        return sum;
    }
}