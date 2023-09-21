package tests.strategiesTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import com.google.common.collect.ListMultimap;

import FinalPackage.Merging.Strategies.ManualReviewResolutionStrategy;

public class StrategyText {

    public RDFNode resolveConflict(ListMultimap<RDFNode, Integer> objects, Resource subject, Property predicate) {
        Map<RDFNode, Integer> frequencyCount = new HashMap<>();

        // Calculate the weighted count for each RDF node
        for(RDFNode key : objects.keySet()){
            System.out.println(key + " " + objects.get(key).size());
            frequencyCount.put(key,objects.get(key).size());
        }

        // Find the node with the highest weighted count
        RDFNode highestWeightedNode = null;
        double highestWeight = Double.NEGATIVE_INFINITY;

        for (Map.Entry<RDFNode, Integer> entry : frequencyCount.entrySet()) {
            RDFNode node = entry.getKey();
            double weightedCount = entry.getValue();
            if (weightedCount > highestWeight) {
                highestWeight = weightedCount;
                highestWeightedNode = node;
            }
        }

        return highestWeightedNode;
    }
    
}
