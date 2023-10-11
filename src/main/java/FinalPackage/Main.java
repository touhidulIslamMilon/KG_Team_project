package FinalPackage;

import FinalPackage.Merging.Merger;
import org.apache.jena.rdf.model.*;

import java.util.*;

import static FinalPackage.LoadRDF.getModel;
import static FinalPackage.LoadRDF.readAllTestCases;
import static FinalPackage.Merging.FunctionalPropertyFinder.findFunctionalProperties;
import static FinalPackage.analyzeGraph.SophisticatedAnalysis.*;
import static FinalPackage.analyzeGraph.SophisticatedAnalysis.showPredicatesAndObjects2;
import static FinalPackage.analyzeGraph.analyzeGraph.*;
import static FinalPackage.conflictDetection.ConflictFinder.*;

public class Main {

    public static void main(String[] args) {

        /*
        Model model1 = LoadRDF.getModel("test1.rdf");
        System.out.println("Graph 1");
        model1.write(System.out, "RDF/XML-ABBREV");


        Model model2 = LoadRDF.getModel("test2.rdf");
        System.out.println("Graph 2");
        model2.write(System.out, "RDF/XML-ABBREV");

        Model model3 = LoadRDF.getModel("test3.rdf");
        System.out.println("Graph 3");
        model3.write(System.out, "RDF/XML-ABBREV");


        //Model model4 = LoadRDF.getModel("swtor.rdf");
        //Model model5 = LoadRDF.getModel("starwars.rdf");
        //Model model6 = LoadRDF.getModel("swg.rdf");
        System.out.println("Read");


        /*
            Test 4: Merge more than two graphs
        */
        /*
        List<Model> models = new ArrayList<>();


        models.add(model1);
        models.add(model2);
        models.add(model3);
        */

        /*
            Test conflict types
         */

        List<Model> models = readAllTestCases();
        Model mergedModel=models.get(0);
        //Model mergedModel = Merger.mergeGraphs(models);
        List<Model> models1 = new ArrayList<>();
        models1.add(mergedModel);
        //number of statements
        System.out.println("Number of statements:" + mergedModel.size());
        System.out.println();

        //number of subjects
        int numberOfSubjects = numberOfSubjects(mergedModel).size();
        System.out.println("Number of subjects:" + numberOfSubjects);

        // show those subjects, only for usage with small graphs
        System.out.println("Subjects: ");
        int counter = 0;
        for(Resource r : numberOfSubjects(mergedModel)) {
            if(counter >= 10){
                break;
            }
            counter++;
            System.out.println(r);

        }

        System.out.println();

        //////////////////////////////////////////////////////////////////

        //number of unique predicates
        int numberOfPredicates = numberOfPredicates(mergedModel).size();
        System.out.println("Number of predicates:" + numberOfPredicates);

        // show those predicates
        int counter1 = 0;
        Set<Property> uniquePredicates = numberOfPredicates(mergedModel);
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
        int numberOfObjects = numberOfObjects(mergedModel).size();
        System.out.println("Number of unique objects:" + numberOfObjects);

        System.out.println();

        //////////////////////////////////////////////////////////////////

        // number of subjects with resource as the object
        int numberOfSubWithResourceObj = countSubjectsWithResourceObjects(mergedModel).size();
        System.out.println("Number of subjects with resources as objects:" + numberOfSubWithResourceObj);

        // Show those subjects
        int counter2 = 0;
        Set<Resource> subjectsWithResourceAsObjects = countSubjectsWithResourceObjects(mergedModel);
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
        int numberOfNumericPredicates = countNumericPredicates(mergedModel).size();
        System.out.println("Number of numeric predicates:" + numberOfNumericPredicates);

        //show numeric predicates.
        int counter3=0;
        Set<Property> numericPredicates = countNumericPredicates(mergedModel);
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
        int numberOfLiteralPredicates = countStringPredicates(mergedModel).size();
        System.out.println("Number of literal predicates:" + numberOfLiteralPredicates);

        //show literal predicates.
        int counter4=0;
        Set<Property> literalPredicates = countStringPredicates(mergedModel);
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

        Set<Property> dateProperties = getDatePredicates(mergedModel);
        System.out.println("Number of date predicates:" + dateProperties.size());

        // show date predicates
        System.out.println("date predicates");
        for (Property p : dateProperties) {
            System.out.println("/n" + p);
        }

        System.out.println();

        //////////////////////////////////////////////////////////////////

        //number of connected nodes.(it is like clusters)
        List<Set<Resource>> connectedComponents = findConnectedComponents(mergedModel);
        System.out.println("Connected nodes:"+connectedComponents.size());

        // show those connected nodes
        System.out.println("Components and their nodes");
        int maxComponentsToShow = connectedComponents.size();  // Show all components
        int maxNodesToShow = 15;  // Limit to the first 10 nodes per component

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
        Map<Property, Integer> predicateFrequencies = countPredicateFrequencies(mergedModel);

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

        // Get the functional properties sorted in descending order

        List<Property> sortedFunctionalProperties = getFunctionalPropertiesInDescOrder(models1);

        // Count and display the number of functional properties
        int numOfFunctionalProperties = sortedFunctionalProperties.size();
        System.out.println("Number of Functional Properties: " + numOfFunctionalProperties);
        // Display the sorted functional properties
        System.out.println("Functional Properties in Descending Order:");
        int counter7 =0;
        for (Property property : sortedFunctionalProperties) {
            if(counter7>=10){
                break;
            }
            System.out.println(property);
            counter7++;
        }



        System.out.println();


        //////////////////////////////////////////////////////////////////

        // print the node centrality. Is it useful?
        int counter6 =0;
        Map<RDFNode, Integer> sortedCentralityMap = calculateNodeCentrality(mergedModel);

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
        Map<RDFNode, Integer> typeCount4 = countTypesForFrequency(mergedModel);
        showTopTypesAndObjects(mergedModel, typeCount4, 10, 3);

        System.out.println();

        //////////////////////////////////////////////////////////////////

        //predicates with resource as objects
        Set<Property> predicatesWithResourceAsObjects = countPredicatesWithResourceObjects(mergedModel);
        System.out.println("Predicates with resource as objects:");
        for (Property property : predicatesWithResourceAsObjects) {
            System.out.println("Predicate:"+ property);
        }

        System.out.println();

        //////////////////////////////////////////////////////////////////

        //show predicates and their objects which are presented in descending order

        System.out.println("Predicates and their objects");
        showPredicatesAndObjects2(mergedModel);

        System.out.println();

        //////////////////////////////////////////////////////////////////

        /*
        findFunctionalProperties(models);
        identifyAndPrintConflicts(models);

         */
        /*
        Map<Model, Integer> modelPriorities = new HashMap<>();
        modelPriorities.put(model1, 1);
        modelPriorities.put(model2, 2);
        modelPriorities.put(model3, 3);
        Model mergedModel1 = Merger.mergeGraphs(modelPriorities);
        System.out.println("Merged1");
        mergedModel1.write(System.out, "RDF/XML-ABBREV");

        Model mergedModel = Merger.mergeGraphs(models);
        System.out.println("Merged");
        mergedModel.write(System.out, "RDF/XML-ABBREV");
        */

    }
}

//Old

        /*
            Test 2: Functional Properties


        System.out.println("\nTest 2");

        int count = 0;
        int functional = 0;


        StmtIterator stmtIterator = model3.listStatements();
        while (stmtIterator.hasNext()) {
            Statement statement = stmtIterator.next();
            count ++;
            Property predicate = statement.getPredicate();
            Resource subject = statement.getSubject();
            System.out.println("Subject: " + subject.getURI() + "\nPredicate: " + predicate.getURI());
            RDFNode object = statement.getObject();
            if (object.isResource()) {
                Resource resource = object.asResource();
                System.out.println("Object (Resource): " + resource.getURI() + "\n");
            } else if (object.isLiteral()) {
                Literal literal = object.asLiteral();
                System.out.println("Object (Literal): " + literal.getLexicalForm() + "\n");
            }

            if (FunctionalPropertyDetector.isFunctionalProperty(model3, predicate)){
                functional++;
            }
        }

        System.out.println("\nFunctionalProperties");

        StmtIterator stmtIterator2 = model4.listStatements();
        while (stmtIterator2.hasNext()) {
            Statement statement = stmtIterator2.next();
            Property predicate = statement.getPredicate();
            count++;

            if (FunctionalPropertyDetector.isFunctionalProperty(model3, predicate)){
                functional++;
                Resource subject = statement.getSubject();
                RDFNode object = statement.getObject();
                if (object.isResource()) {
                    Resource resource = object.asResource();
                    System.out.println("Subject: " + subject.getURI() + "\nPredicate: " + predicate.getURI());
                    System.out.println("Object (Resource): " + resource.getURI() + "\n");
                } else if (object.isLiteral()) {
                    Literal literal = object.asLiteral();
                    System.out.println("Subject: " + subject.getURI() + "\nPredicate: " + predicate.getURI());
                    System.out.println("Object (Literal): " + literal.getLexicalForm() + "\n");
                }
            }

        }

        System.out.println("\nNonFunctionalProperties");

        StmtIterator stmtIterator3 = model3.listStatements();
        while (stmtIterator3.hasNext()) {
            Statement statement = stmtIterator3.next();
            Property predicate = statement.getPredicate();

            if (!FunctionalPropertyDetector.isFunctionalProperty(model3, predicate)){
                Resource subject = statement.getSubject();
                RDFNode object = statement.getObject();
                if (object.isResource()) {
                    Resource resource = object.asResource();
                    System.out.println("Subject: " + subject.getURI() + "\nPredicate: " + predicate.getURI());
                    System.out.println("Object (Resource): " + resource.getURI() + "\n");
                } else if (object.isLiteral()) {
                    Literal literal = object.asLiteral();
                    System.out.println("Subject: " + subject.getURI() + "\nPredicate: " + predicate.getURI());
                    System.out.println("Object (Literal): " + literal.getLexicalForm() + "\n");
                }
            }

        }

        System.out.println("All pairs: " + count);
        System.out.println("Functional pairs: " + functional);
        */
