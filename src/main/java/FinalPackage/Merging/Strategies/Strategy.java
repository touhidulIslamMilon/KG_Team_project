package FinalPackage.Merging.Strategies;

import com.google.common.collect.ListMultimap;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

public interface Strategy {
    RDFNode resolveConflict(ListMultimap<RDFNode, Integer> objects, Resource subject, Property predicate);
    enum ResolutionStrategy {
        MAX_VALUE,
        MIN_VALUE,
        LONG_VALUE,
        SHORT_VALUE,
        RECENT_DATE,
        OLDEST_DATE,
        MOST_FREQUENT,
        HIGHEST_PRIORITY,
        PRIORITY_AND_FREQUENCY,
        MANUAL_REVIEW,
        MEAN,
        MEDIAN,
        RANDOM,
        DEFAULT
    }
}
