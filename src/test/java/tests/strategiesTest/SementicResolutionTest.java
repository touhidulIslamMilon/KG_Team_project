package tests.strategiesTest;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import FinalPackage.Merging.Strategies.FrequencyAndPriorityBaseStrategy;
import FinalPackage.Merging.Strategies.SementicResolutionStrategy;

public class SementicResolutionTest {
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
        SementicResolutionStrategy strategy = new SementicResolutionStrategy();

       //perform the test and print the result
        RDFNode resolvedObject = strategy.resolveConflict(objects, subject, predicate);

        System.out.println("ResolvedObject: " + resolvedObject.toString());

    }
}
