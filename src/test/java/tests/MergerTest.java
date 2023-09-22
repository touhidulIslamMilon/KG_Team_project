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

        List<Model> models = new ArrayList<>();
        models.add(model1);
        models.add(model2);
        models.add(model3);


        //Use With Priorities
        System.out.println("Merged1");
        System.out.println("Please select first Option");
        Map<Model, Integer> modelPriorities = new HashMap<>();
        modelPriorities.put(model1, 1);
        modelPriorities.put(model2, 2);
        modelPriorities.put(model3, 3);
        Model mergedModel1 = Merger.mergeGraphs(modelPriorities);
        Model expectedModel1 = ModelFactory.createDefaultModel();
        expectedModel1.read("src/test/testResources/starwarsMerged1.rdf");
        System.out.println("Was Merge successfull? " + mergedModel1.isIsomorphicWith(expectedModel1));

        //Use Without Priorities
        System.out.println("Merged2");
        System.out.println("Please select first Option");
        Model mergedModel2 = Merger.mergeGraphs(models);
        Model expectedModel2 = ModelFactory.createDefaultModel();
        expectedModel2.read("src/test/testResources/starwarsMerged2.rdf");
        System.out.println("Was Merge successfull? " + mergedModel2.isIsomorphicWith(expectedModel2));




    }
}