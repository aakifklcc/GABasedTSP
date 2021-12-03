package GeneticTSP;

import java.util.ArrayList;
import java.util.Random;

public class GeneticAlgorithm {
    //Genetic algorithm parameters
    Parameter parameter;

    //Constructor
    public GeneticAlgorithm(Parameter parameter) {
        this.parameter = parameter;
    }

    //Begin to inherit
    SpeciesNode run(SpeciesList list, ArrayList<String> outdata) {
        //Create initial population
        createBeginningSpecies(list);

        //Record the optimal value of the previous population
        float oldBest = 9999999.0f;
        //Record the number of iterations to determine whether the 2-opt disturbance
        int epochNumForOpt = 0;

        for (int i = 1; i <= this.parameter.getDevelopNum(); i++) {
            //selection
            select(list);

            //crossover
            crossover(list);

            //Mutations
            mutate(list);

            //Print this iteration information
            System.out.println("* Iterating " + i + " times: ");
            SpeciesNode thisBest = getBest(list);
            thisBest.printRate();

            //Save this iteration information to a file
            outdata.add(i + " " + thisBest.distance);

            //Determine whether to perform 2-opt disturbance
            if(!Constant.OPT_ON){
                continue;
            }
            if (Math.abs(oldBest - thisBest.distance) < Constant.DISTURB_THRESHOLD) {
                epochNumForOpt++;
                if (epochNumForOpt > Constant.DISTURB_DEVELOP) {
                    twoOpt(list);
                }
            } else {
                epochNumForOpt = 0;
            }
            oldBest = thisBest.distance;
        }

        return getBest(list);
    }
    //Perform 2-opt perturbation
    void twoOpt(SpeciesList list) {
        SpeciesNode point = list.head;
        while (point.next != null) {
            int beginPoint = Utils.randInt(0, Constant.CITY_NUM - 1);
            int endPoint = Utils.randInt(0, Constant.CITY_NUM - 1);
            if (beginPoint >= endPoint)
                continue;
            String[] genesTemp = point.genes;
            while (beginPoint < endPoint) {
                String temp = genesTemp[beginPoint];
                genesTemp[beginPoint] = genesTemp[endPoint];
                genesTemp[endPoint] = temp;
                beginPoint++;
                endPoint--;
            }
            point.genes = genesTemp;
            point = point.next;
        }
    }
    //Randomly create initial population
    void createBeginningSpecies(SpeciesList list) {
        int randomNum = this.parameter.getSpeciesNum();
        for (int i = 1; i <= randomNum; i++) {
            SpeciesNode species = new SpeciesNode();//Create node
            if(Constant.RANDOM_INIT_POPULATION){
                species.createByRandomGenes();    //Random initial population genes
            }else {
                species.createByGreedyGenes();    //Greedy initial population gene
            }
            list.add(species);//Add species
        }
    }
    //Calculate the probability of each species being selected
    void calRate(SpeciesList list) {
        //Calculate total fitness
        float totalFitness = 0.0f;
        list.speciesNum = 0;
        SpeciesNode point = list.head.next;//cursor
        while (point != null)//Find the end of the table node
        {
            point.calFitness();// Calculate fitness

            totalFitness += point.fitness;
            list.speciesNum++;
            point = point.next;
        }

        //Calculate the probability of selection
        point = list.head.next;//cursor
        while (point != null)//Find the end of the table node
        {
            point.rate = point.fitness / totalFitness;
            point = point.next;
        }
    }
    //Choose excellent species (roulette)
    void select(SpeciesList list) {
        //Calculate fitness
        calRate(list);

        //Find the most adaptive species
        float talentDis = Float.MAX_VALUE;
        SpeciesNode talentSpecies = null;
        SpeciesNode point = list.head.next;//cursor

        while (point != null) {
            if (talentDis > point.distance) {
                talentDis = point.distance;
                talentSpecies = point;
            }
            point = point.next;
        }
        //Copy talentNum of the species with the greatest fitness
        SpeciesList newSpeciesList = new SpeciesList(this.parameter.getSpeciesNum());
        int talentNum = (int) (list.speciesNum * this.parameter.getTalentReserveRate());
        for (int i = 1; i <= talentNum; i++) {
            //Copy species to new table
            SpeciesNode newSpecies = talentSpecies.clone();
            newSpeciesList.add(newSpecies);
        }

        //Roulette list.speciesNum-talentNum times
        int roundNum = list.speciesNum - talentNum;
        for (int i = 1; i <= roundNum; i++) {
            //Probability of 0-1
            float rate = (float) Math.random();

            SpeciesNode oldPoint = list.head.next;
            while (oldPoint != null && oldPoint != talentSpecies)//Find the end of the table node
            {
                if (rate <= oldPoint.rate) {
                    SpeciesNode newSpecies = oldPoint.clone();
                    newSpeciesList.add(newSpecies);

                    break;
                } else {
                    rate = rate - oldPoint.rate;
                }
                oldPoint = oldPoint.next;
            }
            if (oldPoint == null || oldPoint == talentSpecies) {
                //Copy the last one
                point = list.head;
                while (point.next != null)//Find the end of the table node
                    point = point.next;
                SpeciesNode newSpecies = point.clone();
                newSpeciesList.add(newSpecies);
            }
        }
        list.head = newSpeciesList.head;
    }

    //Cross operation
    void crossover(SpeciesList list) {
        //With probability pcl~pch
        float rate = (float) Math.random();
        if (rate <= this.parameter.getPc()) {
            SpeciesNode point = list.head.next;
            Random rand = new Random();
            int find = rand.nextInt(list.speciesNum);
            while (point != null && find != 0)//Find the end of the table node
            {
                point = point.next;
                find--;
            }

            if (point.next != null) {
                int begin = rand.nextInt(Constant.CITY_NUM);

                //Take point and point.next to cross to form two new chromosomes
                for (int i = begin; i < Constant.CITY_NUM; i++) {
                    //Find the position fir in point.genes that is equal to point.next.genes[i]
                    //Find the position sec equal to point.genes[i] in point.next.genes
                    int fir, sec;
                    for (fir = 0; !point.genes[fir].equals(point.next.genes[i]); fir++) ;
                    for (sec = 0; !point.next.genes[sec].equals(point.genes[i]); sec++) ;
                    //Two genes interchange
                    String tmp;
                    tmp = point.genes[i];
                    point.genes[i] = point.next.genes[i];
                    point.next.genes[i] = tmp;

                    //Eliminate the duplicated gene after the swap
                    point.genes[fir] = point.next.genes[i];
                    point.next.genes[sec] = point.genes[i];

                }
            }
        }
    }

    //Mutation operation
    void mutate(SpeciesList list) {
        //Every species has a chance to mutate, proceed with probability pm
        SpeciesNode point = list.head.next;
        while (point != null) {
            float rate = (float) Math.random();
            if (rate < this.parameter.getPm()) {
                //Find the reversal left and right endpoints
                Random rand = new Random();
                int left = rand.nextInt(Constant.CITY_NUM);
                int right = rand.nextInt(Constant.CITY_NUM);
                if (left > right) {
                    int tmp;
                    tmp = left;
                    left = right;
                    right = tmp;
                }

                //Reverse left-right subscript elements
                while (left < right) {
                    String tmp;
                    tmp = point.genes[left];
                    point.genes[left] = point.genes[right];
                    point.genes[right] = tmp;

                    left++;
                    right--;
                }
            }
            point = point.next;
        }
    }

    //Acquire the most adaptable species
    SpeciesNode getBest(SpeciesList list) {
        float distance = Float.MAX_VALUE;
        SpeciesNode bestSpecies = null;
        SpeciesNode point = list.head.next;
        while (point != null)//Find the end of the table node
        {
            if (distance > point.distance) {
                bestSpecies = point;
                distance = point.distance;
            }
            point = point.next;
        }

        return bestSpecies;
    }
}
