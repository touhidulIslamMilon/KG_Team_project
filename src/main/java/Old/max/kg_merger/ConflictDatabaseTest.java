package Old.max.kg_merger;

import FinalPackage.LoadRDF;
import org.apache.jena.rdf.model.Model;

import java.util.List;

public class ConflictDatabaseTest {
    public static void main(String[] args) {
        // Create a list of models from RDF files (you can replace this with your own data loading logic)
        List<Model> models = LoadRDF.readAllTestCases();

        /*Model model1 = ModelFactory.createDefaultModel();
        model1.read("src/test/testResources/starwars1Test.rdf");

        Model model2 = ModelFactory.createDefaultModel();
        model2.read("src/test/testResources/starwars2Test.rdf");

        Model model3 = ModelFactory.createDefaultModel();
        model3.read("src/test/testResources/starwars3Test.rdf");

        List<Model> models = new ArrayList<>();
        models.add(model1);
        models.add(model2);
        models.add(model3);*/

        System.out.println("Models loaded");

        // Find conflicting statements
        ConflictDatabase map = new ConflictDatabase();
        map.findConflicts(models);
        map.printAllConflicts();
        map.close();
        // Print the conflicts
        //for (Map.Entry<ConflictFinder.SubjectPredicatePair, List<RDFNode>> entry : conflicts.entrySet()) {



    }

}
