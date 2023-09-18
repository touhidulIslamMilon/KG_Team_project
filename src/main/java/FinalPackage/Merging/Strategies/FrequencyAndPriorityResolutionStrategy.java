package FinalPackage.Merging.Strategies;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FrequencyAndPriorityBaseStrategy implements Strategy {
    @Override
    public RDFNode resolveConflict(Map<RDFNode, Integer> objects, Resource subject, Property predicate) {
        
        Map<RDFNode, Double> weightedCounts = new HashMap<>();

        // Calculate the weighted count for each RDF node
        for (Map.Entry<RDFNode, Integer> entry : objects.entrySet()) {
            RDFNode node = entry.getKey();
            double weight = entry.getValue();
            weightedCounts.put(node, weight * countNodeFrequency(node, objects.keySet()));
        }

        // Find the node with the highest weighted count
        RDFNode highestWeightedNode = null;
        double highestWeight = Double.NEGATIVE_INFINITY;

        for (Map.Entry<RDFNode, Double> entry : weightedCounts.entrySet()) {
            RDFNode node = entry.getKey();
            double weightedCount = entry.getValue();
            if (weightedCount > highestWeight) {
                highestWeight = weightedCount;
                highestWeightedNode = node;
            }
        }

        return highestWeightedNode;

    }
    public static int countNodeFrequency(RDFNode node, Set<RDFNode> nodeSet) {
        int frequency = 0;
        for (RDFNode n : nodeSet) {
            if (n.equals(node)) {
                frequency++;
            }
        }
        return frequency;
    }
}
