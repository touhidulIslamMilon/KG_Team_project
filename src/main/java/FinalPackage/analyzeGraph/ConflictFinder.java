package FinalPackage.analyzeGraph;


import FinalPackage.Merging.FunctionalPropertyFinder;
import lombok.Getter;
import org.apache.jena.rdf.model.*;

import java.io.File;
import java.util.*;

import static FinalPackage.Merging.Strategies.HelperFunction.detectStringType;
import static FinalPackage.Merging.Strategies.HelperFunction.objectType1;

public class ConflictFinder {

    public static Map<SubjectPredicatePair, List<RDFNode>> findConflictingObjects(List<Model> models) {
        Map<SubjectPredicatePair, List<RDFNode>> conflictMap = new HashMap<>();

        Set<Property> functionalProperties = new HashSet<>(FunctionalPropertyFinder.findFunctionalProperties(models));

        for (Model model : models) {
            StmtIterator stmtIterator = model.listStatements();

            while (stmtIterator.hasNext()) {
                Statement statement = stmtIterator.nextStatement();
                SubjectPredicatePair pair = new SubjectPredicatePair(statement.getSubject(), statement.getPredicate());
                RDFNode object = statement.getObject();

                if(functionalProperties.contains(statement.getPredicate())) {
                    // Check if the pair is already in the conflictMap
                    if (conflictMap.containsKey(pair)) {
                        List<RDFNode> objects = conflictMap.get(pair);

                        // Check if the object is not already in the list of conflicting objects
                        if (!objects.contains(object)) {
                            objects.add(object);
                        }
                    } else {
                        // If the pair is not in the map, add it with a new list containing the object
                        List<RDFNode> objects = new ArrayList<>();
                        objects.add(object);
                        conflictMap.put(pair, objects);
                    }
                }

            }
        }

        // Remove pairs with only one object (no conflict)
        conflictMap.entrySet().removeIf(entry -> entry.getValue().size() <= 1);

        return conflictMap;
    }

    @Getter
    public static class SubjectPredicatePair {
        private final Resource subject;
        private final Property predicate;

        public SubjectPredicatePair(Resource subject, Property predicate) {
            this.subject = subject;
            this.predicate = predicate;
        }

        @Override
        public int hashCode() {
            return Objects.hash(subject, predicate);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            SubjectPredicatePair other = (SubjectPredicatePair) obj;
            return Objects.equals(subject, other.subject) && Objects.equals(predicate, other.predicate);
        }
    }

    public static void identifyAndPrintConflicts(List<Model> models) {
        Map<SubjectPredicatePair, List<RDFNode>> conflicts = findConflictingObjects(models);
        Map<String, Integer> conflictCountMap = new HashMap<>();

        for (Map.Entry<SubjectPredicatePair, List<RDFNode>> entry : conflicts.entrySet()) {
            SubjectPredicatePair pair = entry.getKey();
            List<RDFNode> conflictingObjects = entry.getValue();

            System.out.println("Conflict Detected:");
            // Assuming we have a way to find the model name
            // System.out.println("Model: " + findModelName(pair)); // Placeholder, replace with actual implementation
            System.out.println("Subject: " + pair.getSubject());
            System.out.println("Predicate: " + pair.getPredicate());

            for (RDFNode object : conflictingObjects) {
                String conflictType = objectType1(object);

                // Refining the conflictType for strings
                if ("string".equals(conflictType) && object.isLiteral()) {
                    Literal literal = object.asLiteral();
                    conflictType = detectStringType(literal.getString());
                }

                System.out.println("Conflict Type: " + conflictType);
                System.out.println("Conflicting Object: " + object);

                conflictCountMap.put(conflictType, conflictCountMap.getOrDefault(conflictType, 0) + 1);
            }
            System.out.println("------------------------------");
        }

        // Printing the occurrence of each type of conflict
        System.out.println("Conflict Types Count:");
        for (Map.Entry<String, Integer> entry : conflictCountMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }


    /*
    public static void identifyAndPrintConflicts(List<Model> models) {
        Map<SubjectPredicatePair, List<RDFNode>> conflicts = findConflictingObjects(models);
        Map<String, Integer> conflictCountMap = new HashMap<>();

        for (Map.Entry<SubjectPredicatePair, List<RDFNode>> entry : conflicts.entrySet()) {
            SubjectPredicatePair pair = entry.getKey();
            List<RDFNode> conflictingObjects = entry.getValue();

            for (RDFNode object : conflictingObjects) {
                String conflictType = objectType1(object);

                // Refining the conflictType for strings
                if("string".equals(conflictType) && object.isLiteral()) {
                    Literal literal = object.asLiteral();
                    conflictType = detectStringType(literal.getString());
                }

                System.out.println("Conflict Detected:");
                //System.out.println("Model: " + SubjectPredicatePair.getModelNameFromPath()); // Assuming SubjectPredicatePair has getModelName method
                System.out.println("Subject: " + pair.getSubject());
                System.out.println("Predicate: " + pair.getPredicate());
                System.out.println("Conflict Type: " + conflictType);
                System.out.println("Conflicting Object: " + object);
                System.out.println("------------------------------");

                conflictCountMap.put(conflictType, conflictCountMap.getOrDefault(conflictType, 0) + 1);
            }
        }

        // Printing the occurrence of each type of conflict
        System.out.println("Conflict Types Count:");
        for (Map.Entry<String, Integer> entry : conflictCountMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    */




    /*
    public static void printConflictingObjects(Map<SubjectPredicatePair, List<RDFNode>> conflictMap, List<Model> models) {
        Map<String, Integer> conflictTypeCounts = new HashMap<>();

        for (Map.Entry<SubjectPredicatePair, List<RDFNode>> entry : conflictMap.entrySet()) {
            SubjectPredicatePair pair = entry.getKey();
            List<RDFNode> conflictingObjects = entry.getValue();

            for (RDFNode object : conflictingObjects) {
                String objectType = objectType1(object);
                conflictTypeCounts.merge(objectType, 1, Integer::sum); // Update the conflict count for this object type

                System.out.println();
                System.out.println("Conflicting Object: " + object);
                System.out.println("Type of Object: " + objectType);

                for (Model model : models) {
                    StmtIterator iterator = model.listStatements(pair.getSubject(), pair.getPredicate(), object);
                    while (iterator.hasNext()) {
                        Statement statement = iterator.nextStatement();
                        System.out.println("Model: " + getModelName(model));
                        System.out.println("Statement: " + statement);
                    }
                }
            }
        }

        printConflictSummary(conflictTypeCounts);
    }

    private static void printConflictSummary(Map<String, Integer> conflictTypeCounts) {
        System.out.println("\n=== Conflict Summary ===");
        for (Map.Entry<String, Integer> entry : conflictTypeCounts.entrySet()) {
            System.out.println("Conflict Type: " + entry.getKey() + " | Count: " + entry.getValue());
        }
    }



     */
}
