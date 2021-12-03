package GeneticTSP;
import java.util.Random;

public class SpeciesNode {
    String[] genes;         // gene sequence
    float distance;         // distance
    float fitness;          // Fitness
    SpeciesNode next;
    float rate;             // Roulette Probability

    SpeciesNode() {
        //initialization
        this.genes = new String[Constant.CITY_NUM];
        this.fitness = 0.0f;
        this.distance = 0.0f;
        this.next = null;
        rate = 0.0f;
    }
    //Randomly initialize the individual's genetic sequence
    void createByRandomGenes() {
        //Sequential initialization gene sequence
        for (int i = 0; i < genes.length; i++) {
            genes[i] = Integer.toString(i + 1);
        }
        //Get random seed
        Random rand = new Random();

        for (int j = 0; j < genes.length; j++) {
            int num = j + rand.nextInt(genes.length - j);

            //exchange
            String tmp;
            tmp = genes[num];
            genes[num] = genes[j];
            genes[j] = tmp;
        }
    }
    //Original species gene (greed)
    void createByGreedyGenes() {
        Random rand = new Random();
        int i = rand.nextInt(Constant.CITY_NUM); //Randomly generate a city as a starting point
        genes[0] = Integer.toString(i + 1);
        int j;//destination
        int cityNum = 0;
        do {
            cityNum++;

            //Select the shortest single source city
            float minDis = Integer.MAX_VALUE;
            int minCity = 0;
            for (j = 0; j < Constant.CITY_NUM; j++) {
                if (j != i) {
                    //Judging whether it is duplicated
                    boolean repeat = false;
                    for (int n = 0; n < cityNum; n++) {
                        if (Integer.parseInt(genes[n]) == j + 1) {
                            repeat = true;
                            break;
                        }
                    }
                    if (repeat == false)
                    {
                        //Judgment length
                        if (Constant.disMap[i][j] < minDis) {
                            minDis = Constant.disMap[i][j];
                            minCity = j;
                        }
                    }
                }
            }

            //Add to chromosome
            genes[cityNum] = Integer.toString(minCity + 1);
            i = minCity;
        } while (cityNum < Constant.CITY_NUM - 1);
    }

    //Calculate species fitness
    void calFitness() {
        float totalDis = 0.0f;
        for (int i = 0; i < Constant.CITY_NUM; i++) {
            int curCity = Integer.parseInt(this.genes[i]) - 1;
            int nextCity = Integer.parseInt(this.genes[(i + 1) % Constant.CITY_NUM]) - 1;

            totalDis += Constant.disMap[curCity][nextCity];
        }
        this.distance = totalDis;
        this.fitness = 1.0f / totalDis;
    }
    //Deep copy
    public SpeciesNode clone() {
        SpeciesNode species = new SpeciesNode();

        //Copy value
        for (int i = 0; i < this.genes.length; i++)
            species.genes[i] = this.genes[i];
        species.distance = this.distance;
        species.fitness = this.fitness;

        return species;
    }
    //Print path
    void printRate() {
        System.out.print("Shortest path: ");
        for (int i = 0; i < genes.length; i++)
            System.out.print(genes[i] + "->");
        System.out.print(genes[0] + "\n");
        System.out.println("Shortest distance: " + distance);
    }
}
