package GeneticTSP;

import java.util.ArrayList;

public class Constant {

    static int CITY_NUM;                      // Number of city
    static final float[][] disMap;           // Maps data
    static final int EPOCHS = 1;            // Number of program runs, average

    //If the optimal solution of continuous DISTURB_DEVELOP sub-evolution does not change 
    // more than DISTURB_THRESHOLD each time, perform 2-opt perturbation
    static final int DISTURB_DEVELOP=100;
    static final float DISTURB_THRESHOLD=10.0f;

    static final boolean OPT_ON=false;      //2-opt switch, closed by default
    static final boolean RANDOM_INIT_POPULATION=true;  //Random or greedy when initializing the population, random by default

    static final String dataFile = "/Users/akifkilic/Desktop/GeneticTSP/GeneticTSP/data/CitiesCoordinaties.txt";   //data set
    static final String paramFile = "/Users/akifkilic/Desktop/GeneticTSP/GeneticTSP/data/param.txt";    //Run parameter file
    static final String avgResultsFile = "/Users/akifkilic/Desktop/GeneticTSP/GeneticTSP/data\\avg_results.txt";   //Save the average results of each set of parameters

    static {
        //City coordinate collection
        ArrayList<Position> cityPosition = Utils.readData(dataFile);
        //Path collection
        CITY_NUM = cityPosition.size();
        disMap = new float[CITY_NUM][CITY_NUM];
        for (int i = 0; i < CITY_NUM; i++) {
            for (int j = i; j < CITY_NUM; j++) {
                Position thisPos = cityPosition.get(i);
                Position nextPos = cityPosition.get(j);
                float dis = (float) Math.sqrt(Math.pow(thisPos.getX() - nextPos.getX(), 2) +
                        Math.pow(thisPos.getY() - nextPos.getY(), 2));
                disMap[i][j] = dis;
                disMap[j][i] = disMap[i][j];
            }
        }
    }
}
