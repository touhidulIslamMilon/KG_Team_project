package FinalPackage.Merging;

import FinalPackage.Merging.Strategies.*;
import com.google.common.collect.ListMultimap;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

public class ConflictResolution {

    /*
        Method to specify Strategy
     */
    public static RDFNode resolveConflict(ListMultimap<RDFNode, Integer> objects, Resource subject, Property predicate){

        Strategies strategy = new Strategies(new CustomResolutionStrategy());
        return strategy.mergeObjects(objects, subject, predicate);
    }

}

