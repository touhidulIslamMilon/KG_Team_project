package FinalPackage.Merging.Strategies;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import java.util.Map;

interface Strategy {
    RDFNode resolveConflict(Map<RDFNode, Integer> objects, Resource subject, Property predicate);
}
