package tests;

import FinalPackage.Merging.Strategies.ManualReviewResolutionStrategy;
import org.apache.jena.rdf.model.*;
import java.util.HashMap;
import java.util.Map;

public class ManualReviewResolutionTest {
    public static void main(String[] args) {
        // Create a map of RDFNodes for testing
        Map<RDFNode, Integer> objects = new HashMap<>();

        Resource subject = ResourceFactory.createResource("http://example.org/subject");
        Property predicate = ResourceFactory.createProperty("http://example.org/predicate");
        RDFNode object1 = ResourceFactory.createResource("http://example.org/object1");
        RDFNode object2 = ResourceFactory.createResource("http://example.org/object2");
        RDFNode object3 = ResourceFactory.createResource("http://example.org/object3");

        objects.put(object1, 0);
        objects.put(object2, 0);
        objects.put(object3, 0);

        // Create an instance of ManualReviewResolutionStrategy
        ManualReviewResolutionStrategy strategy = new ManualReviewResolutionStrategy();

        // Simulate user input for manual review (choose object2)
        System.out.println("Please choose an object for following subject and predicate:");
        System.out.println(subject + " " + predicate);
        RDFNode resolvedObject = strategy.resolveConflict(objects, subject, predicate);

        System.out.println("ResolvedObject: " + resolvedObject.toString());

    }
}

