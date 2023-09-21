package tests.strategiesTest;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import FinalPackage.Merging.Strategies.FrequencyAndPriorityBaseStrategy;
import FinalPackage.Merging.Strategies.MostFrequentResolutionStrategy;
import FinalPackage.Merging.Strategies.Strategies;

import org.apache.jena.rdf.model.*;
import java.util.HashMap;
import java.util.Map;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import FinalPackage.Merging.Strategies.MostFrequentResolutionStrategy;

public class FrequencyAndPriorityTest {
    public static void main(String[] args) {
        // Create a map of RDFNodes for testing
        ListMultimap<RDFNode, Integer> objects = ArrayListMultimap.create();

        Resource subject = ResourceFactory.createResource("http://example.org/subject");
        Property predicate = ResourceFactory.createProperty("http://example.org/predicate");
        RDFNode object1 = ResourceFactory.createResource("http://example.org/object1");
        RDFNode object2 = ResourceFactory.createResource("http://example.org/object2");
        RDFNode object3 = ResourceFactory.createResource("http://example.org/object3");
        RDFNode object4 = ResourceFactory.createResource("http://example.org/object2");
         RDFNode object5 = ResourceFactory.createResource("http://example.org/object1");

        objects.put(object1, 1);
        objects.put(object2, 2);
        objects.put(object3, 4);
        objects.put(object4, 2);
        objects.put(object5, 2);
   
        // Create an instance of PriorityBasedResolutionStrategy
        FrequencyAndPriorityBaseStrategy strategy = new FrequencyAndPriorityBaseStrategy();

       //perform the test and print the result
        RDFNode resolvedObject = strategy.resolveConflict(objects, subject, predicate);

        System.out.println("ResolvedObject: " + resolvedObject.toString());

    }
}
