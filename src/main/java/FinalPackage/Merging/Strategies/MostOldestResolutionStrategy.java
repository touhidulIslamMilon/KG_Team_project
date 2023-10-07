package FinalPackage.Merging.Strategies;

import java.util.Date;
import java.util.Map;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import com.google.common.collect.ListMultimap;

public class MostOldestResolutionStrategy  implements Strategy{
    @Override
    public RDFNode resolveConflict(ListMultimap<RDFNode, Integer> objects, Resource subject, Property predicate) {
        RDFNode mostoldestNode = null;
        Date mostOldestDate = null;
        Map<RDFNode, Date> nodeCreationMap;
        try {
            nodeCreationMap = convertMapToDates(objects);
        } catch (Exception e) {

            //if conversion to date fails, use manual review
            // ManualReviewResolutionStrategy manualReview = new ManualReviewResolutionStrategy();
            // return manualReview.resolveConflict(objects, subject, predicate);
            DefaultStratigy defaultStratigy = new DefaultStratigy();
            return defaultStratigy.resolveConflict(objects, subject, predicate);
        }

        for (Map.Entry<RDFNode, Date> entry : nodeCreationMap.entrySet()) {
            Date currentDate = entry.getValue();

            if (mostOldestDate == null || currentDate.before(mostOldestDate)) {
                mostOldestDate = currentDate;
                mostoldestNode = entry.getKey();
            }
        }

        return mostoldestNode;
    }

    public static Map<RDFNode, Date> convertMapToDates(ListMultimap<RDFNode, Integer> objects) throws Exception{
        Map<RDFNode, Date> nodeCreationMap = new java.util.HashMap<>();
        HelperFunction helper = new HelperFunction();

        for (Map.Entry<RDFNode, Integer> entry : objects.entries()) {
            RDFNode node = entry.getKey();
            int creationTime = entry.getValue();
            Date date = helper.intToDate(creationTime);
            nodeCreationMap.put(node, date);
        }

        return nodeCreationMap;
    }
}
