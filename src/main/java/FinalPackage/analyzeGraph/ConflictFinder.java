package FinalPackage.analyzeGraph;


import FinalPackage.Merging.FunctionalPropertyFinder;
import lombok.Getter;
import org.apache.jena.rdf.model.*;

import java.util.*;

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
            // Clear the model to release memory
            model.close();
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
}
