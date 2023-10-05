package FinalPackage.analyzeGraph;
import kotlin.Pair;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import java.util.Map;
import org.apache.jena.rdf.model.Resource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SophisticatedAnalysis {
    public static Map<RDFNode, Integer> calculateNodeCentrality(Model model) {
        // Initialize a map to hold the nodes and their centrality scores
        Map<RDFNode, Integer> centralityMap = new HashMap<>();

        // Use a StmtIterator to go through all statements in the model
        StmtIterator stmtIterator = model.listStatements();

        // For each statement, update the centrality score for both the subject and object nodes
        while (stmtIterator.hasNext()) {
            Statement stmt = stmtIterator.nextStatement();

            // Update subject
            RDFNode subject = stmt.getSubject();
            centralityMap.put(subject, centralityMap.getOrDefault(subject, 0) + 1);

            // Update object
            RDFNode object = stmt.getObject();
            centralityMap.put(object, centralityMap.getOrDefault(object, 0) + 1);
        }

        return centralityMap.entrySet()
                .stream()
                .sorted(Map.Entry.<RDFNode, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    // get types of graph and display the most frequent ones


    public static Map<RDFNode, Integer> countTypesForFrequency(Model model) {
        Map<RDFNode, Integer> typeCount = new HashMap<>();
        StmtIterator stmtIterator = model.listStatements(null, RDF.type, (RDFNode) null);

        while (stmtIterator.hasNext()) {
            Statement stmt = stmtIterator.nextStatement();
            RDFNode type = stmt.getObject();
            typeCount.put(type, typeCount.getOrDefault(type, 0) + 1);
        }
        return typeCount;
    }



    public static void showTopTypesAndObjects(Model model, Map<RDFNode, Integer> typeCount, int maxTypes, int maxObjects) {
        List<Map.Entry<RDFNode, Integer>> sortedTypes = typeCount.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        for (int i = 0; i < Math.min(maxTypes, sortedTypes.size()); i++) {
            Map.Entry<RDFNode, Integer> typeEntry = sortedTypes.get(i);
            System.out.println("Type: " + typeEntry.getKey() + ", Frequency: " + typeEntry.getValue());

            Set<RDFNode> objectSet = new HashSet<>();
            StmtIterator stmtIterator = model.listStatements(null, RDF.type, typeEntry.getKey());

            int objectCount = 0;
            while (stmtIterator.hasNext() && objectCount < maxObjects) {
                RDFNode object = stmtIterator.nextStatement().getSubject();
                if (objectSet.add(object)) {
                    System.out.println("  Sample Object: " + object);
                    objectCount++;
                }
            }
        }
    }

    // show predicates and their most frequent objects
    public static void showPredicatesAndObjects2(Model model) {
        Map<Property, Integer> predicateFrequencies = new HashMap<>();
        Map<Property, Map<RDFNode, Integer>> objectFrequencies = new HashMap<>();

        StmtIterator statements = model.listStatements();
        while (statements.hasNext()) {
            Statement statement = statements.nextStatement();
            Property predicate = statement.getPredicate();
            RDFNode object = statement.getObject();

            // Update predicate frequency
            predicateFrequencies.put(predicate, predicateFrequencies.getOrDefault(predicate, 0) + 1);

            // Update object frequency for this predicate
            objectFrequencies.putIfAbsent(predicate, new HashMap<>());
            Map<RDFNode, Integer> freqMap = objectFrequencies.get(predicate);
            freqMap.put(object, freqMap.getOrDefault(object, 0) + 1);
        }

        // Sort predicates by frequency in descending order and pick the top 15
        List<Map.Entry<Property, Integer>> sortedPredicates = new ArrayList<>(predicateFrequencies.entrySet());
        sortedPredicates.sort(Map.Entry.<Property, Integer>comparingByValue().reversed());

        int limitPredicates = Math.min(15, sortedPredicates.size());
        for (int i = 0; i < limitPredicates; i++) {
            Property predicate = sortedPredicates.get(i).getKey();
            int frequency = sortedPredicates.get(i).getValue();

            // Print predicate and frequency
            System.out.println("Predicate: " + predicate + ", Frequency: " + frequency);

            // Sort objects by frequency for this predicate, in descending order
            List<Map.Entry<RDFNode, Integer>> sortedObjects = new ArrayList<>(objectFrequencies.get(predicate).entrySet());
            sortedObjects.sort(Map.Entry.<RDFNode, Integer>comparingByValue().reversed());

            // Print up to 10 sample objects
            int limitObjects = Math.min(10, sortedObjects.size());
            for (int j = 0; j < limitObjects; j++) {
                System.out.println("  Sample Object: " + sortedObjects.get(j).getKey());
            }
        }
    }
}
