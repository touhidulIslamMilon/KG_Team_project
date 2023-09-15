package FinalPackage.Merging.Strategies;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import java.util.Map;

public class MostFrequentResolutionStrategy implements Strategy {
    @Override
    public RDFNode resolveConflict(Map<RDFNode, Integer> objects, Resource subject, Property predicate) {
        RDFNode result = null;
        int highestFrequency = 0;

        for (Map.Entry<RDFNode, Integer> entry : objects.entrySet()) {
            RDFNode object = entry.getKey();
            int frequency = entry.getValue();

            if (frequency > highestFrequency) {
                result = object;
                highestFrequency = frequency;
            }
        }

        return result;
    }
}