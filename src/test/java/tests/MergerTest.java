package tests;

import FinalPackage.Merging.Merger;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MergerTest {

    public static void main(String[] args) {
        // Load RDF models from the provided RDF files
        Model model1 = ModelFactory.createDefaultModel();
        model1.read("src/test/testResources/starwars1Test.rdf");

        Model model2 = ModelFactory.createDefaultModel();
        model2.read("src/test/testResources/starwars2Test.rdf");

        Model model3 = ModelFactory.createDefaultModel();
        model3.read("src/test/testResources/starwars3Test.rdf");

        /*
            Test: Merge more than two graphs
        */
        List<Model> models = new ArrayList<>();
        models.add(model1);
        models.add(model2);
        models.add(model3);


        //Use With Priorities
        Map<Model, Integer> modelPriorities = new HashMap<>();
        modelPriorities.put(model1, 1);
        modelPriorities.put(model2, 2);
        modelPriorities.put(model3, 3);
        Model mergedModel1 = Merger.mergeGraphs(modelPriorities);
        System.out.println("Merged1");
        mergedModel1.write(System.out, "RDF/XML-ABBREV");

        //Use Without Priorities
        Model mergedModel = Merger.mergeGraphs(models);
        System.out.println("Merged");
        mergedModel.write(System.out, "RDF/XML-ABBREV");

    }
}