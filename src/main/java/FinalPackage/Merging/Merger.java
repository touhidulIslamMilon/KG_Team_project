package FinalPackage.Merging;

import org.apache.jena.rdf.model.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static FinalPackage.Merging.FunctionalPropertyDetector.isFunctionalProperty;

public class Merger {

    /*
        Method to merge multiple knowledge graphs from a list of RDF models
        Target Model is the merged model which is returned
        Resolved Model is a helping model, it contains all subject, predicate combination, where we checked for the right object already

        Iterate over each model and merge its properties
        1.  Check if the subject-predicate combination has been resolved before
        2.  Check if the Subject-Predicate combination exists in the targetModel
        3.  Remove the old statements (if any) from the targetModel
        4.  Check in all models if predicate is functional (use method getFunctionalProperties()).
        5.  If it is functional in at least one model
            -> we assume its functional
            -> only one object for subject predicate combination allowed
            -> Call Function to resolve: Only one valid object for subject, predicate combination
        6.  If predicate is not functional for every single model
                 Add all statements with this subject predicate combination to the target model
                 and add one statement to resolvedModel (-> subject, predicate have been checked)
     */
    public static Model mergeGraphs(List<Model> models) {
        Model targetModel = ModelFactory.createDefaultModel();
        Model resolvedModel = ModelFactory.createDefaultModel();
        boolean functionalProperty = true;
        List<Property> functionalProperties = FunctionalPropertyFinder.findFunctionalProperties(models);
        //List<Property> functionalProperties = FunctionalPropertyDetector.getFunctionalProperties(models);
        System.out.println("Functional Properties");
        for (Property property : functionalProperties) {
            System.out.println(property.getURI());
        }
        System.out.println("Func ended");

        for (Model model : models) {
            StmtIterator iter = model.listStatements();
            while (iter.hasNext()) {
                Statement stmt = iter.nextStatement();
                Resource subject = stmt.getSubject();
                Property predicate = stmt.getPredicate();

                if (!resolvedModel.contains(subject, predicate)) {
                    if (!targetModel.contains(subject, predicate)) {
                        targetModel.add(stmt);
                        System.out.println("added: " + subject + predicate);
                    } else {
                        targetModel.removeAll(subject, predicate, null);
                        if (functionalProperties.contains(predicate)) {
                            System.out.println("Functional: " + subject + predicate);
                            // Call a method to find the resolved value and add it to the targetModel
                            RDFNode resolvedObject = getResolvedObjectValue(models, subject, predicate);
                            targetModel.add(subject, predicate, resolvedObject);
                            resolvedModel.add(stmt);
                        } else {
                            Model allStatements = getDistinctStatements(models, subject, predicate);
                            targetModel.add(allStatements);
                            resolvedModel.add(stmt);
                            System.out.println("Not Functional: " + subject + predicate);

                        }
                    }

                }
            }
        }
        return targetModel;
    }

    /*
        Method to find the resolved value for a subject-predicate combination from multiple models
                list all objects: If all are the same -> return
                if not resolveConflict
    */

    public static RDFNode getResolvedObjectValue(List<Model> models, Resource subject, Property predicate) {
        Set<RDFNode> allObjects = getAllObjects(models, subject, predicate);

        RDFNode firstObject = allObjects.iterator().next();
        for (RDFNode object : allObjects) {
            if (!firstObject.equals(object)) {
                // Not all objects are the same, return the first object as a simple conflict resolution
                /*
                        TODO:
                        Resolve from set of objects for subject/predicate to just one object
                        RDFNode resolved Object = ...Method(allObjects, subject, predicate)

                */

               fusionStrategy fusionStrategy = new fusionStrategy();

               //I am returning a set of RDF node instead of one because some subject and predicate may have a list of object
               // like movies or children. 
               Set<RDFNode> resolveNodes = fusionStrategy.fusion(allObjects, subject, predicate);


                System.out.println("Conflict: " + subject + predicate);
                for (RDFNode object1 : allObjects) {
                    System.out.println(object1.toString());
                }
                return firstObject;
            }
        }
        return firstObject;

    }

    /*
        Get a Model which contains all distinct statements from all the models with a given subject and predicate
     */
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


    /*
        Get a set of all objects in a list of models which appear in a property with a given subject and predicate
     */
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


        /*
                Method to find the resolved value for a subject-predicate combination from multiple models
                for loop: go through statements with given subject-predicate combination
                    -> if it is the first, assigned it to commonObject
                    -> if not check for every statement if it is the same until we find one that isn't
                        -> call method to resolve for one object and exit loop

    public static RDFNode getResolvedObjectValue(List<Model> models, Resource subject, Property predicate) {
        RDFNode commonObject = null;
        boolean conflict = false;

        for (Model model : models) {
            StmtIterator iter = model.listStatements(subject, predicate, (RDFNode) null);
            if (iter.hasNext()) {
                RDFNode object = iter.nextStatement().getObject();
                if (commonObject == null) {
                    commonObject = object;
                } else if (!commonObject.equals(object)) {
                    conflict = true;
                    Set<RDFNode> allObjects = getAllObjects(models, subject, predicate);
                    /*
                        TODO:
                        Resolve from set of objects for subject/predicate to just one object
                        RDFNode resolved Object = ...Method(allObjects, subject, predicate)



                    System.out.println("Conflict: " + subject + predicate);
                    for (RDFNode object1 : allObjects) {
                        System.out.println(object1.toString());
                    }
                }
            }
        }
        return commonObject;
        //at the moment we just return the first entry (correct if we dont have a conflict)
        //when we implement solution we need tio return this or return directly in loop
        //return conflict ? resolvedObject : commonObject;
    }
    */

}