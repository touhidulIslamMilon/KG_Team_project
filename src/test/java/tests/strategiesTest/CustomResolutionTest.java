package tests.strategiesTest;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import FinalPackage.Merging.Strategies.CustomResolutionStrategy;
import FinalPackage.Merging.Strategies.FrequencyAndPriorityBaseStrategy;
import FinalPackage.Merging.Strategies.HelperFunction;
import FinalPackage.Merging.Strategies.MostFrequentResolutionStrategy;
import FinalPackage.Merging.Strategies.Strategies;

import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.impl.PropertyImpl;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import FinalPackage.Merging.Strategies.MostFrequentResolutionStrategy;


public class CustomResolutionTest {
    public static void main(String[] args) {
        // Create a map of RDFNodes for testing
        ListMultimap<RDFNode, Integer> objects = ArrayListMultimap.create();

        Resource subject = ResourceFactory.createResource("http://example.org/subject");

        // Property predicate = ResourceFactory.createProperty("http://example.org/predicate");
        //Property predicate =ResourceFactory.createProperty("http://www.w3.org/2000/01/rdf-schema#label");  //,ResolutionStrategy.MAX_VALUE);Cheaked
        Property predicate =ResourceFactory.createProperty("http://www.w3.org/2000/01/rdf-schema#comment");  //,ResolutionStrategy.MIN_VALUE);Cheaked
        //Property predicate =ResourceFactory.createProperty("http://www.w3.org/2000/01/rdf-schema#seeAlso");  //,ResolutionStrategy.LONG_VALUE);Cheaked
        //Property predicate =ResourceFactory.createProperty("http://www.w3.org/2000/01/rdf-schema#isDefinedBy");  //,ResolutionStrategy.SHORT_VALUE);Cheaked
        //Property predicate =ResourceFactory.createProperty("http://www.w3.org/2000/01/rdf-schema#member");  //,ResolutionStrategy.RECENT_DATE);Cheaked
        //Property predicate =ResourceFactory.createProperty("http://www.w3.org/2000/01/rdf-schema#subClassOf");  //,ResolutionStrategy.HIGHEST_PRIORITY);Cheaked
        //Property predicate =ResourceFactory.createProperty("http://www.w3.org/2000/01/rdf-schema#subPropertyOf");  //,ResolutionStrategy.PRIORITY_AND_FREQUENCY);Cheaked
        // Property predicate =ResourceFactory.createProperty("http://www.w3.org/2000/01/rdf-schema#domain");  //,ResolutionStrategy.MOST_FREQUENT); Cheaked


        HelperFunction helper = new HelperFunction();

        RDFNode object1 = ResourceFactory.createResource("http://example.org/object1");
        RDFNode object2 = ResourceFactory.createResource("http://example.org/object2");
        RDFNode object3 = ResourceFactory.createResource("http://example.org/object3s");
        RDFNode object4 = ResourceFactory.createResource("http://example.org/object2");
        RDFNode object5 = ResourceFactory.createResource("http://example.org/object1");

        // try {
        //     objects.put(object1, helper.dateToInt("1997-04-16"));
        //     objects.put(object2,  helper.dateToInt("1998-04-16"));
        //     objects.put(object3,  helper.dateToInt("1999-04-16"));
        //     objects.put(object4,  helper.dateToInt("2000-04-16"));
        //     objects.put(object5,  helper.dateToInt("1996-04-16"));
        // } catch (ParseException e) {
           
        //     e.printStackTrace();
        // }
        
        
        objects.put(object1, 1);
        objects.put(object2, 2);
        objects.put(object3, 3);
        objects.put(object4, 2);
        objects.put(object5, 2);
   
        // Create an instance of PriorityBasedResolutionStrategy
        CustomResolutionStrategy strategy = new CustomResolutionStrategy();

       //perform the test and print the result
        RDFNode resolvedObject = strategy.resolveConflict(objects, subject, predicate);

        System.out.println("ResolvedObject: " + resolvedObject.toString());

    }
}
