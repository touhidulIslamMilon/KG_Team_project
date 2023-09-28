package FinalPackage.analyzeGraph;

import FinalPackage.Merging.ConflictResolution;
import FinalPackage.Merging.FunctionalPropertyFinder;
import FinalPackage.Merging.ObjectResolution;
import com.google.common.collect.ListMultimap;
import lombok.Getter;
import org.apache.jena.rdf.model.*;

import java.util.*;

@Getter
public class ConflictMap {

    private final Map<SubjectPredicatePair, List<RDFNode>> conflicts;


    public ConflictMap(){
        this.conflicts = new HashMap<>();
    }

    public void findConflicts (List<Model> models) {
        Model targetModel = ModelFactory.createDefaultModel();
        Model resolvedModel = ModelFactory.createDefaultModel();

        List<Property> functionalProperties = FunctionalPropertyFinder.findFunctionalProperties(models);
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
                    } else {
                        targetModel.removeAll(subject, predicate, null);
                        if (functionalProperties.contains(predicate)) {
                            addConflictingObjects(models, subject, predicate);
                            targetModel.add(stmt);
                            resolvedModel.add(stmt);
                        } else {
                            Model allStatements = ObjectResolution.getDistinctStatements(models, subject, predicate);
                            targetModel.add(allStatements);
                            resolvedModel.add(stmt);

                        }
                    }

                }
            }
        }
    }

    public void addConflictingObjects(List<Model> models, Resource subject, Property predicate) {
        List<RDFNode> allObjects = ObjectResolution.getAllObjects(models, subject, predicate);

        RDFNode firstObject = allObjects.iterator().next();
        for (RDFNode object : allObjects) {
            if (!firstObject.equals(object)) {
                SubjectPredicatePair pair = new SubjectPredicatePair(subject, predicate);
                conflicts.put(pair, allObjects);
            }
        }
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
            return Objects.equals(subject, other.getSubject()) && Objects.equals(predicate, other.getPredicate());
        }
    }

}
