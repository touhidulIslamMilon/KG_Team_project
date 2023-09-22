package FinalPackage.Merging.Strategies;

import com.google.common.collect.ListMultimap;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import java.util.Map;

public class PriorityBasedResolutionStrategy implements Strategy {
    @Override
    public RDFNode resolveConflict(ListMultimap<RDFNode, Integer> objects, Resource subject, Property predicate) {
        // Determine the highest priority
        int highestPriority = Integer.MIN_VALUE;

        for (Integer priority : objects.values()) {
            if (priority > highestPriority) {
                highestPriority = priority;
            }
        }

        RDFNode result = null;
        boolean manualReviewRequired = false;

        for (Map.Entry<RDFNode, Integer> entry : objects.entries()) {
            RDFNode key = entry.getKey();
            int priority = entry.getValue();

            if (priority == highestPriority) {
                // If the priority is the same as the highest priority, add to manual review list
                manualReviewRequired = true;
            }

            if (result == null || priority > highestPriority) {
                result = key;
                highestPriority = priority;
                manualReviewRequired = false; // Reset manual review flag if a new highest priority is found
            }
        }

        // If manual review is required for objects with the same highest priority, call it
        if (manualReviewRequired) {
            ManualReviewResolutionStrategy manualReviewStrategy = new ManualReviewResolutionStrategy();
            result = manualReviewStrategy.resolveConflict(objects, subject, predicate);
        }

        return result;
    }
}
