package tests.strategiesTest;

import FinalPackage.Merging.Strategies.MostFrequentResolutionStrategy;
import FinalPackage.Merging.Strategies.Strategies;

import org.apache.jena.rdf.model.*;
import java.util.HashMap;
import java.util.Map;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

public class MostFrequentResolutionTest {
    public static void main(String[] args) {
        // Create a map of RDFNodes for testing
        ListMultimap<RDFNode, Integer> objects = ArrayListMultimap.create();

        Resource subject = ResourceFactory.createResource("http://example.org/subject");
        Property predicate = ResourceFactory.createProperty("http://example.org/predicate");
        RDFNode object1 = ResourceFactory.createResource("http://example.org/object1");
        RDFNode object2 = ResourceFactory.createResource("http://example.org/object2");
        RDFNode object3 = ResourceFactory.createResource("http://example.org/object3");
        RDFNode object4 = ResourceFactory.createResource("http://example.org/object2");

        objects.put(object1, 1);
        objects.put(object2, 2);
        objects.put(object3, 2);
        objects.put(object4, 4);
   
        // Create an instance of PriorityBasedResolutionStrategy
        MostFrequentResolutionStrategy strategy = new MostFrequentResolutionStrategy();

       //perform the test and print the result
        RDFNode resolvedObject = strategy.resolveConflict(objects, subject, predicate);

        System.out.println("ResolvedObject: " + resolvedObject.toString());

    }
}
