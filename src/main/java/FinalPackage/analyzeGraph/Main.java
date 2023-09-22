package FinalPackage.analyzeGraph;

import FinalPackage.LoadRDF;
import org.apache.jena.rdf.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static FinalPackage.analyzeGraph.SophisticatedAnalysis.*;
import static FinalPackage.analyzeGraph.analyzeGraph.*;

public class Main {
    public static void main(String[] args) {
        Model model1 = LoadRDF.getModel("swtor.rdf");

        //model1.write(System.out, "RDF/XML-ABBREV");
        //normalizeModel(model1);

        //number of statements
        System.out.println("Number of statements:" + model1.size());

        System.out.println();

        //number of subjects
        int numberOfSubjects = numberOfSubjects(model1).size();
        System.out.println("Number of subjects:" + numberOfSubjects);

        // show those subjects, only for usage with small graphs
        System.out.println("Subjects: ");
        int counter = 0;
        for(Resource r : numberOfSubjects(model1)) {
            if (counter >= 10) {
                break;
            }
            System.out.println(r);
            counter++;
        }

        System.out.println();

        //////////////////////////////////////////////////////////////////

        //number of unique predicates
        int numberOfPredicates = numberOfPredicates(model1).size();
        System.out.println("Number of predicates:" + numberOfPredicates);

        // show those predicates
        int counter1 = 0;
        Set<Property> uniquePredicates = numberOfPredicates(model1);
        System.out.println("Unique predicates:");
        for(Property r:uniquePredicates){
            if(counter1 >= 10){
                break;
            }
            System.out.println("/n"+r);
            counter1++;
        }

        System.out.println();

        //////////////////////////////////////////////////////////////////

        //number of objects
        int numberOfObjects = numberOfObjects(model1).size();
        System.out.println("Number of unique objects:" + numberOfObjects);

        System.out.println();

        //////////////////////////////////////////////////////////////////

        // number of subjects with resource as the object
        int numberOfSubWithResourceObj = countSubjectsWithResourceObjects(model1).size();
        System.out.println("Number of subjects with resources as objects:" + numberOfSubWithResourceObj);

        // Show those subjects
        int counter2 = 0;
        Set<Resource> subjectsWithResourceAsObjects = countSubjectsWithResourceObjects(model1);
        System.out.println("Subjects which have resource as an object:");
        for(Resource r:subjectsWithResourceAsObjects){
            if(counter2 >= 10){
                break;
            }
            System.out.println("/n"+ r);
            counter2++;
        }

        System.out.println();

        //////////////////////////////////////////////////////////////////

        //number of numeric predicates
        int numberOfNumericPredicates = countNumericPredicates(model1).size();
        System.out.println("Number of numeric predicates:" + numberOfNumericPredicates);

        //show numeric predicates.
        int counter3=0;
        Set<Property> numericPredicates = countNumericPredicates(model1);
        System.out.println("numeric predicates");
        for (Property p : numericPredicates) {
            if(counter3 >= 10){
                break;
            }
            System.out.println("/n" + p);
            counter3++;
        }

        System.out.println();

        //////////////////////////////////////////////////////////////////

        // number of literal predicates
        int numberOfLiteralPredicates = countStringPredicates(model1).size();
        System.out.println("Number of literal predicates:" + numberOfLiteralPredicates);

        //show literal predicates.
        int counter4=0;
        Set<Property> literalPredicates = countStringPredicates(model1);
        System.out.println("literal predicates");
        for (Property p : literalPredicates) {
            if(counter4>=10){
                break;
            }
            System.out.println("/n" + p);
            counter4++;
        }

        System.out.println();

        //////////////////////////////////////////////////////////////////

        //number of properties with type date

        Set<Property> dateProperties = getDatePredicates(model1);
        System.out.println("Number of date predicates:" + dateProperties.size());

        // show date predicates
        System.out.println("date predicates");
        for (Property p : dateProperties) {
            System.out.println("/n" + p);
        }

        System.out.println();

        //////////////////////////////////////////////////////////////////
        /*
        // count predicates per subject
        System.out.println("Subjects with their respective number of predicates:");
        Map<Resource, Integer> predicateCountPerSubject = countPredicatesPerSubject(model1);

        // Show the subjects and their predicate count
        predicateCountPerSubject.entrySet().stream()
                .sorted(Map.Entry.<Resource, Integer>comparingByValue().reversed()).limit(25)
                .forEach(entry -> System.out.println("Subject: " + entry.getKey() + ", Predicates count: " + entry.getValue()));

        */
        System.out.println();

        //////////////////////////////////////////////////////////////////

        //number of connected nodes.(it is like clusters)
        List<Set<Resource>> connectedComponents = findConnectedComponents(model1);
        System.out.println("Connected nodes:"+connectedComponents.size());

        // show those connected nodes
        System.out.println("Components and their nodes");
        int maxComponentsToShow = connectedComponents.size();  // Show all components
        int maxNodesToShow = 10;  // Limit to the first 10 nodes per component

        for (int i = 0; i < maxComponentsToShow; i++) {
            Set<Resource> component = connectedComponents.get(i);
            System.out.println("Component " + (i+1) + " has " + component.size() + " nodes:");

            int nodeCount = 0;
            for (Resource resource : component) {
                if (nodeCount >= maxNodesToShow) {
                    System.out.println("...and more");
                    break;
                }
                System.out.println(resource.getURI());
                nodeCount++;
            }
        }

        System.out.println();

        //////////////////////////////////////////////////////////////////

        // frequency of predicates in descending order.

        System.out.println("frequency of predicates:");
        Map<Property, Integer> predicateFrequencies = countPredicateFrequencies(model1);

        // Convert the frequency map entries to a list
        List<Map.Entry<Property, Integer>> entryList = new ArrayList<>(predicateFrequencies.entrySet());

        // Sort the list in descending order of the values (i.e., frequencies)
        entryList.sort(Map.Entry.<Property, Integer>comparingByValue().reversed());

        // Display the top 25 most frequent predicates
        for (int i = 0; i < Math.min(20, entryList.size()); i++) {
            Map.Entry<Property, Integer> entry = entryList.get(i);
            System.out.println("Predicate: " + entry.getKey() + ", Frequency: " + entry.getValue());
        }

        System.out.println();

        //////////////////////////////////////////////////////////////////

        // counting most frequent objects for the most frequent predicates. UNNECESSARY
        /*
        System.out.println("FREQUENCY OF Objects for the most frequent predicates:");

        Map<Property, Integer> predicateFrequencies1 = countPredicateFrequencies(model1);

        // Convert the frequency map entries to a list
        List<Map.Entry<Property, Integer>> entryList2 = new ArrayList<>(predicateFrequencies1.entrySet());

        // Sort the list in descending order of the values (i.e., frequencies)
        entryList2.sort(Map.Entry.<Property, Integer>comparingByValue().reversed());

        // For the top three predicates, count and display the frequencies of their objects
        for (int i = 0; i < Math.min(10, entryList2.size()); i++) {
            Map.Entry<Property, Integer> entry = entryList2.get(i);
            Property predicate = entry.getKey();
            Map<RDFNode, Integer> objectFrequencies = countObjectFrequencies(model1, predicate);

            List<Map.Entry<RDFNode, Integer>> objectList = new ArrayList<>(objectFrequencies.entrySet());
            objectList.sort(Map.Entry.<RDFNode, Integer>comparingByValue().reversed());

            System.out.println("Predicate: " + predicate);
            for (int j = 0; j < Math.min(10, objectList.size()); j++) {
                Map.Entry<RDFNode, Integer> objectEntry = objectList.get(j);
                System.out.println("    Object: " + objectEntry.getKey() + ", Frequency: " + objectEntry.getValue());
            }
        }

        System.out.println();

        //////////////////////////////////////////////////////////////////
         */

        // display functional properties and number of functional predicates

        // Adding the model to a list since the original function expects a List<Model>
        List<Model> models = new ArrayList<>();
        models.add(model1);

        // Get the functional properties sorted in descending order
        List<Property> sortedFunctionalProperties = getFunctionalPropertiesInDescOrder(models);

        // Display the sorted functional properties
        System.out.println("Functional Properties in Descending Order:");
        for (Property property : sortedFunctionalProperties) {
            System.out.println(property);
        }

        System.out.println();

        // Count and display the number of functional properties
        int numOfFunctionalProperties = sortedFunctionalProperties.size();
        System.out.println("Number of Functional Properties: " + numOfFunctionalProperties);

        System.out.println();

        //////////////////////////////////////////////////////////////////

        // print the node centrality. Is it useful? TODO: Show it in descending order?
        int counter6 =0;
        Map<RDFNode, Integer> centralityMap = calculateNodeCentrality(model1);
        System.out.println("Node centrality:");
        for (Map.Entry<RDFNode, Integer> entry : centralityMap.entrySet()) {
            if(counter6>=30){
                break;
            }
            System.out.println(entry.getKey() + ": " + entry.getValue());
            counter6++;
        }

        System.out.println();

        //////////////////////////////////////////////////////////////////

        // types of nodes and most frequent ones. Display objects of those types as well.
        System.out.println("Types ");
        Map<RDFNode, Integer> typeCount1 = countTypesForFrequency(model1);
        showTopTypesAndObjects(model1, typeCount1, 10, 3);

        System.out.println();

        //////////////////////////////////////////////////////////////////

        //predicates with resource as objects
        Set<Property> predicatesWithResourceAsObjects = countPredicatesWithResourceObjects(model1);
        System.out.println("Predicates with resource as objects:");
        for (Property property : predicatesWithResourceAsObjects) {
            System.out.println("Predicate:"+ property);
        }

        System.out.println();

        //////////////////////////////////////////////////////////////////

        //show predicates and their objects which are presented in descending order

        System.out.println("Predicates and their objects");
        showPredicatesAndObjects2(model1);

        System.out.println();

        //////////////////////////////////////////////////////////////////
        System.out.println("Graph radius");
        // Initialize your model here, for instance, read it from a file
        Model model = ModelFactory.createDefaultModel();
        // model.read("your_file.rdf");

        // Assuming the functions computeDiameter and computeRadius are static methods in another class called GraphAnalysis
        int diameter = SophisticatedAnalysis.computeDiameter(model);
        int radius = SophisticatedAnalysis.computeRadius(model);

        // Print or otherwise use the diameter and radius
        System.out.println("The diameter of the graph is: " + diameter);
        System.out.println("The radius of the graph is: " + radius);

    }
}
