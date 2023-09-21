package tests;

import FinalPackage.Merging.Merger;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class MergerTest {

    public static void main(String[] args) {
        // Load RDF models from the provided RDF files
        Model model1 = ModelFactory.createDefaultModel();
        model1.read("src/test/testResources/starwars1Test.rdf");

        Model model2 = ModelFactory.createDefaultModel();
        model2.read("src/test/testResources/starwars2Test.rdf");

        Model model3 = ModelFactory.createDefaultModel();
        model3.read("src/test/testResources/starwars3Test.rdf");

        // Create a list of RDF models
        List<Model> models = new ArrayList<>();
        models.add(model1);
        models.add(model2);
        models.add(model3);

        // Merge the RDF models using the mergeGraphs method
        Model mergedModel = Merger.mergeGraphs(models);

        // Print the merged model
        mergedModel.write(System.out, "RDF/XML");

    }
}