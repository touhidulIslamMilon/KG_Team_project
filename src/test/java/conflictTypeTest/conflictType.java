package conflictTypeTest;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.util.ArrayList;
import java.util.List;

import static FinalPackage.Merging.FunctionalPropertyFinder.findFunctionalProperties;
import static FinalPackage.analyzeGraph.ConflictFinder.identifyAndPrintConflicts;

public class conflictType {
    public static void main(String[] args) {
        Model model1 = ModelFactory.createDefaultModel();
        model1.read("src/test/testResources/analysisTest.rdf");

        Model model2 = ModelFactory.createDefaultModel();
        model2.read("src/test/testResources/analysisTest2.rdf");

        List<Model> models = new ArrayList<>();
        models.add(model1);
        models.add(model2);

        System.out.println("Models loaded");

        findFunctionalProperties(models);
        identifyAndPrintConflicts(models);

        System.out.println("Functions are executed");
    }
}
