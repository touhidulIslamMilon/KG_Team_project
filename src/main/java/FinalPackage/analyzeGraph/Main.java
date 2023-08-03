package FinalPackage.analyzeGraph;
import FinalPackage.Merging.Merger;
import FinalPackage.Merging.LoadRDF;
import org.apache.jena.rdf.model.*;
import scala.sys.Prop;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static FinalPackage.analyzeGraph.analyzeGraph.*;
import static FinalPackage.normalisation.normlaisationFunctions.normalizeModel;

public class Main {
    public static void main(String[] args) {
        Model model1 = LoadRDF.getModel("graph1.rdf");
        //model1.write(System.out, "RDF/XML-ABBREV");
        //normalizeModel(model1);


        System.out.println();
        //number of statements
        System.out.println("Number of statements:" + model1.size());

        System.out.println();
        //number of subjects
        int numberOfSubjects = numberOfSubjects(model1).size();
        System.out.println("Number of subjects:" + numberOfSubjects);

        // show those subjects
        Set<Resource> uniqueSubjects = numberOfSubjects(model1);
        System.out.println("Unique subjects:");
        for(Resource r:uniqueSubjects){
            System.out.println("/n"+r);
        }

        System.out.println();
        //number of unique predicates
        int numberOfPredicates = numberOfPredicates(model1).size();
        System.out.println("Number of predicates:" + numberOfPredicates);

        // show those predicates
        Set<Property> uniquePredicates = numberOfPredicates(model1);
        System.out.println("Unique predicates:");
        for(Property r:uniquePredicates){
            System.out.println("/n"+r);
        }

        System.out.println();
        //number of objects
        int numberOfObjects = numberOfObjects(model1).size();
        System.out.println("Overall Number of objects:" + numberOfObjects);

        //very long processing function. CHECK!
        //int numberOfFunctionalProperties = countFunctionalProperties(model1);
        //System.out.println("Number of functional properties:"+ numberOfFunctionalProperties);

        System.out.println();
        // number of subjects with resource as an objects
        int numberOfSubWithResourceObj = countSubjectsWithResourceObjects(model1).size();
        System.out.println("Number of subjects with resources as objects:" + numberOfSubWithResourceObj);

        // Show those subjects
        Set<Resource> subjectsWithResourceAsObjects = countSubjectsWithResourceObjects(model1);
        System.out.println("Subjects which have resource as an object:");
        for(Resource r:subjectsWithResourceAsObjects){
            System.out.println("/n"+ r);
        }

        System.out.println();
        //number of numeric predicates
        int numberOfNumericPredicates = countNumericObjects(model1).size();
        System.out.println("Number of numeric predicates:" + numberOfNumericPredicates);
        //show numeric predicates
        Set<Property> numericPredicates = countNumericObjects(model1);
        System.out.println("numeric predicates");
        for (Property p : numericPredicates) {
            System.out.println("/n" + p);
        }

        System.out.println();
        // number of literal predicates
        int numberOfLiteralPredicates = countStringPredicates(model1).size();
        System.out.println("Number of literal predicates:" + numberOfLiteralPredicates);

        //show literal predicates
        Set<Property> literalPredicates = countStringPredicates(model1);
        System.out.println("literal predicates");
        for (Property p : literalPredicates) {
            System.out.println("/n" + p);
        }

        System.out.println();
        //number of properties with date type
        Set<Property> dateProperties = getDatePredicates(model1);
        System.out.println("Number of date predicates:" + dateProperties.size());
        System.out.println("date predicates");
        for (Property p : dateProperties) {
            System.out.println("/n" + p);
        }

        System.out.println();
        // count predicates per subject
        System.out.println("Subjects with their respective number of predicates:");
        Map<Resource, Integer> predicateCountPerSubject = countPredicatesPerSubject(model1);
        // Print the subjects and their predicate count
        predicateCountPerSubject.entrySet().stream()
                .sorted(Map.Entry.<Resource, Integer>comparingByValue().reversed()).limit(25)
                .forEach(entry -> System.out.println("Subject: " + entry.getKey() + ", Predicates count: " + entry.getValue()));

        System.out.println();

        // count number of types of nodes
        System.out.println("Types of nodes:");
        Map<Resource, Integer> typeCount = countTypes(model1);
        // Print the types and their counts from most frequent to the least frequent
        typeCount.entrySet().stream()
                .sorted(Map.Entry.<Resource, Integer>comparingByValue().reversed()).limit(5)
                .forEach(entry -> System.out.println("Type: " + entry.getKey() + ", Count: " + entry.getValue()));


        //number of connected nodes. CHECK!

        /*
        System.out.println();
        List<Set<Resource>> connectedComponents = findConnectedComponents(model1);
        System.out.println("Connected nodes:"+connectedComponents.size());
        int maxComponentsToShow = Math.min(connectedComponents.size(), 25);
        for (int i = 0; i < maxComponentsToShow; i++) {
            Set<Resource> component = connectedComponents.get(i);
            System.out.println("Component " + (i+1) + " has " + component.size() + " nodes:");
            for (Resource resource : component) {
                System.out.println(resource.getURI());
            }
            System.out.println();
        }

         */

        // frequency of predicates
        System.out.println();
        System.out.println("FREQUENCY OF PREDICATES:");
        Map<Property, Integer> predicateFrequencies = countPredicateFrequencies(model1);

        // Convert the frequency map entries to a list
        List<Map.Entry<Property, Integer>> entryList = new ArrayList<>(predicateFrequencies.entrySet());

        // Sort the list in descending order of the values (i.e., frequencies)
        entryList.sort(Map.Entry.<Property, Integer>comparingByValue().reversed());

        // Display the top 25 most frequent predicates
        for (int i = 0; i < Math.min(5, entryList.size()); i++) {
            Map.Entry<Property, Integer> entry = entryList.get(i);
            System.out.println("Predicate: " + entry.getKey() + ", Frequency: " + entry.getValue());
        }

        System.out.println();
        System.out.println("FREQUENCY OF Subjects:");

        Map<Resource, Integer> subjectFrequencies = countSubjectFrequencies(model1);

        // Convert the frequency map entries to a list
        List<Map.Entry<Resource, Integer>> entryList1 = new ArrayList<>(subjectFrequencies.entrySet());

        // Sort the list in descending order of the values (i.e., frequencies)
        entryList1.sort(Map.Entry.<Resource, Integer>comparingByValue().reversed());

        // Display the top 25 most frequent subjects
        for (int i = 0; i < Math.min(3, entryList.size()); i++) {
            Map.Entry<Resource, Integer> entry = entryList1.get(i);
            System.out.println("Subject: " + entry.getKey() + ", Frequency: " + entry.getValue());
        }



        // counting most frequent objects for the most frequent predicates
        System.out.println();
        System.out.println("FREQUENCY OF Objects for the most frequent predicates:");

        Map<Property, Integer> predicateFrequencies1 = countPredicateFrequencies(model1);

        // Convert the frequency map entries to a list
        List<Map.Entry<Property, Integer>> entryList2 = new ArrayList<>(predicateFrequencies.entrySet());

        // Sort the list in descending order of the values (i.e., frequencies)
        entryList2.sort(Map.Entry.<Property, Integer>comparingByValue().reversed());

        // For the top three predicates, count and display the frequencies of their objects
        for (int i = 0; i < Math.min(3, entryList2.size()); i++) {
            Map.Entry<Property, Integer> entry = entryList2.get(i);
            Property predicate = entry.getKey();
            Map<RDFNode, Integer> objectFrequencies = countObjectFrequencies(model1, predicate);

            List<Map.Entry<RDFNode, Integer>> objectList = new ArrayList<>(objectFrequencies.entrySet());
            objectList.sort(Map.Entry.<RDFNode, Integer>comparingByValue().reversed());

            System.out.println("Predicate: " + predicate);
            for (int j = 0; j < Math.min(3, objectList.size()); j++) {
                Map.Entry<RDFNode, Integer> objectEntry = objectList.get(j);
                System.out.println("    Object: " + objectEntry.getKey() + ", Frequency: " + objectEntry.getValue());
            }
        }




    }
}
