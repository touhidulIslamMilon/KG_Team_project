package FinalPackage.Merging.Strategies;

import java.util.Date;
import java.util.Map;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

public class MostRecentResolutionStrategy implements Strategy{

    @Override
    public RDFNode resolveConflict(Map<RDFNode, Integer> objects, Resource subject, Property predicate) {
        RDFNode mostRecentNode = null;
        Date mostRecentDate = null;
        Map<RDFNode, Date> nodeCreationMap = convertMapToDates(objects);

        for (Map.Entry<RDFNode, Date> entry : nodeCreationMap.entrySet()) {
            Date currentDate = entry.getValue();

            if (mostRecentDate == null || currentDate.after(mostRecentDate)) {
                mostRecentDate = currentDate;
                mostRecentNode = entry.getKey();
            }
        }

        return mostRecentNode;
    }

    public static Map<RDFNode, Date> convertMapToDates(Map<RDFNode, Integer> objects) {
        Map<RDFNode, Date> nodeCreationMap = new java.util.HashMap<>();

        for (Map.Entry<RDFNode, Integer> entry : objects.entrySet()) {
            RDFNode node = entry.getKey();
            int creationTime = entry.getValue();
            Date date = new Date(creationTime);
            nodeCreationMap.put(node, date);
        }

        return nodeCreationMap;
    }
    
}
