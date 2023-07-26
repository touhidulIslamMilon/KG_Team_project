package max.kg_merger;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;

public class Merger {

    public static Model mergeGraphs(Model model1, Model model2){
        Model targetModel = ModelFactory.createDefaultModel();

        // Merge properties from model1
        StmtIterator iter = model1.listStatements();
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement();
            Resource subject = stmt.getSubject();
            Property predicate = stmt.getPredicate();
            RDFNode object = stmt.getObject();

            // Check if the Subject-Predicate combination exists in model2
            if (!model2.contains(subject, predicate)) {
                targetModel.add(stmt);
            } else {
                // Call a method to check if the objects are the same and handle conflict
                Statement stmt2 = model2.getProperty(subject, predicate);
                RDFNode object2 = stmt2.getObject();
                if (object.equals(object2)) {
                    targetModel.add(stmt);
                } else {
                    resolveConflict(subject, predicate, object, object2);
                }
            }
        }

        // Merge properties from model2 that are not in model1
        iter = model2.listStatements();
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement();
            Resource subject = stmt.getSubject();
            Property predicate = stmt.getPredicate();
            RDFNode object = stmt.getObject();

            // Check if the Subject-Predicate combination exists in model1
            if (!model1.contains(subject, predicate)) {
                targetModel.add(stmt);
            }
        }

        return targetModel;
    }

    // Method to resolve conflicts when the same subject and predicate combination have different objects in both graphs
    private static void resolveConflict(Resource subject, Property predicate, RDFNode object1, RDFNode object2) {

        //Both objects are resources -> Both objects are literals -> one is a literal, and one is a resource
        if (object1.isResource() && object2.isResource()) {
            // Handle conflict between two resources
            Resource resource1 = object1.asResource();
            Resource resource2 = object2.asResource();
            // Your resource conflict resolution logic here.
            System.out.println("Conflict detected for resources: " + subject + " " + predicate);
            System.out.println("Resource in model1: " + resource1);
            System.out.println("Resource in model2: " + resource2);
        } else if (object1.isLiteral() && object2.isLiteral()) {
            // Handle conflict between two literals
            Literal literal1 = object1.asLiteral();
            Literal literal2 = object2.asLiteral();
            // Your literal conflict resolution logic here.
            System.out.println("Conflict detected for literals: " + subject + " " + predicate);
            System.out.println("Literal in model1: " + literal1);
            System.out.println("Literal in model2: " + literal2);
        } else {
            // Handle conflict between a resource and a literal
            Resource resource = object1.isResource() ? object1.asResource() : object2.asResource();
            Literal literal = object1.isLiteral() ? object1.asLiteral() : object2.asLiteral();
            // Your resource-literal conflict resolution logic here.
            System.out.println("Conflict detected for resource-literal: " + subject + " " + predicate);
            System.out.println("Resource in model: " + resource);
            System.out.println("Literal in model: " + literal);
        }

        // You can modify this method to suit your specific conflict resolution requirements.
    }


}
