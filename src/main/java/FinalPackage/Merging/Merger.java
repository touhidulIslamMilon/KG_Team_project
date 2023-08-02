package FinalPackage.Merging;

import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.riot.RDFWriter;
import org.apache.jena.vocabulary.RDF;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static FinalPackage.Merging.FunctionalPropertyDetector.isFunctionalProperty;

public class Merger {

    // Method to merge multiple knowledge graphs from a list of RDF models
    public static Model mergeGraphs(List<Model> models) {
        Model targetModel = ModelFactory.createDefaultModel();
        Model resolvedModel = ModelFactory.createDefaultModel();
        boolean functionalProperty = true;

        // Iterate over each model and merge its properties
        for (Model model : models) {
            StmtIterator iter = model.listStatements();
            while (iter.hasNext()) {
                Statement stmt = iter.nextStatement();
                Resource subject = stmt.getSubject();
                Property predicate = stmt.getPredicate();

                // Check if the subject-predicate combination has been resolved before
                if (!resolvedModel.contains(subject, predicate)) {
                    // Check if the Subject-Predicate combination exists in the targetModel
                    if (!targetModel.contains(subject, predicate)) {
                        targetModel.add(stmt);
                    } else {
                        // Remove the old statements (if any) from the targetModel
                        targetModel.removeAll(subject, predicate, null);

                        //Check if functional. If not for every single model: Add all statements with this subject predicate comnbination
                        for (Model model1 : models) {
                            functionalProperty = isFunctionalProperty(model1, predicate);
                            if (!functionalProperty){
                                Model allStatements = getDistinctStatements(models, subject, predicate);
                                targetModel.add(allStatements);
                                resolvedModel.add(stmt);
                                System.out.println("Not Functional: " + subject + predicate);
                                break;
                            }
                        }

                        if (functionalProperty){
                            // Call a method to find the resolved value and add it to the targetModel
                            System.out.println("Functional: " + subject + predicate);
                            RDFNode resolvedObject = getResolvedObjectValue(models, subject, predicate);
                            targetModel.add(subject, predicate, resolvedObject);
                            // Mark the statement as resolved by adding it to the resolvedModel
                            resolvedModel.add(stmt);
                        }
                    }

                }
                functionalProperty = true;
            }
        }

        return targetModel;
    }

    // Method to find the resolved value for a subject-predicate combination from multiple models
    public static RDFNode getResolvedObjectValue(List<Model> models, Resource subject, Property predicate) {
        RDFNode commonObject = null;
        boolean hasConflict = false;
        RDFNode dummyObject = null;

        for (Model model : models) {
            StmtIterator iter = model.listStatements(subject, predicate, (RDFNode) null);
            if (iter.hasNext()) {
                dummyObject = iter.nextStatement().getObject();
            }
        }

        for (Model model : models) {
            StmtIterator iter = model.listStatements(subject, predicate, (RDFNode) null);
            if (iter.hasNext()) {
                RDFNode object = iter.nextStatement().getObject();
                if (commonObject == null) {
                    commonObject = object;
                } else if (!commonObject.equals(object)) {
                    hasConflict = true;
                    Set<RDFNode> allObjects = getAllObjects(models, subject, predicate);
                    System.out.println("Conflict: "+ subject + predicate);
                    break;
                } else {
                    System.out.println("Dummy: " + subject + predicate);
                }
            }
        }

        return hasConflict ? dummyObject : commonObject;
    }

    public static Model getDistinctStatements(List<Model> models, Resource subject, Property predicate) {
        Model distinctStatements = ModelFactory.createDefaultModel();

        for (Model model : models) {
            StmtIterator iter = model.listStatements(subject, predicate, (RDFNode) null);
            while (iter.hasNext()) {
                Statement stmt = iter.nextStatement();
                distinctStatements.add(stmt);
            }
        }

        return distinctStatements;
    }

    public static Set<RDFNode> getAllObjects(List<Model> models, Resource subject, Property predicate) {
        Set<RDFNode> objects = new HashSet<>();

        for (Model model : models) {
            StmtIterator iter = model.listStatements(subject, predicate, (RDFNode) null);
            while (iter.hasNext()) {
                Statement stmt = iter.nextStatement();
                objects.add(stmt.getObject());
            }
        }

        return objects;
    }


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

        fusionStrategy fusionStrategy = new fusionStrategy();
        //Both objects are resources -> Both objects are literals -> one is a literal, and one is a resource
        if (object1.isResource() && object2.isResource()) {
            // Handle conflict between two resources
            Resource resource1 = object1.asResource();
            Resource resource2 = object2.asResource();
            // Your resource conflict resolution logic here.
            fusionStrategy.fuseResources(resource1, resource2);

            System.out.println("Conflict detected for resources: " + subject + " " + predicate);
            System.out.println("Resource in model1: " + resource1);
            System.out.println("Resource in model2: " + resource2);
        } else if (object1.isLiteral() && object2.isLiteral()) {
            // Handle conflict between two literals
            Literal literal1 = object1.asLiteral();
            Literal literal2 = object2.asLiteral();
            // Your literal conflict resolution logic here.
            //Here are the code that take the two literals and fuse them
            fusionStrategy.fuseLiterals(literal1, literal2);

            System.out.println("Conflict detected for literals: " + subject + " " + predicate);
            System.out.println("Literal in model1: " + literal1);
            System.out.println("Literal in model2: " + literal2);
        } else {
            // Handle conflict between a resource and a literal
            Resource resource = object1.isResource() ? object1.asResource() : object2.asResource();
            Literal literal = object1.isLiteral() ? object1.asLiteral() : object2.asLiteral();
            // Your resource-literal conflict resolution logic here.
            fusionStrategy.fuseResourceLiteral(resource, literal);

            System.out.println("Conflict detected for resource-literal: " + subject + " " + predicate);
            System.out.println("Resource in model: " + resource);
            System.out.println("Literal in model: " + literal);
        }

        // You can modify this method to suit your specific conflict resolution requirements.
    }


}
