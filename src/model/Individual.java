package model;

import mutation.BLXAlpha;
import mutation.Mutation;
import mutation.None;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.lang.Math.pow;

public class Individual implements  Comparable<Individual> {

    private double[] gens;
    private double[] evaluation;
    private double crowdingDist;
    private Random random;
    public BLXAlpha crossBLX;
    public Mutation mutation;

    public Individual(int QTD_GENES, int QTD_AVALIACAO){
        this.gens = new double[QTD_GENES];
        this.evaluation = new double[QTD_AVALIACAO];
        this.random = new Random();
        this.crossBLX = new BLXAlpha(0.1);
    }

    public Individual(double[] gens){
        this.gens = gens;
    }
    public Individual(double[] gens, int qntAvaliation) {
        this(gens, qntAvaliation, new BLXAlpha(0.1), new None());
    }
    public Individual(double[] gens, double[] avaliacao) {
        this(gens, avaliacao, new BLXAlpha(0.1), new None());
    }
    public Individual(double[] gens, int qntAvaliation, BLXAlpha crossBLX, Mutation mutation){
        this.gens = gens;
        this.evaluation = new double[qntAvaliation];
        this.crossBLX = crossBLX;
        this.mutation = mutation;
    }

    public Individual(double[] gens, double[] avaliacao, BLXAlpha crossBLX, Mutation mutation){
        this.gens = gens;
        this.evaluation = avaliacao;
        this.crossBLX = crossBLX;
        this.mutation = mutation;
    }
    public List<Individual> recombine(Individual p2, int FUNCTION){
        List<Individual> children = new ArrayList<>(2);

        double[][] childrenMat = crossBLX.getOffSpring(this.gens, p2.gens, new double[]{-10, -10}, new double[]{10, 10});
        Individual f1 = new Individual(childrenMat[0], FUNCTION);
        Individual f2 = new Individual(childrenMat[1], FUNCTION);

        if (f1.gens.length == 0 || f2.gens.length == 0){
            int i = 0;
        }

        children.add(f1);
        children.add(f2);

        return children;
    }

    public void mutate(){
        this.gens = mutation.getMutation(this.gens, new double[]{-10, -10}, new double[]{10, 10});
    }

    public void evaluate(int FUNCTION){
        switch (FUNCTION){
            case 1:
                // problema 1
                this.evaluation[0] = pow(this.gens[0], 2);
                this.evaluation[1] = pow(this.gens[0]-1, 2);
                break;
            case 2:
                // problema 2
                this.evaluation[0] = pow(this.gens[0], 2) + pow(this.gens[1], 2);
                this.evaluation[1] = pow(this.gens[0], 2) + pow(this.gens[1]-2, 2);
                break;
            case 3:
                // problema 3

                this.evaluation[0] = pow(this.gens[0]-1, 2) + pow(this.gens[1], 2) + pow(this.gens[2], 2);
                this.evaluation[1] = pow(this.gens[0], 2) + pow(this.gens[1]-1, 2) + pow(this.gens[2], 2);
                this.evaluation[2] = pow(this.gens[0], 2)+ pow(this.gens[1], 2) + pow(this.gens[2]-1, 2);
                break;
        }
    }

    public void gerarGenes(double a, double b){
        for (int i = 0; i < this.gens.length; i++) {
            this.gens[i] = random.nextDouble() * b * 2 + a;
        }
    }
    public double[] getAvaliation() {
        return evaluation;
    }

    public double[] getGens() {
        return gens;
    }

    public double getCrowdingDistance() {
        return crowdingDist;
    }

    public void setCrowdingDistance(double crowdingDistance) {
        this.crowdingDist = crowdingDistance;
    }

    @Override
    public int compareTo(Individual i2) {
        return Double.compare(i2.crowdingDist, this.crowdingDist);
    }

    @Override
    public String toString() {
        return "\nGenes: " +
                Arrays.toString(this.gens) +
                "\nAvaliação: " +
                Arrays.toString(this.evaluation);
    }
}



