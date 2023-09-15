package FinalPackage.Merging;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import java.util.Map;

public class Strategies {

    private Strategy strategy;

    public Strategies(Strategy strategy) {
        this.strategy = strategy;
    }

    public RDFNode mergeObjects(Map<RDFNode, Integer> objects, Resource subject, Property predicate) {
        return strategy.resolveConflict(objects, subject, predicate);
    }
}

interface Strategy {
    RDFNode resolveConflict(Map<RDFNode, Integer> objects, Resource subject, Property predicate);
}

class PriorityBasedResolutionStrategy implements Strategy {
    @Override
    public RDFNode resolveConflict(Map<RDFNode, Integer> objects, Resource subject, Property predicate) {
        // Implement conflict resolution logic based on priority
        // You can iterate through the map and choose the object with the highest priority
        RDFNode result = null;
        int highestPriority = Integer.MIN_VALUE;

        for (Map.Entry<RDFNode, Integer> entry : objects.entrySet()) {
            RDFNode object = entry.getKey();
            int priority = entry.getValue();

            if (priority > highestPriority) {
                result = object;
                highestPriority = priority;
            }
        }

        return result;
    }
}

class CustomResolutionStrategy implements Strategy {
    @Override
    public RDFNode resolveConflict(Map<RDFNode, Integer> objects, Resource subject, Property predicate) {
        // Implement your custom conflict resolution logic here
        // You can access the map of objects, subject, and predicate to make your decision
        // Return the merged RDFNode based on your custom logic
        // Example:
        // if (objects.size() > 0) {
        //     return objects.entrySet().iterator().next().getKey();
        // }
        // return null;

        // Replace the example logic above with your own implementation.
        return null;
    }

}
