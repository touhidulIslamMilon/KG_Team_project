package tests.strategiesTest;
import FinalPackage.Merging.Strategies.HelperFunction;
import FinalPackage.Merging.Strategies.MostOldestResolutionStrategy;
import FinalPackage.Merging.Strategies.MostRecentResolutionStrategy;

import org.apache.jena.rdf.model.*;

import java.text.ParseException;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

public class MostRecentAndOldestResolusionTest {
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
        HelperFunction helper = new HelperFunction();
        try {
            objects.put(object1, helper.dateToInt("1997-04-16"));
            objects.put(object2,  helper.dateToInt("1998-04-16"));
            objects.put(object3,  helper.dateToInt("1999-04-16"));
            objects.put(object4,  helper.dateToInt("2000-04-16"));
            objects.put(object5,  helper.dateToInt("1996-04-16"));
        } catch (ParseException e) {
           
            e.printStackTrace();
        }
        
   
        // Create an instance of PriorityBasedResolutionStrategy
        MostRecentResolutionStrategy strategy = new MostRecentResolutionStrategy();
        MostOldestResolutionStrategy strategy2 = new MostOldestResolutionStrategy();
       //perform the test and print the r  esult
        //RDFNode resolvedObject = strategy.resolveConflict(objects, subject, predicate);
        RDFNode resolvedObject = strategy2.resolveConflict(objects, subject, predicate);

        System.out.println("ResolvedObject: " + resolvedObject.toString());

    }
}
