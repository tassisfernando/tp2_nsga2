package app;

import model.CrowdingDistance;
import model.FNDS;
import model.Individual;
import utils.GenerateFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
public class NSGA2 {
  final int FUNCTION_SELECTOR = 2;
  int QTD_GENS;
  int QTD_EVALUATIONS;
  final int MAX_GEN = 1000;
  final int NUM_INDIVIDUALS = 20;
  final double X_BOUND = 10 ;// 20 e -20
  final List<Integer> generationsToPrint = Arrays.asList(1, 20, 40, 60, 80, 100, MAX_GEN);

  public static void main(String[] args) {
    NSGA2 nsga2 = new NSGA2();
    nsga2.start();
  }

  private Individual start(){

    switch (FUNCTION_SELECTOR){
      case 1:
        QTD_GENS = 1;
        QTD_EVALUATIONS = 2;
        break;
      case 2:
        QTD_GENS = 2;
        QTD_EVALUATIONS = 2;
        break;
      case 3:
        QTD_GENS = 3;
        QTD_EVALUATIONS = 3;
        break;
    }

    List<Individual> individualsPop = new ArrayList<>(NUM_INDIVIDUALS);
    individualsPop.addAll(initPop(NUM_INDIVIDUALS,X_BOUND));
    evaluanteIndividuals(individualsPop);

    int num_gen = 1;
    while (num_gen <= MAX_GEN) {
      List<Individual> newPop = new ArrayList<>(NUM_INDIVIDUALS);
      List<Individual> intermediatePopulation = new ArrayList<>(individualsPop);

      makeOffspring(intermediatePopulation, individualsPop, FUNCTION_SELECTOR);
      List<List<Individual>> borders = FNDS.execute(intermediatePopulation);

      for (List<Individual> border : borders) {
        if (newPop.size() >= NUM_INDIVIDUALS) break;

        if (border.size() + newPop.size() > NUM_INDIVIDUALS) {
          List<Individual> individualCD = new CrowdingDistance().evaluate(border);
          for (Individual individual : individualCD) {
            if (newPop.size() < NUM_INDIVIDUALS) {
              newPop.add(individual);
            } else break;
          }

        } else {
          newPop.addAll(border);
        }
      }

      individualsPop = newPop;

      if (generationsToPrint.contains(num_gen)) {
        generatePonts(individualsPop, num_gen);
      }

      num_gen++;
    }

    printIndividuals(individualsPop, num_gen);
    return individualsPop.get(0);
  }

  public void evaluanteIndividuals(List<Individual> individuals){
    for (Individual individual : individuals) {
      individual.evaluate(FUNCTION_SELECTOR);
    }
  }

  public List<Individual> initPop(int NUM_INDIVIDUALS, double X_BOUND){
    List<Individual> initPop = new ArrayList<>(NUM_INDIVIDUALS);
    for (int i = 0; i < NUM_INDIVIDUALS; i++) {
      Individual individual = new Individual(QTD_GENS, QTD_EVALUATIONS);
      individual.gerarGenes(-X_BOUND, X_BOUND);
      initPop.add(individual);
    }
    return initPop;
  }

  public static void makeOffspring(List<Individual> intermediatePopulation, List<Individual> individuals, int FUNCTION) {
    Random random = new Random();

    List<Individual> popAux = new ArrayList<>(individuals.size());
    popAux.addAll(individuals);

    while (popAux.size() > 1) {
      int idxR1 = random.nextInt(popAux.size());
      Individual p1 = popAux.remove(idxR1);
      int idxR2 = random.nextInt(popAux.size());
      Individual p2 = popAux.remove(idxR2);

      List<Individual> children = p1.recombine(p2, FUNCTION);
      Individual f1 = children.get(0);
      if (random.nextDouble() > 0.9) {
        f1.mutate();
      }
      Individual f2 = children.get(1);
      if (random.nextDouble() > 0.9) {
        f2.mutate();
      }

      f1.evaluate(FUNCTION);
      f2.evaluate(FUNCTION);
      intermediatePopulation.add(f1);
      intermediatePopulation.add(f2);
    }
  }


  public static void generatePonts(List<Individual> individuals, int numGen) {
    try {
      createGensFile(individuals, numGen);
      generateFunctionResults(individuals, numGen);
    } catch (IOException erro){
      System.out.printf("Erro: %s", erro.getMessage());
    }
  }

  private static void createGensFile(List<Individual> individuals, int numGen) throws IOException {
    String path = "./src/files/gen_"+numGen+"_genes.txt";
    GenerateFile.createFile(path, individuals, false);
  }

  private static void generateFunctionResults(List<Individual> individuals, int numGen) throws IOException {
    String path = "./src/files/gen_"+numGen+"_values.txt";
    GenerateFile.createFile(path, individuals, true);
  }

  private void printIndividuals(List<Individual> popInd, int numGen) {
    System.out.printf("Geração: %d - indivíduos: %s \n", numGen, popInd.toString());
  }

}