package FinalPackage.Merging.Strategies;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import java.util.Map;

public class Strategies {

    private final Strategy strategy;

    public Strategies(Strategy strategy) {
        this.strategy = strategy;
    }

    public RDFNode mergeObjects(Map<RDFNode, Integer> objects, Resource subject, Property predicate) {
        return strategy.resolveConflict(objects, subject, predicate);
    }
}
