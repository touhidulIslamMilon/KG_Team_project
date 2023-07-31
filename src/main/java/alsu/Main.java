package alsu;

import org.apache.jena.rdf.model.*;

import java.util.*;

import static alsu.analyzeGraph.analyzeGraph.*;
import static alsu.normalisation.normlaisationFunctions.normalizeModel;
import static alsu.graphComparison.graphComparison.*;

public class Main {
    public static void main(String[] args) {

        // CHECK NORMALISATION
        /*
        // Load the RDF files
        String file1 = "/Users/alsufathutdinova/Documents/GitHub/KG_Team_project/src/main/java/alsu/graph1.rdf";
        String file2 = "/Users/alsufathutdinova/Documents/GitHub/KG_Team_project/src/main/java/alsu/graph2.rdf";

        // Create two models to hold the RDF graphs
        Model model1 = FileManager.get().loadModel(file1);
        Model model2 = FileManager.get().loadModel(file2);


        // Normalize the models
        Model normalizedModel1 = normalizeModel(model1);
        Model normalizedModel2 = normalizeModel(model2);

        // Output the normalized knowledge graphs
        System.out.println("Normalized Model 1:");
        normalizedModel1.write(System.out, "TTL");

        System.out.println("\nNormalized Model 2:");
        normalizedModel2.write(System.out, "TTL");

        // Normalize dates
        Model normalizedDateModel1 = normalizeDates(normalizedModel1);
        Model normalizedDateModel2 = normalizeDates(normalizedModel2);

        // Output the date-normalized knowledge graphs
        System.out.println("\nDate-normalized Model 1:");
        normalizedDateModel1.write(System.out, "TTL");

        System.out.println("\nDate-normalized Model 2:");
        normalizedDateModel2.write(System.out, "TTL");


        // test resolveLiteralInconsistencies() function
        Model fusedModel = ModelFactory.createDefaultModel();
        fusedModel = resolveLiteralInconsistencies(normalizedModel1,normalizedModel2);
        System.out.println("\nFused Model:");
        fusedModel.write(System.out, "TTL");
        */

        // Check the WeightedFusion function

            // Load the RDF files
            /*
            Model model1 = ModelFactory.createDefaultModel();
            model1.read("graph1.rdf");
            model1=normalizeModel(model1);

            Model model2 = ModelFactory.createDefaultModel();
            model2.read("graph2.rdf");
            model2=normalizeModel(model2);

            Model model3 = ModelFactory.createDefaultModel();
            model3.read("graph3.rdf");
            model3=normalizeModel(model3);

            List<WeightedModel> weightedModels = new ArrayList<>();

            // Add weighted models to the list
            weightedModels.add(new WeightedModel(model1, 0.5));
            weightedModels.add(new WeightedModel(model2, 0.2));
            weightedModels.add(new WeightedModel(model3, 0.3));

            // Sort the weighted models based on their weights
            Collections.sort(weightedModels, Comparator.comparingDouble(WeightedModel::getWeight).reversed());
            */
            // check the order
            /*
                for (WeightedModel weightedModel : weightedModels) {
                    Model model = weightedModel.getModel();
                    StmtIterator stmtIterator = model.listStatements();
                    System.out.println("Model:"+model);
                    while (stmtIterator.hasNext()) {
                        Statement stmt = stmtIterator.next();
                        Resource stmtSubject = stmt.getSubject();
                        Property stmtProperty = stmt.getPredicate();
                        RDFNode stmtObject = stmt.getObject();

                        String output = String.format("Subject: %s | Property: %s | Object: %s",
                                stmtSubject.toString(), stmtProperty.toString(), stmtObject.toString());

                        System.out.println(output);
                    }
                }

             */

            // Test of the WeightedFusion
            /*
            Model testM = WeightedFusion.WeightedFusion(weightedModels);
            StmtIterator stmtIterator = testM.listStatements();
            while (stmtIterator.hasNext()) {
                Statement stmt = stmtIterator.next();
                Resource stmtSubject = stmt.getSubject();
                Property stmtProperty = stmt.getPredicate();
                RDFNode stmtObject = stmt.getObject();

                String output = String.format("Subject: %s | Property: %s | Object: %s",
                        stmtSubject.toString(), stmtProperty.toString(), stmtObject.toString());

                System.out.println(output);
            }

             */


            //////////////////////////////////////////////////////////////////////////////////

            // test AnalyzeGraph functions


            Model model = ModelFactory.createDefaultModel();
            model.read("graph1.rdf");
            model=normalizeModel(model);

            // display the first graph

            System.out.println("First graph:");
            StmtIterator stmtIterator = model.listStatements();
            while (stmtIterator.hasNext()) {
                    Statement stmt = stmtIterator.next();
                    Resource stmtSubject = stmt.getSubject();
                    Property stmtProperty = stmt.getPredicate();
                    RDFNode stmtObject = stmt.getObject();

                    String output = String.format("Subject: %s | Property: %s | Object: %s",
                            stmtSubject.toString(), stmtProperty.toString(), stmtObject.toString());

                    System.out.println(output);
            }

            Model model2 = ModelFactory.createDefaultModel();
            model2.read("graph2.rdf");
            model2=normalizeModel(model2);

            System.out.println("Second graph:");
            StmtIterator stmtIterator1 = model2.listStatements();
            while (stmtIterator1.hasNext()) {
                    Statement stmt = stmtIterator1.next();
                    Resource stmtSubject = stmt.getSubject();
                    Property stmtProperty = stmt.getPredicate();
                    RDFNode stmtObject = stmt.getObject();

                    String output = String.format("Subject: %s | Property: %s | Object: %s",
                            stmtSubject.toString(), stmtProperty.toString(), stmtObject.toString());

                    System.out.println(output);
            }

            Set<Model> models= new HashSet();
            models.add(model);
            models.add(model2);

            System.out.println("TESTING THE ANALYSIS FUNCTIONS:");
            System.out.println("//////////////////////////////////////////////////////////////");
            // counting overall number of predicates for all subjects

            int numberOfPredicates = numberOfPredicates(model);
            System.out.println("Overall number of predicates:" +numberOfPredicates);

            // counting the number of subjects

            int numberOfSubjects = numberOfSubjects(model);
            System.out.println("Number of subjects: "+ numberOfSubjects);

            // counting the number of unique objects

            int numberOfObjects = numberOfObjects(model);
            System.out.println("Number of  objects: "+ numberOfObjects);

            // number of functional properties

            int numberOfFunctionalProperties = countFunctionalProperties(model);
            System.out.println("Number of functional properties: "+ numberOfFunctionalProperties);

            // counting the number of predicates with numeric values. Displaying predicate's name as well.

            Set<Property> setOfNumericPredicates = countNumericObjects(model);
            int numberOfNumericPredicates = setOfNumericPredicates.size();
            System.out.println("Number of overall numeric predicates: "+numberOfNumericPredicates);

            // counting the number of predicates with string values. Displaying respective predicates
            Set<Property> setOfLiteralObjects = countStringPredicates(model);
            int numberOfLiteralObjects = setOfLiteralObjects.size();
            System.out.println("Number of overall literal predicates: "+numberOfLiteralObjects);

            for (Property property : setOfLiteralObjects) {
                    System.out.println(property);
            }

            // counting the number of subjects that have a resource as an objects
            int numberOfResourcesAsObjects = countSubjectsWithResourceObjects(model);
            System.out.println("Number of overall subjects with resources as an objects: "+numberOfResourcesAsObjects);

            Set<Property> propertiesWithDateTypeObjects = getDatePredicates(model);
            System.out.println("Number of date type predicates "+propertiesWithDateTypeObjects.size());


            System.out.println("TESTING THE COMPARISON FUNCTIONS");
            System.out.println("//////////////////////////////////////////////////////////////");
            Map<Resource, Map<Property, Set<RDFNode>>> subjectsWithDifferentObjects = getSubjectsWithDifferentObjects(models);
            displaySubjectsObjects(subjectsWithDifferentObjects);

            System.out.println("//////////////////////////////////////////////////////////////");




            /////////////////////////////////////////////////////////////////////////////////////

            // Graph comparison test

            // function for counting the number of the same properties in the fused 2 graphs
            /*
            // First graph
            Model model = ModelFactory.createDefaultModel();
            model.read("graph1.rdf");
            model=normalizeModel(model);

            //Second graph
            Model model2 = ModelFactory.createDefaultModel();
            model2.read("graph2.rdf");
            model2=normalizeModel(model2);

            // used Max's function to merge to graphs
            //Model fusedGraphs = model.add(model2);

            // display the first graph

            System.out.println("First graph:");
            StmtIterator stmtIterator = model.listStatements();
            while (stmtIterator.hasNext()) {
                    Statement stmt = stmtIterator.next();
                    Resource stmtSubject = stmt.getSubject();
                    Property stmtProperty = stmt.getPredicate();
                    RDFNode stmtObject = stmt.getObject();

                    String output = String.format("Subject: %s | Property: %s | Object: %s",
                            stmtSubject.toString(), stmtProperty.toString(), stmtObject.toString());

                    System.out.println(output);
            }


            // display the second graph
            /*
            System.out.println("Second graph:");
            StmtIterator stmtIterator1 = model2.listStatements();
            while (stmtIterator1.hasNext()) {
                    Statement stmt1 = stmtIterator1.next();
                    Resource stmtSubject = stmt1.getSubject();
                    Property stmtProperty = stmt1.getPredicate();
                    RDFNode stmtObject = stmt1.getObject();

                    String output1 = String.format("Subject: %s | Property: %s | Object: %s",
                            stmtSubject.toString(), stmtProperty.toString(), stmtObject.toString());

                    System.out.println(output1);

            }


            // Count the number of similar properties of 2 graphs
            int numberOfSimilarProperties =  countSimilarProperties(model,model2);
            System.out.println("Number of similar properties: "+ numberOfSimilarProperties);

            // Count the number of similar objects
            int numberOfSimilarObjects = countSimilarObjects(model,model2);
            System.out.println("Number of similar objects:"+ numberOfSimilarObjects);

            // Count the number of similar subjects
            int numberOfSimilarSubjects = countSimilarSubjects(model,model2);
            System.out.println("Number of similar subjects:"+ numberOfSimilarSubjects);

            //shared predicates and respective types
            Map<String, Set<String>> comparisonResult = comparePredicatesAndObjectTypes(model, model2);
            for (Map.Entry<String, Set<String>> entry : comparisonResult.entrySet()) {
                    System.out.println("Predicate: " + entry.getKey());
                    System.out.println("Object types: " + entry.getValue());
            }

             */


            /////////////////////////////////////////////////////////////////////////////////////////
    }
}
