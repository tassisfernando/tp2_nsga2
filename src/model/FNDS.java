package model;

import java.util.ArrayList;
import java.util.List;

public class FNDS {

    public static List<List<Individual>> execute(List<Individual> individuals){
        List<Point> points = new ArrayList<>(individuals.size());

        for (Individual individual : individuals) {
            points.add(new Point(individual));
        }

        List<List<Point>> bounds = new ArrayList<>();
        List<Point> bound_1 = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            Point point_p = points.get(i);
            point_p.S = new ArrayList<>();
            point_p.n = 0;
            for (int j = 0; j < points.size(); j++) {
                if (i != j){
                    Point point_q = points.get(j);
                    if (dominates(point_p.individual.getAvaliation(), point_q.individual.getAvaliation())){
                        point_p.S.add(point_q);
                    }else if (dominates(point_q.individual.getAvaliation(), point_p.individual.getAvaliation())){
                        point_p.n++;
                    }
                }
            }
            if(point_p.n == 0) {
                point_p.rank = 1;
                bound_1.add(point_p);
            }
        }
        bounds.add(bound_1);

        int i = 0;
        List<Point> bound_i = bounds.get(i);

        while (bound_i.size() != 0){

            List<Point> Q = new ArrayList<>();

            for (Point point_i : bound_i) {
                List<Point> Sp = point_i.S;
                for (Point point_q : Sp) {
                    point_q.n--;
                    if (point_q.n == 0){
                        point_q.rank = i+1;
                        Q.add(point_q);
                    }
                }
            }
            i++;
            bound_i = Q;
            bounds.add(bound_i);
        }

        List<List<Individual>> finaList = new ArrayList<List<Individual>>();

        for (int j = 0; j < bounds.size(); j++) {
            List<Point> fronteira_j = bounds.get(j);
            if (fronteira_j.size() > 0){
                List<Individual> Find = new ArrayList<>();

                for (int k = 0; k < fronteira_j.size(); k++) {
                    Point p = fronteira_j.get(k);
                    Individual ind = p.individual;
                    Find.add(ind);
                }
                finaList.add(Find);
            }

        }
        return finaList;
    }
    public static boolean dominates(double[] avalInd, double[] avalInd2){
        boolean domina = false;
        for (int i = 0; i < avalInd.length; i++) {
            if (avalInd[i] > avalInd2[i]) {
                return false;
            }

            if (avalInd[i] < avalInd2[i]) {
                domina = true;
            }
        }
        return domina;
    }
}

