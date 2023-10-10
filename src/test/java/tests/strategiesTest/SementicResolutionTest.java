package tests.strategiesTest;

import java.text.ParseException;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;

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

        //Test for uri (Longest and shortest)
        RDFNode object1 = ResourceFactory.createResource("http://example.org/object1");
        RDFNode object2 = ResourceFactory.createResource("httprg/object2s");
        RDFNode object3 = ResourceFactory.createResource("http://example.org/object3");
        RDFNode object4 = ResourceFactory.createResource("http://example.org/object2");
        RDFNode object5 = ResourceFactory.createResource("http://example.org/object4");
        RDFNode object6 = ModelFactory.createDefaultModel().createTypedLiteral("6", XSDDatatype.XSDinteger);
        RDFNode object7 = ModelFactory.createDefaultModel().createTypedLiteral("6", XSDDatatype.XSDinteger);

        //Test for number (Mean ,Max ,Min and Median)
        // RDFNode object1 = ResourceFactory.createResource("1");
        // RDFNode object2 = ResourceFactory.createResource("2");
        // RDFNode object3 = ResourceFactory.createResource("3");
        // RDFNode object4 = ResourceFactory.createResource("4");
        // RDFNode object5 = ResourceFactory.createResource("20");
        

        //Test for Date (Recent and old) 
        // RDFNode object1 = ResourceFactory.createResource("1997-04-16");
        // RDFNode object2 = ResourceFactory.createResource("1998-04-16");
        // RDFNode object3 = ResourceFactory.createResource("1999-04-16");
        // RDFNode object4 = ResourceFactory.createResource("2000-04-16");
        // RDFNode object5 = ResourceFactory.createResource("1996-04-16");

        
        // objects.put(object1, 1);
        // objects.put(object2, 2);
        // objects.put(object3, 4);
        // objects.put(object4, 2);
        // objects.put(object5, 2);
        objects.put(object6, 2);
        objects.put(object7, 2);
   
        // Create an instance of PriorityBasedResolutionStrategy
        SementicResolutionStrategy strategy = new SementicResolutionStrategy();

       //perform the test and print the result
        RDFNode resolvedObject = strategy.resolveConflict(objects, subject, predicate);

        System.out.println("ResolvedObject: " + resolvedObject.toString());

    }
}
