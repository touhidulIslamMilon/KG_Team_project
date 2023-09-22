package FinalPackage.Merging.Strategies;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import com.google.common.collect.ListMultimap;
import java.util.Map.Entry;

import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.apache.jena.rdf.model.impl.ResourceImpl;
import org.apache.jena.rdf.model.impl.StatementImpl;
import org.eclipse.rdf4j.model.vocabulary.RDF;

import org.apache.jena.rdf.model.impl.LiteralImpl;

public class CustomResolutionStrategy implements Strategy{

    
    static Map<Property, ResolutionStrategy> strategyMap = new HashMap<>();
    //create constant for the class
    public CustomResolutionStrategy() {
        strategyMap.put(new PropertyImpl("http://www.w3.org/2000/01/rdf-schema#label"),ResolutionStrategy.MAX_VALUE);
        strategyMap.put(new PropertyImpl("http://www.w3.org/2000/01/rdf-schema#comment"),ResolutionStrategy.MIN_VALUE);
        strategyMap.put(new PropertyImpl("http://www.w3.org/2000/01/rdf-schema#seeAlso"),ResolutionStrategy.LONG_VALUE);
        strategyMap.put(new PropertyImpl("http://www.w3.org/2000/01/rdf-schema#isDefinedBy"),ResolutionStrategy.SHORT_VALUE);
        strategyMap.put(new PropertyImpl("http://www.w3.org/2000/01/rdf-schema#member"),ResolutionStrategy.RECENT_DATE);
        strategyMap.put(new PropertyImpl("http://www.w3.org/2000/01/rdf-schema#subClassOf"),ResolutionStrategy.HIGHEST_PRIORITY);
        strategyMap.put(new PropertyImpl("http://www.w3.org/2000/01/rdf-schema#subPropertyOf"),ResolutionStrategy.PRIORITY_AND_FREQUENCY);
        strategyMap.put(new PropertyImpl("http://www.w3.org/2000/01/rdf-schema#domain"),ResolutionStrategy.MOST_FREQUENT);

    }

    @Override
    public RDFNode resolveConflict(ListMultimap<RDFNode, Integer> objects, Resource subject, Property predicate) {
        RDFNode resolvedObject = null;
        ResolutionStrategy strategy = strategyMap.get(predicate);
        if (strategy == ResolutionStrategy.MAX_VALUE) {
            resolvedObject = getMaxValueForProperty(objects);
        } else if (strategy == ResolutionStrategy.MIN_VALUE) {
            resolvedObject= getMinValueForProperty(objects);
        } else if(strategy == ResolutionStrategy.LONG_VALUE){
            resolvedObject = getLongestValueForPropertyy(objects);
        } else if(strategy == ResolutionStrategy.SHORT_VALUE){
            resolvedObject = getShortestValueForPropertyy(objects);
        }else if(strategy == ResolutionStrategy.RECENT_DATE){
            MostRecentResolusionStrategy strategy1 = new MostRecentResolusionStrategy();
            resolvedObject = strategy1.resolveConflict(objects, subject, predicate);
        }else if(strategy == ResolutionStrategy.OLDEST_DATE){
            MostOldestResolutionStrategy strategy1 = new MostOldestResolutionStrategy();
            resolvedObject = strategy1.resolveConflict(objects, subject, predicate);
        }else if(strategy == ResolutionStrategy.MOST_FREQUENT){
            MostFrequentResolutionStrategy strategy1 = new MostFrequentResolutionStrategy();
            resolvedObject = strategy1.resolveConflict(objects, subject, predicate);
        }else if(strategy == ResolutionStrategy.HIGHEST_PRIORITY){
            PriorityBasedResolutionStrategy strategy1 = new PriorityBasedResolutionStrategy();
            resolvedObject = strategy1.resolveConflict(objects, subject, predicate);
        }else if(strategy == ResolutionStrategy.PRIORITY_AND_FREQUENCY){
            FrequencyAndPriorityBaseStrategy strategy1 = new FrequencyAndPriorityBaseStrategy();
            resolvedObject = strategy1.resolveConflict(objects, subject, predicate);
        }else if(strategy == ResolutionStrategy.MANUAL_REVIEW){
            ManualReviewResolutionStrategy strategy1 = new ManualReviewResolutionStrategy();
            resolvedObject = strategy1.resolveConflict(objects, subject, predicate);
        }else {
            ManualReviewResolutionStrategy strategy1 = new ManualReviewResolutionStrategy();
            resolvedObject = strategy1.resolveConflict(objects, subject, predicate);
        }
        return resolvedObject;
    }

    
   
    //Type of resolution strategies
    enum ResolutionStrategy {
        MAX_VALUE,
        MIN_VALUE,
        LONG_VALUE,
        SHORT_VALUE,
        RECENT_DATE,
        OLDEST_DATE,
        MOST_FREQUENT,
        HIGHEST_PRIORITY,
        PRIORITY_AND_FREQUENCY,
        MANUAL_REVIEW,
    }

    // Define other custom resolution strategies as needed

    private RDFNode getLongestValueForPropertyy(ListMultimap<RDFNode, Integer> objects) {

        if (objects == null || objects.isEmpty()) {
            throw new IllegalArgumentException("Input map is null or empty");
        }

        RDFNode longestNode = null;
        int longestString = Integer.MIN_VALUE;

        for (Map.Entry<RDFNode, Integer> entry : objects.entries()) {
            RDFNode node = entry.getKey();

            if (node.toString().length() > longestString) {
                longestNode = node;
                longestString = node.toString().length();
            } 
        }

        return longestNode;
    }

    private RDFNode getShortestValueForPropertyy(ListMultimap<RDFNode, Integer> objects) {

       if (objects == null || objects.isEmpty()) {
            throw new IllegalArgumentException("Input map is null or empty");
        }

        RDFNode longestNode = null;
        int longestString = Integer.MAX_VALUE;

        for (Map.Entry<RDFNode, Integer> entry : objects.entries()) {
            RDFNode node = entry.getKey();

            if (node.toString().length() < longestString) {
                longestNode = node;
                longestString = node.toString().length();
            } 
        }

        return longestNode;
    }
    
    private static RDFNode getMinValueForProperty(ListMultimap<RDFNode, Integer> objects) {
        //This Function the node that have the minimum value for the RDFNode
        if (objects == null || objects.isEmpty()) {
            throw new IllegalArgumentException("Input map is null or empty");
        }

        RDFNode minNode = null;
        int minValue = Integer.MAX_VALUE;

        for (Map.Entry<RDFNode, Integer> entry : objects.entries()) {
            RDFNode node = entry.getKey();
            int value = entry.getValue();

            if (value < minValue) {
                minValue = value;
                minNode = node;
            }
        }

        return minNode;
        
    }

    private static RDFNode getMaxValueForProperty(ListMultimap<RDFNode, Integer> objects) {
        //This Function the node that have the minimum value for the RDFNode
        if (objects == null || objects.isEmpty()) {
            throw new IllegalArgumentException("Input map is null or empty");
        }

        RDFNode maxNode = null;
        int maxValue = Integer.MIN_VALUE;

        for (Map.Entry<RDFNode, Integer> entry : objects.entries()) {
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
