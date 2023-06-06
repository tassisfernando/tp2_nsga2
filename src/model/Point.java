package model;

import java.util.ArrayList;

public class Point {
    public double[] gens;
    public ArrayList<Point> S;
    public int n;
    public int rank;

    public Individual individual;

    public Point(Individual individual){
        this.individual = individual;
        this.gens = individual.getGens();
    }

}
