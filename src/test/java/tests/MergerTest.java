package tests;

import FinalPackage.Merging.Merger;
import FinalPackage.Merging.LoadRDF;
import org.apache.jena.rdf.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MergerTest {
    @Test
    public void testMergeGraphs() {
        // Create sample RDF models representing knowledge graphs
        Model model1 = LoadRDF.getModel("sample-graph1.rdf");
        Model model2 = LoadRDF.getModel("sample-graph2.rdf");
        Model model3 = LoadRDF.getModel("sample-graph3.rdf");

        // Create a list of the sample models
        List<Model> models = new ArrayList<>();
        models.add(model1);
        models.add(model2);
        models.add(model3);

        // Call the mergeGraphs method to merge the sample models
        Model mergedModel = Merger.mergeGraphs(models);

        // Load the expected merged model from file
        Model expectedModel = LoadRDF.getModel("expected-graph.rdf");

        // Assert that the mergedModel is equal to the expectedModel
        Assert.assertTrue("Merged graph does not match the expected result.", mergedModel.isIsomorphicWith(expectedModel));
    }

}
