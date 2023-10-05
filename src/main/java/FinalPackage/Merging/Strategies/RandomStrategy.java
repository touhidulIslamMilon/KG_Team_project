package FinalPackage.Merging.Strategies;

import com.google.common.collect.ListMultimap;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class RandomStrategy implements Strategy{

    @Override
    public RDFNode resolveConflict(ListMultimap<RDFNode, Integer> objects, Resource subject, Property predicate) {
        // Get a list of all conflicting RDFNodes
        List<RDFNode> conflictingNodes = new ArrayList<>(objects.keySet());

        if (conflictingNodes.isEmpty()) {
            return null; // No conflicting objects
        }

        // Generate a random index to choose a random object
        Random random = new Random();
        int randomIndex = random.nextInt(conflictingNodes.size());

        // Return the randomly selected RDFNode
        return conflictingNodes.get(randomIndex);
    }
}
