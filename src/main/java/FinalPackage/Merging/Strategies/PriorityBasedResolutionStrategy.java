package FinalPackage.Merging.Strategies;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import com.google.common.collect.ListMultimap;
import java.util.Map;

public class PriorityBasedResolutionStrategy implements Strategy {
    @Override
    public RDFNode resolveConflict(ListMultimap<RDFNode, Integer> objects, Resource subject, Property predicate) {
        // Implement conflict resolution logic based on priority
        // You can iterate through the map and choose the object with the highest priority
        RDFNode result = null;
        int highestPriority = Integer.MIN_VALUE;

        for (Map.Entry<RDFNode, Integer> entry : objects.entries()) {
            RDFNode object = entry.getKey();
            int priority = entry.getValue();

            if (priority > highestPriority) {
                result = object;
                highestPriority = priority;
            }else if(priority == highestPriority){
                // If the priority is the same, then we will use the manual review strategy
                // to resolve the conflict
                ManualReviewResolutionStrategy manualReviewStrategy = new ManualReviewResolutionStrategy();
                result = manualReviewStrategy.resolveConflict(objects, subject, predicate);
            }
        }

        return result;
    }
}
