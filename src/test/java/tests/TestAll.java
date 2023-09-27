package tests;

import FinalPackage.LoadRDF;
import FinalPackage.Merging.Merger;
import org.apache.jena.rdf.model.Model;

import java.util.List;

public class TestAll {

    public static void main(String[] args) {
        List<Model> models = LoadRDF.readAllTestCases();
        Model merged = Merger.mergeGraphs(models);

        //Use With Priorities
        System.out.println("Merged");

    }
}