package GeneticTSP;

public class SpeciesList {
    SpeciesNode head;//Head node
    int speciesNum;//Population size

    SpeciesList(int speciesNum) {
        this.head = new SpeciesNode();
        this.speciesNum = speciesNum;
    }

    //Add species
    void add(SpeciesNode species) {
        SpeciesNode point = head;
        while (point.next != null)//Find the end of the table node
            point = point.next;
        point.next = species;
    }
}
