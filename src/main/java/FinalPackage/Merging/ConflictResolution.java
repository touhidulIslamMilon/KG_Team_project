package FinalPackage.Merging;

import FinalPackage.Merging.Strategies.PriorityBasedResolutionStrategy;
import FinalPackage.Merging.Strategies.Strategies;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import java.util.Map;

public class ConflictResolution {

    /*
        Method to specify Strategy
     */
    public static RDFNode resolveConflict(Map<RDFNode, Integer> objects, Resource subject, Property predicate){

        Strategies strategy = new Strategies(new PriorityBasedResolutionStrategy());
        return strategy.mergeObjects(objects, subject, predicate);
    }

}

