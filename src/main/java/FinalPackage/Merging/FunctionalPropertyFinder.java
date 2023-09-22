package FinalPackage.Merging;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import java.util.*;

public class FunctionalPropertyFinder {
    // Define the threshold percentage (e.g., 90%)
    private static final double THRESHOLD_PERCENTAGE = 0.25;

    public static double getThresholdPercentage(){
        return THRESHOLD_PERCENTAGE;
    }

    public static List<Property> findFunctionalProperties(List<Model> models) {
        // Count the occurrences of each predicate in all the combined models
        Map<Property, Integer> predicateCounts = new HashMap<>();

        for (Model model : models) {
            StmtIterator stmtIterator = model.listStatements();
            while (stmtIterator.hasNext()) {
                Statement statement = stmtIterator.next();
                Property predicate = statement.getPredicate();
                predicateCounts.put(predicate, predicateCounts.getOrDefault(predicate, 0) + 1);
            }
        }

        List<Property> functionalProperties = new ArrayList<>();

        for (Property predicate : predicateCounts.keySet()) {
            int totalOccurrences = predicateCounts.get(predicate);
            int totalFunctionalOccurrences = 0;

            // Calculate the total functional occurrences across all models
            for (Model model : models) {
                totalFunctionalOccurrences += countFunctionalOccurrences(predicate, model);
            }
            //System.out.println("Property: " + predicate + " total: " + totalOccurrences + " func: " + totalFunctionalOccurrences);

            double percentage = (double) totalFunctionalOccurrences / totalOccurrences;
            if (percentage >= 1 - THRESHOLD_PERCENTAGE) {
                functionalProperties.add(predicate);
            }
        }

        return functionalProperties;
    }

    private static int countFunctionalOccurrences(Property predicate, Model model) {
        // SPARQL query to count the total number of times the predicate occurs in a functional manner
        String queryString = "SELECT (COUNT(*) AS ?functionalCount) WHERE { ?subject <" + predicate.getURI() + "> ?object1 . FILTER NOT EXISTS { ?subject <" + predicate.getURI() + "> ?object2 . FILTER (?object1 != ?object2) } }";

        try (QueryExecution queryExecution = QueryExecutionFactory.create(queryString, model)) {
            ResultSet resultSet = queryExecution.execSelect();
            if (resultSet.hasNext()) {
                QuerySolution solution = resultSet.next();
                return solution.getLiteral("functionalCount").getInt();
            }
            return 0;
        }
    }

    public static boolean isFunctionalProperty(Model model, Property property) {
        // Iterate over all subjects in the model.
        ResIterator subjects = model.listSubjects();
        while (subjects.hasNext()) {
            Resource subject = subjects.next();

            // For each subject, count the number of distinct objects linked by the property.
            NodeIterator objects = model.listObjectsOfProperty(subject, property);
            int objectCount = 0;
            while (objects.hasNext()) {
                objects.next();
                objectCount++;

                // If a subject is linked to more than one object by the property, it's not functional.
                if (objectCount > 1) {
                    return false;
                }
            }
        }

        // If no subject is linked to more than one object by the property, it is functional.
        return true;
    }


}


//Old!
    /*private static boolean isFunctionalInModel(Property predicate, Model model) {
        // SPARQL query to check if there is only one object associated with each subject-predicate combination
        String queryString = "ASK WHERE { ?subject <" + predicate.getURI() + "> ?object1 . ?subject <" + predicate.getURI() + "> ?object2 . FILTER (?object1 != ?object2) }";

        try (QueryExecution queryExecution = QueryExecutionFactory.create(queryString, model)) {
            return !queryExecution.execAsk();
        }
    }*/



        /*List<Property> functionalProperties = new ArrayList<>();

        // Iterate over each model
        for (Model model : models) {
            // Count the occurrences of each predicate in the model
            Map<Property, Integer> predicateCounts = new HashMap<>();
            StmtIterator stmtIterator = model.listStatements();
            while (stmtIterator.hasNext()) {
                Statement statement = stmtIterator.next();
                Property predicate = statement.getPredicate();
                predicateCounts.put(predicate, predicateCounts.getOrDefault(predicate, 0) + 1);
            }

            // Iterate over all predicates and check if they are functional using SPARQL
            for (Property predicate : predicateCounts.keySet()) {
                int totalOccurrences = predicateCounts.get(predicate);
                if (isFunctionalInModel(predicate, model)) {
                    // Check if the functional occurrences exceed the threshold percentage
                    int functionalOccurrences = countFunctionalOccurrences(predicate, model);
                    double percentage = (double) functionalOccurrences / totalOccurrences;
                    if (percentage >= 1 - THRESHOLD_PERCENTAGE) {
                        functionalProperties.add(predicate);
                    }
                }
            }
        }

        // Filter the functional properties based on the threshold
        Map<Property, Integer> functionalCounts = new HashMap<>();
        for (Property property : functionalProperties) {
            functionalCounts.put(property, functionalCounts.getOrDefault(property, 0) + 1);
        }

        functionalProperties.clear(); // Clear the list and re-add based on the threshold
        for (Map.Entry<Property, Integer> entry : functionalCounts.entrySet()) {
            double percentage = (double) entry.getValue() / models.size();
            if (percentage >= 1 - THRESHOLD_PERCENTAGE) {
                functionalProperties.add(entry.getKey());
            }
        }

        return functionalProperties;*/