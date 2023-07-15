package alsu;

import alsu.kg_fusion.WeightedFusion;
import org.apache.jena.rdf.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static alsu.normalisation.normlaisationFunctions.normalizeModel;


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



    }
}
