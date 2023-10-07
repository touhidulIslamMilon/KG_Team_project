package FinalPackage.Merging.Strategies;

import java.util.HashMap;
import java.util.Map;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import com.google.common.collect.ListMultimap;

public class MostFrequentResolutionStrategy implements Strategy {
    @Override
    public RDFNode resolveConflict(ListMultimap<RDFNode, Integer> objects, Resource subject, Property predicate) {
        Map<RDFNode, Integer> frequencyCount = new HashMap<>();

        // Calculate the weighted count for each RDF node
        for(RDFNode key : objects.keySet()){
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
            }else if(weightedCount == highestWeight){
                // If the priority is the same, then we will use the manual review strategy
                // to resolve the conflict
                // ManualReviewResolutionStrategy manualReviewStrategy = new ManualReviewResolutionStrategy();
                // highestWeightedNode = manualReviewStrategy.resolveConflict(objects, subject, predicate);
                DefaultStratigy defaultStratigy = new DefaultStratigy();
                return defaultStratigy.resolveConflict(objects, subject, predicate);
            }
        }

        return highestWeightedNode;
    }
   
}