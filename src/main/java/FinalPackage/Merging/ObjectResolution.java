package FinalPackage.Merging;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.apache.jena.rdf.model.*;

import java.util.*;

public class ObjectResolution {

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
                ListMultimap<RDFNode, Integer> objectsWithPrio = createObjectMap(allObjects);
                RDFNode resolvedObject = ConflictResolution.resolveConflict(objectsWithPrio, subject, predicate);

                System.out.println("Conflict: " + subject + predicate + resolvedObject);
                return resolvedObject;
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

    public static ListMultimap<RDFNode, Integer> createObjectMap(List<RDFNode> allObjects) {
        ListMultimap<RDFNode, Integer> objectMap = ArrayListMultimap.create();

        for (RDFNode object : allObjects) {
            objectMap.put(object, 1);
        }

        return objectMap;
    }


    public static RDFNode getResolvedObjectValue(Map<Model, Integer> modelPriorities, Resource subject, Property predicate) {
        ListMultimap<RDFNode, Integer> objectPriorityMap = ArrayListMultimap.create();

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
            RDFNode resolvedObject = ConflictResolution.resolveConflict(objectPriorityMap, subject, predicate);

            System.out.println("Conflict: " + subject + predicate + resolvedObject);
            return resolvedObject;
        }

        // If no conflicts or after resolving conflicts, return the first object
        return objectPriorityMap.keys().iterator().next();
    }


    public static boolean hasConflicts(ListMultimap<RDFNode, Integer> multimap) {

        Set<RDFNode> uniqueObjects = new HashSet<>(multimap.keySet());

        return uniqueObjects.size() > 1; // Return true if there is more than one distinct object
    }
}


//Old
/*public static boolean hasConflicts(Map<RDFNode, Integer> objectPriorityMap) {
        // Check if there are conflicts (more than one object with different priorities)
        Set<Integer> prioritySet = new HashSet<>(objectPriorityMap.values());
        return prioritySet.size() > 1;
    }*/
