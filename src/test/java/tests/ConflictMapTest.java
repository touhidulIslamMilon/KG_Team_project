package tests;

import FinalPackage.LoadRDF;
import FinalPackage.analyzeGraph.ConflictFinder;
import FinalPackage.analyzeGraph.ConflictMap;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConflictMapTest {
    public static void main(String[] args) {
        // Create a list of models from RDF files (you can replace this with your own data loading logic)
        List<Model> models = LoadRDF.readAllTestCases();

       /* Model model1 = ModelFactory.createDefaultModel();
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
        ConflictMap map = new ConflictMap();
        map.findConflicts(models);

        // Print the conflicts


        for (Map.Entry<ConflictMap.SubjectPredicatePair, List<RDFNode>> entry : map.getConflicts().entrySet()) {
            System.out.println("\nSubject: " + entry.getKey().getSubject() + "\nPredicate: " + entry.getKey().getPredicate() + "\nConflicting Objects: " + entry.getValue());
        }

    }

}
