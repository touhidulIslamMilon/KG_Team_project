package tests.strategiesTest;


import FinalPackage.Merging.Strategies.ManualReviewResolutionStrategy;
import FinalPackage.Merging.Strategies.PriorityBasedResolutionStrategy;
import FinalPackage.Merging.Strategies.Strategies;

import org.apache.jena.rdf.model.*;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.HashMap;
import java.util.Map;

public class PriorityBaseResolutionTest {
    public static void main(String[] args) {
        // Create a map of RDFNodes for testing
        ListMultimap<RDFNode, Integer> objects = ArrayListMultimap.create();
        Resource subject = ResourceFactory.createResource("http://example.org/subject");
        Property predicate = ResourceFactory.createProperty("http://example.org/predicate");
        RDFNode object1 = ResourceFactory.createResource("http://example.org/object1");
        RDFNode object2 = ResourceFactory.createResource("http://example.org/object2");
        RDFNode object3 = ResourceFactory.createResource("http://example.org/object3");

        objects.put(object1, 1);
        objects.put(object2, 2);
        objects.put(object3, 3);

        // Create an instance of PriorityBasedResolutionStrategy
        PriorityBasedResolutionStrategy strategy = new PriorityBasedResolutionStrategy();

       //perform the test and print the result
        RDFNode resolvedObject = strategy.resolveConflict(objects, subject, predicate);

        System.out.println("ResolvedObject: " + resolvedObject.toString());

    }
    
}
