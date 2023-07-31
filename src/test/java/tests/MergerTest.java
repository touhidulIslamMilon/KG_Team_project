package tests;

import max.loadRDF.LoadRDF;
import org.apache.jena.rdf.model.Model;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        Model mergedModel = max.kg_merger.Merger.mergeGraphs(models);

        // Load the expected merged model from file
        Model expectedModel = LoadRDF.getModel("expected-graph.rdf");

        // Assert that the mergedModel is equal to the expectedModel
        Assert.assertTrue("Merged graph does not match the expected result.", mergedModel.isIsomorphicWith(expectedModel));
    }
}
