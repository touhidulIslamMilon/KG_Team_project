package tests;

import FinalPackage.LoadRDF;
import FinalPackage.Merging.Merger;
import org.apache.jena.rdf.model.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestAll {

    public static void main(String[] args) {
        List<Model> models = LoadRDF.readAllTestCases();

        // Create a map of models with increasing priorities
        Map<Model, Integer> modelPriorityMap = new HashMap<>();
        int priority = models.size();

        // Assign priorities to each model using a for loop
        for (Model model : models) {
            modelPriorityMap.put(model, priority);
            priority--; // Increase the priority for the next model
        }

        Model merged = Merger.mergeGraphs(modelPriorityMap);

        //Use With Priorities
        System.out.println("Merged");

    }
}