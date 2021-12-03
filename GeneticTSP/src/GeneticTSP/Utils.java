package GeneticTSP;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Utils {
    //Read data file
    public static ArrayList<Position> readData(String filePath) {
        ArrayList<Position> cityPosition = new ArrayList<>();
        // Read the file and initialize cityPosition
        File dataFile = new File(filePath);
        if (dataFile.isFile() && dataFile.exists()) {
            try {
                InputStreamReader reader = new InputStreamReader(new FileInputStream(dataFile));
                BufferedReader bufferedReader = new BufferedReader(reader);
                String singleLine = null;
                while ((singleLine = bufferedReader.readLine()) != null) {
                    // Parse this row of data
                    cityPosition.add(parseDataLine(singleLine));
                }
                bufferedReader.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cityPosition;
    }
    // Parse a row of data in the data set
    private static Position parseDataLine(String line) {
        Position position = new Position();
        String temp[] = line.split(" ");
        position.setX(Integer.parseInt(temp[1]));
        position.setY(Integer.parseInt(temp[2]));
        return position;
    }

    //Read parameter file
    public static ArrayList<Parameter> readParameter(String filePath) {
        ArrayList<Parameter> parameters = new ArrayList<>();
        File paramFile = new File(filePath);
        if (paramFile.isFile() && paramFile.exists()) {
            try {
                InputStreamReader reader = new InputStreamReader(new FileInputStream(paramFile));
                BufferedReader bufferedReader = new BufferedReader(reader);
                String singleLine = null;
                while ((singleLine = bufferedReader.readLine()) != null) {
                    // Parse this row of data
                    parameters.add(parseParamLine(singleLine));
                }
                bufferedReader.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return parameters;
    }
    // Parse one line of data in the parameter file
    private static Parameter parseParamLine(String line) {
        Parameter parameter = new Parameter();
        String[] temp = line.split(" ");
        parameter.setSerialNumber(temp[0]);
        parameter.setSpeciesNum(Integer.parseInt(temp[1]));
        parameter.setDevelopNum(Integer.parseInt(temp[2]));
        parameter.setPc(Float.parseFloat(temp[3]));
        parameter.setPm(Float.parseFloat(temp[4]));
        parameter.setTalentReserveRate(Float.parseFloat(temp[5]));
        return parameter;
    }

    //Generate a random number in the specified range
    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
    //Get the path corresponding to a gene
    public static String getPath(String[] genes){
        String path="";
        for (int i = 0; i < genes.length; i++)
            path += (genes[i] + "->");
        path +=(genes[0] + "\n");
        return path;
    }
}
