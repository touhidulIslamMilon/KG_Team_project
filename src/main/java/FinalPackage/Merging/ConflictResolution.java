package FinalPackage.Merging;

import org.apache.jena.rdf.model.*;

import java.util.*;

public class ConflictResolution {

    /*
        Method to find the resolved value for a subject-predicate combination from multiple models
                list all objects: If all are the same -> return
                if not resolveConflict
    */

    public static RDFNode getResolvedObjectValue(List<Model> models, Resource subject, Property predicate) {
        List<RDFNode> allObjects = getAllObjects(models, subject, predicate);

        RDFNode firstObject = allObjects.iterator().next();
        for (RDFNode object : allObjects) {
            if (!firstObject.equals(object)) {
                createObjectMap(allObjects);
                // Not all objects are the same, return the first object as a simple conflict resolution
                /*
                        TODO:
                        Resolve from set of objects for subject/predicate to just one object
                        RDFNode resolved Object = ...Method(allObjects, subject, predicate)

                        fusionStrategy fusionStrategy = new fusionStrategy();

               //I am returning a set of RDF node instead of one because some subject and predicate may have a list of object
               // like movies or children.
               //Set<RDFNode> resolveNodes = fusionStrategy.fusion(allObjects, subject, predicate);

                */

                System.out.println("Conflict: " + subject + predicate);
                for (RDFNode object1 : allObjects) {
                    System.out.println(object1.toString());
                }
                return firstObject;
            }
        }
        System.out.println("No Conflict: " + subject + predicate + firstObject);
        // All objects are the same, return that object
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
    public static List<RDFNode> getAllObjects(List<Model> models, Resource subject, Property predicate) {
        List<RDFNode> objects = new ArrayList<>();

        for (Model model : models) {
            StmtIterator iter = model.listStatements(subject, predicate, (RDFNode) null);
            while (iter.hasNext()) {
                Statement stmt = iter.nextStatement();
                objects.add(stmt.getObject());
            }
        }

        return objects;
    }

    public static Map<RDFNode, Integer> createObjectMap(List<RDFNode> allObjects) {
        Map<RDFNode, Integer> objectMap = new HashMap<>();

        for (RDFNode object : allObjects) {
            objectMap.put(object, 1);
        }

        return objectMap;
    }


    public static RDFNode getResolvedObjectValue(Map<Model, Integer> modelPriorities, Resource subject, Property predicate) {
        Map<RDFNode, Integer> objectPriorityMap = new HashMap<>();

        // Collect objects and their associated priorities
        for (Map.Entry<Model, Integer> entry : modelPriorities.entrySet()) {
            Model model = entry.getKey();
            int priority = entry.getValue();
            if (model.contains(subject, predicate)) {
                StmtIterator stmtIterator = model.listStatements(subject, predicate, (RDFNode) null);
                while (stmtIterator.hasNext()) {
                    Statement statement = stmtIterator.nextStatement();
                    RDFNode object = statement.getObject();
                    objectPriorityMap.put(object, priority);
                }
            }
        }

        // Check for conflicts
        if (!objectPriorityMap.isEmpty() && hasConflicts(objectPriorityMap)) {
            // Call the fuse method with objects and their priorities
            /*
                TODO: fuse(objectPriorityMap);
             */
            System.out.println("Conflict: " + subject + predicate);
            for (Map.Entry<RDFNode, Integer> entry : objectPriorityMap.entrySet()) {
                RDFNode object = entry.getKey();
                System.out.println(object.toString());
            }
            return objectPriorityMap.keySet().iterator().next();
        }

        // If no conflicts or after resolving conflicts, return the first object
        return objectPriorityMap.keySet().iterator().next();
    }


    public static boolean hasConflicts(Map<RDFNode, Integer> objectPriorityMap) {
        // Check if there are conflicts (more than one object with different priorities)
        Set<Integer> prioritySet = new HashSet<>(objectPriorityMap.values());
        return prioritySet.size() > 1;
    }
}
