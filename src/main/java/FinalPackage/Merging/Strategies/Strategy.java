package FinalPackage.Merging.Strategies;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import com.google.common.collect.ListMultimap;

import java.util.Map;

interface Strategy {
    RDFNode resolveConflict(ListMultimap<RDFNode, Integer> objects, Resource subject, Property predicate);
}
