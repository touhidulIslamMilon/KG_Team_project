package FinalPackage.Merging;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import java.util.*;

public class FunctionalPropertyFinder {
    // Define the threshold percentage (e.g., 90%)
    private static final double THRESHOLD_PERCENTAGE = 0.1;

    public static List<Property> findFunctionalProperties(List<Model> models) {
        List<Property> functionalProperties = new ArrayList<>();

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
                    if ((1 - percentage) <= THRESHOLD_PERCENTAGE) {
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
            if ((1 - percentage) <= THRESHOLD_PERCENTAGE) {
                functionalProperties.add(entry.getKey());
            }
        }

        return functionalProperties;
    }

    private static boolean isFunctionalInModel(Property predicate, Model model) {
        // SPARQL query to check if there is only one object associated with each subject-predicate combination
        String queryString = "ASK WHERE { ?subject <" + predicate.getURI() + "> ?object1 . ?subject <" + predicate.getURI() + "> ?object2 . FILTER (?object1 != ?object2) }";

        try (QueryExecution queryExecution = QueryExecutionFactory.create(queryString, model)) {
            return !queryExecution.execAsk();
        }
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
}
