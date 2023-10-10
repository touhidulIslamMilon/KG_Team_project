package FinalPackage.Merging.Strategies;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import com.google.common.collect.ListMultimap;

public class DefaultStrategy implements Strategy{

    @Override
    public RDFNode resolveConflict(ListMultimap<RDFNode, Integer> objects, Resource subject, Property predicate) {
        RandomStrategy randomStrategy = new RandomStrategy();
        return randomStrategy.resolveConflict(objects, subject, predicate);
    }

   
}
