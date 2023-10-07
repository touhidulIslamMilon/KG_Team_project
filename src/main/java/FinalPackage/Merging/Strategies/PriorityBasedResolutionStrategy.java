package FinalPackage.Merging.Strategies;

import com.google.common.collect.ArrayListMultimap;
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

        ListMultimap<RDFNode, Integer> highestPriorityObjects = ArrayListMultimap.create();

        for (Map.Entry<RDFNode, Integer> entry : objects.entries()) {
            RDFNode key = entry.getKey();
            int priority = entry.getValue();

            if (priority == highestPriority) {
                highestPriorityObjects.put(key, priority);
            }
        }

        // If there is only one highest-priority object, return it
        if (highestPriorityObjects.size() == 1) {
            return highestPriorityObjects.entries().iterator().next().getKey();
        } else {
            // If there are multiple highest-priority objects, call other strategies
            // You can choose one of the following approaches:

            // Approach 1: Randomly select one of the highest-priority objects
            DefaultStratigy defaultStratigy = new DefaultStratigy();
            return defaultStratigy.resolveConflict(objects, subject, predicate);

            // Approach 2: Use a manual review strategy
            // ManualReviewResolutionStrategy manualReviewStrategy = new ManualReviewResolutionStrategy();
            // return manualReviewStrategy.resolveConflict(highestPriorityObjects, subject, predicate);
        }
    }
}
