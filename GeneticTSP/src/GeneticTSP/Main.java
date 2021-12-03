package GeneticTSP;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;


public class Main {
    public static void main(String[] args) {
        //Starting time
        long startTime = System.currentTimeMillis();

        //Read parameters from the parameter file
        ArrayList<Parameter> parameters = Utils.readParameter(Constant.paramFile);

        //Record the average result of each set of parameters running epoch times
        ArrayList<String> allAvgResults = new ArrayList<>();

        //Perform genetic algorithm for each parameter configuration
        Iterator<Parameter> iterator = parameters.iterator();
        while (iterator.hasNext()) {
            //Parameter configuration this time
            Parameter thisParam = iterator.next();

            //Record this parameter
            ArrayList<String> outData = new ArrayList<>();
            outData.add("SPECIES_NUM: " + thisParam.getSpeciesNum());
            outData.add("DEVELOP_NUM: " + thisParam.getDevelopNum());
            outData.add("pm: " + thisParam.getPm());
            outData.add("pc: " + thisParam.getPc());
            outData.add("TALENT_RESERVE_RATE: " + thisParam.getTalentReserveRate());
            outData.add("EPOCHS: " + Constant.EPOCHS);

            //Record the solution of each epoch, and finally take the average
            ArrayList<Float> results = new ArrayList<>();
            //The optimal value and path of each set of parameters epoch times
            float bestPerEpoch=999999.0f;
            String bestPath=null;


            // Run the same parameter multiple times to average
            for (int epoch = 0; epoch < Constant.EPOCHS; epoch++) {

                System.out.println("# Epoch: " + (epoch + 1));

                //Create genetic algorithm-driven objects
                GeneticAlgorithm GA = new GeneticAlgorithm(thisParam);

                //Create initial population
                SpeciesList speciesList = new SpeciesList(thisParam.getSpeciesNum());

                //Start genetic algorithm (selection operator, crossover operator, mutation operator)
                SpeciesNode bestRate = GA.run(speciesList, outData);

                //Printing path and shortest distance
                System.out.println("* GA Completed: ");
                bestRate.printRate();

                //Record the results of this convergence
                results.add(bestRate.distance);

                //The solution with the smallest iteration epoch for each set of parameters
                if (bestRate.distance<bestPerEpoch){
                    bestPerEpoch=bestRate.distance;
                    bestPath=Utils.getPath(bestRate.genes);
                }

            }
            //Solve for the mean
            float sum = 0;
            for (int i = 0; i < results.size(); i++) {
                sum += results.get(i);
                System.out.println("# Shortest distance in each epoch: ");
                System.out.println("In epoch " + i + ": " + results.get(i));
            }
            float avg = sum / results.size();
            System.out.println("# Average shortest distance: ");
            System.out.println(avg);
            outData.add("avg: " + avg);
            //Record the mean value of this group of parameters
            allAvgResults.add(avg + "");

            //End Time
            long endTime = System.currentTimeMillis();
            System.out.println("# Time cost: " + (endTime - startTime) / 1000.0);
            outData.add("time: " + (endTime - startTime) / 1000.0);

            //Write the result of each group of parameters to the file
            Path outFile = Paths.get("/Users/akifkilic/Desktop/GeneticTSP/GeneticTSP/data" + thisParam.getSerialNumber() + ".txt");
            try {
                Files.write(outFile, outData, Charset.forName("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            //The optimal value of this group of parameters is written to the file
            Path outFileBest = Paths.get("/Users/akifkilic/Desktop/GeneticTSP/GeneticTSP/data" + thisParam.getSerialNumber() + "_best.txt");
            try {
                Files.write(outFileBest,(bestPerEpoch+"\n"+bestPath).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Save the average results of all group parameter runs to a file
        Path outFile = Paths.get(Constant.avgResultsFile);
        try {
            Files.write(outFile, allAvgResults, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
