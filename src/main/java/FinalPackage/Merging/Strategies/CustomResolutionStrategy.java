package FinalPackage.Merging.Strategies;

import org.apache.jena.rdf.model.StmtIterator;

import java.util.HashMap;
import java.util.Map;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.apache.jena.rdf.model.impl.ResourceImpl;
import org.apache.jena.rdf.model.impl.StatementImpl;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.apache.jena.rdf.model.impl.LiteralImpl;

public class CustomResolutionStrategy implements Strategy{
    static Map<Property, ResolutionStrategy> strategyMap = new HashMap<>();
    

    @Override
    public RDFNode resolveConflict(Map<RDFNode, Integer> objects, Resource subject, Property predicate) {

        strategyMap.put(new PropertyImpl("http://www.w3.org/2000/01/rdf-schema#label"),ResolutionStrategy.MAX_VALUE);
        strategyMap.put(new PropertyImpl("http://www.w3.org/2000/01/rdf-schema#comment"),ResolutionStrategy.MAX_VALUE);


        RDFNode resolvedObject = null;
        ResolutionStrategy strategy = strategyMap.get(predicate);
        if (strategy == ResolutionStrategy.MAX_VALUE) {
            resolvedObject = getMaxValueForProperty(objects);

        } else if (strategy == ResolutionStrategy.MIN_VALUE) {
            resolvedObject= getMinValueForProperty(objects);
        } else if(strategy == ResolutionStrategy.LONG_VALUE){
            resolvedObject = getLongestValueForPropertyy(objects);
        } if(strategy == ResolutionStrategy.SHORT_VALUE){
            resolvedObject = getShortestValueForPropertyy(objects);
        }
        return resolvedObject;
    }
 
    //Type of resolution strategies
    enum ResolutionStrategy {
        MAX_VALUE,
        MIN_VALUE,
        LONG_VALUE,
        SHORT_VALUE
        // Add more strategies here
    }

    // Define other custom resolution strategies as needed

    private RDFNode getLongestValueForPropertyy(Map<RDFNode, Integer> objects) {

        if (objects == null || objects.isEmpty()) {
            throw new IllegalArgumentException("Input map is null or empty");
        }

        RDFNode longestNode = null;
        int longestString = Integer.MIN_VALUE;

        for (Map.Entry<RDFNode, Integer> entry : objects.entrySet()) {
            RDFNode node = entry.getKey();

            if (node.toString().length() > longestString) {
                longestNode = node;
            } 
        }

        return longestNode;
    }
    private RDFNode getShortestValueForPropertyy(Map<RDFNode, Integer> objects) {

        if (objects == null || objects.isEmpty()) {
            throw new IllegalArgumentException("Input map is null or empty");
        }

        RDFNode longestNode = null;
        int longestString = Integer.MIN_VALUE;

        for (Map.Entry<RDFNode, Integer> entry : objects.entrySet()) {
            RDFNode node = entry.getKey();

            if (node.toString().length() > longestString) {
                longestNode = node;
            } 
        }

        return longestNode;
    }
    private static RDFNode getMinValueForProperty(Map<RDFNode, Integer> values) {
        //This Function the node that have the minimum value for the RDFNode
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("Input map is null or empty");
        }

        RDFNode minNode = null;
        int minValue = Integer.MIN_VALUE;

        for (Map.Entry<RDFNode, Integer> entry : values.entrySet()) {
            RDFNode node = entry.getKey();
            int value = entry.getValue();

            if (value > minValue) {
                minValue = value;
                minNode = node;
            }
        }

        return minNode;
        
    }

    private static RDFNode getMaxValueForProperty(Map<RDFNode, Integer> values) {
        //This Function the node that have the minimum value for the RDFNode
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("Input map is null or empty");
        }

        RDFNode maxNode = null;
        int maxValue = Integer.MIN_VALUE;

        for (Map.Entry<RDFNode, Integer> entry : values.entrySet()) {
            RDFNode node = entry.getKey();
            int value = entry.getValue();

            if (value > maxValue) {
                maxValue = value;
                maxNode = node;
            }
        }

        return maxNode;
    }

  
    
}
