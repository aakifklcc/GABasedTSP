package GeneticTSP;

public class Parameter {
    // serial number
    private String serialNumber;
    // Population size
    private int speciesNum;
    // Evolutionary algebra
    private int developNum;
    // Crossover probability
    private float pc;
    // Mutation probability
    private float pm;
    //The number of elites replicated as a percentage of the population
    private float talentReserveRate;

    public String getSerialNumber() {
        return serialNumber;
    }
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    public int getSpeciesNum() {
        return speciesNum;
    }
    public void setSpeciesNum(int speciesNum) {
        this.speciesNum = speciesNum;
    }
    public int getDevelopNum() {
        return developNum;
    }
    public void setDevelopNum(int developNUm) {
        this.developNum = developNUm;
    }
    public float getPc() {
        return pc;
    }
    public void setPc(float pc) {
        this.pc = pc;
    }
    public float getPm() {
        return pm;
    }
    public void setPm(float pm) {
        this.pm = pm;
    }
    public float getTalentReserveRate() {
        return talentReserveRate;
    }
    public void setTalentReserveRate(float talentReserveRate) {
        this.talentReserveRate = talentReserveRate;
    }
}
