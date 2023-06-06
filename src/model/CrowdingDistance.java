package model;

import java.util.List;

public class CrowdingDistance {

    public List<Individual> evaluate(List<Individual> border){
        int borderSize = border.size();

        for (Individual individual: border) {
            individual.setCrowdingDistance(0);
        }

        Individual individual0 = border.get(0);

        int objectives = individual0.getAvaliation().length;
        for (int i = 0; i < objectives; i++) {
            sort(border, i);
            border.get(0).setCrowdingDistance(Double.POSITIVE_INFINITY);
            border.get(borderSize - 1).setCrowdingDistance(Double.POSITIVE_INFINITY);

            for (int j = 1; j < borderSize - 1; j++) {
                Individual previous = border.get(j-1);
                Individual later = border.get(j+1);

                double aux = (later.getAvaliation()[i] - previous.getAvaliation()[i])/
                        (border.get(borderSize - 1).getAvaliation()[i] - border.get(0).getAvaliation()[i]);

                double crowdingDistance = border.get(j).getCrowdingDistance() + aux;
                border.get(j).setCrowdingDistance(crowdingDistance);
            }
        }

        border.sort(new IndividuoComparator());

        return border;
    }

    private void sort(List<Individual> border, int objectives){
        for (int i = 0; i < border.size() - 1; i++) {
            for (int j = i + 1; j < border.size(); j++) {
                if(border.get(i).getAvaliation()[objectives] > border.get(j).getAvaliation()[objectives]){
                    Individual aux = border.get(i);
                    border.set(i, border.get(j));
                    border.set(j, aux);
                }
            }
        }
    }
}
