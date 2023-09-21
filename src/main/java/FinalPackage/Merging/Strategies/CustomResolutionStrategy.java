package FinalPackage.Merging.Strategies;

import org.apache.jena.rdf.model.StmtIterator;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.google.common.collect.ListMultimap;
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
    //create constant for the class
    public CustomResolutionStrategy() {
        strategyMap.put(new PropertyImpl("http://www.w3.org/2000/01/rdf-schema#label"),ResolutionStrategy.MAX_VALUE);
        strategyMap.put(new PropertyImpl("http://www.w3.org/2000/01/rdf-schema#comment"),ResolutionStrategy.MAX_VALUE);
        strategyMap.put(new PropertyImpl("http://www.w3.org/2000/01/rdf-schema#seeAlso"),ResolutionStrategy.MAX_VALUE);
        strategyMap.put(new PropertyImpl("http://www.w3.org/2000/01/rdf-schema#isDefinedBy"),ResolutionStrategy.MAX_VALUE);
        strategyMap.put(new PropertyImpl("http://www.w3.org/2000/01/rdf-schema#member"),ResolutionStrategy.MAX_VALUE);
        strategyMap.put(new PropertyImpl("http://www.w3.org/2000/01/rdf-schema#subClassOf"),ResolutionStrategy.MAX_VALUE);
        strategyMap.put(new PropertyImpl("http://www.w3.org/2000/01/rdf-schema#subPropertyOf"),ResolutionStrategy.MAX_VALUE);
        strategyMap.put(new PropertyImpl("http://www.w3.org/2000/01/rdf-schema#domain"),ResolutionStrategy.MAX_VALUE);
        strategyMap.put(new PropertyImpl("http://www.w3.org/2000/01/rdf-schema#range"),ResolutionStrategy.MAX_VALUE);
        strategyMap.put(new PropertyImpl("http://www.w3.org/2000/01/rdf-schema#label"),ResolutionStrategy.MAX_VALUE);

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
            resolvedObject = getRecentDateForProperty(objects);
        }else if(strategy == ResolutionStrategy.OLDEST_DATE){
            resolvedObject = getOldestDateForProperty(objects);
        }else{
            Strategy strategy1 = new SementicResolutionStrategy();
            resolvedObject = strategy1.resolveConflict(objects, subject, predicate);
        }
        return resolvedObject;
    }

    
    private RDFNode getOldestDateForProperty(ListMultimap<RDFNode, Integer> objects) {

        //This Function the node that have the minimum value for the RDFNode
        if (objects == null || objects.isEmpty()) {
            throw new IllegalArgumentException("Input map is null or empty");
        }
        

        RDFNode oldestNode = null;
        LocalDate OldestDate;

        for (Map.Entry<RDFNode, Integer> entry : objects.entries()) {
            RDFNode node = entry.getKey();
            HelperFunction helper = new HelperFunction();
            String type= helper.objectType(node);
            if(type.equals("date")|| type.equals("dateTime")){
                OldestDate = helper.convertToDate(node);
                if (OldestDate.isBefore(helper.convertToDate(oldestNode))) {
                    oldestNode = node;
                }
            }else
            {
                //The type of the node is not date or dateTime
                //Need To use another strategy
                return null;
            }
            
        }

        return oldestNode;
    }

    private RDFNode getRecentDateForProperty(ListMultimap<RDFNode, Integer> objects) {
        //This Function the node that have the minimum value for the RDFNode
        if (objects == null || objects.isEmpty()) {
            throw new IllegalArgumentException("Input map is null or empty");
        }
        

        RDFNode recendNode = null;
        LocalDate recentDate;

        for (Map.Entry<RDFNode, Integer> entry : objects.entries()) {
            RDFNode node = entry.getKey();
            HelperFunction helper = new HelperFunction();
            String type= helper.objectType(node);
            if(type.equals("date")|| type.equals("dateTime")){
                recentDate = helper.convertToDate(node);
                if (recentDate.isBefore(helper.convertToDate(recendNode))) {
                    recendNode = node;
                }
            }else{
                //The type of the node is not date or dateTime
                //Need To use another strategy
                return null;
            }
            
        }

        return recendNode;
    }

    //Type of resolution strategies
    enum ResolutionStrategy {
        MAX_VALUE,
        MIN_VALUE,
        LONG_VALUE,
        SHORT_VALUE,
        RECENT_DATE,
        OLDEST_DATE        // Add more strategies here
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
            } 
        }

        return longestNode;
    }
    private RDFNode getShortestValueForPropertyy(ListMultimap<RDFNode, Integer> objects) {

        if (objects == null || objects.isEmpty()) {
            throw new IllegalArgumentException("Input map is null or empty");
        }

        RDFNode longestNode = null;
        int longestString = Integer.MIN_VALUE;

        for (Map.Entry<RDFNode, Integer> entry : objects.entries()) {
            RDFNode node = entry.getKey();

            if (node.toString().length() > longestString) {
                longestNode = node;
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
        int minValue = Integer.MIN_VALUE;

        for (Map.Entry<RDFNode, Integer> entry : objects.entries()) {
            RDFNode node = entry.getKey();
            int value = entry.getValue();

            if (value > minValue) {
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
