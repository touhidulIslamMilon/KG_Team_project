package FinalPackage.Merging.Strategies.Excluded;

import java.util.Date;
import java.util.Map;

import com.google.common.collect.ListMultimap;
import FinalPackage.Merging.Strategies.Strategy;
import com.google.common.collect.ListMultimap;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import com.google.common.collect.ListMultimap;

public class MostRecentResolusionStrategy implements Strategy{

    @Override
    public RDFNode resolveConflict(ListMultimap<RDFNode, Integer> objects, Resource subject, Property predicate) {
        RDFNode mostRecentNode = null;
        Date mostRecentDate = null;
        Map<RDFNode, Date> nodeCreationMap;
        try {
            nodeCreationMap = convertMapToDates(objects);
        } catch (Exception e) {
                
                //if conversion to date fails, use manual review
                ManualReviewResolutionStrategy manualReview = new ManualReviewResolutionStrategy();
                return manualReview.resolveConflict(objects, subject, predicate);
        }

        for (Map.Entry<RDFNode, Date> entry : nodeCreationMap.entrySet()) {
            Date currentDate = entry.getValue();

            if (mostRecentDate == null || currentDate.after(mostRecentDate)) {
                mostRecentDate = currentDate;
                mostRecentNode = entry.getKey();
            }
        }

        return mostRecentNode;
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
