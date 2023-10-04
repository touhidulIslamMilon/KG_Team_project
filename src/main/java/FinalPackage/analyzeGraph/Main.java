package FinalPackage.analyzeGraph;

import FinalPackage.LoadRDF;
import org.apache.jena.rdf.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static FinalPackage.LoadRDF.readAllTestCases;
import static FinalPackage.analyzeGraph.SophisticatedAnalysis.*;
import static FinalPackage.analyzeGraph.analyzeGraph.*;

public class Main {
    public static void main(String[] args) {

        /*
            Test for BIGDATA
         */

        List<Model> listOfModelsToAssess = readAllTestCases();
        Model model1 = listOfModelsToAssess.get(1);

        //Model model1 = LoadRDF.getModel("test1.rdf");
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

        // print the node centrality. Is it useful?
        int counter6 =0;
        Map<RDFNode, Integer> sortedCentralityMap = calculateNodeCentrality(model1);

        // Print the sorted map in a nice way
        System.out.println("Node Centrality Scores: ");
        for (Map.Entry<RDFNode, Integer> entry : sortedCentralityMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
            counter6++;
            if(counter6>10){
                break;
            }
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

    }
}
