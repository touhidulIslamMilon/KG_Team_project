package FinalPackage.Merging.Strategies;

import java.util.HashMap;
import java.util.Map;
import com.google.common.collect.ListMultimap;

import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.impl.PropertyImpl;

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
        strategyMap.put(new PropertyImpl("http://www.w3.org/2000/01/rdf-schema#range"),ResolutionStrategy.MEAN);
        strategyMap.put(new PropertyImpl("http://www.w3.org/2000/01/rdf-schema#subPropert"),ResolutionStrategy.MEDIAN);

    }

    @Override
    public RDFNode resolveConflict(ListMultimap<RDFNode, Integer> objects, Resource subject, Property predicate) {
        RDFNode resolvedObject = null;
        ResolutionStrategy strategy = strategyMap.get(predicate);
        if (strategy == ResolutionStrategy.MAX_VALUE) {
            resolvedObject = getMaxValueForProperty(objects, subject, predicate);
        } else if (strategy == ResolutionStrategy.MIN_VALUE) {
            resolvedObject= getMinValueForProperty(objects, subject, predicate);
        } else if(strategy == ResolutionStrategy.LONG_VALUE){
            resolvedObject = getLongestValueForPropertyy(objects, subject, predicate);
        } else if(strategy == ResolutionStrategy.SHORT_VALUE){
            resolvedObject = getShortestValueForPropertyy(objects, subject, predicate);
        }else if(strategy == ResolutionStrategy.RECENT_DATE){
            MostRecentResolutionStrategy strategy1 = new MostRecentResolutionStrategy();
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
        }else if(strategy == ResolutionStrategy.MEAN){
            resolvedObject = getMeamValue(objects, subject, predicate);
        }else if(strategy == ResolutionStrategy.MEDIAN){
            HelperFunction helper = new HelperFunction();
            resolvedObject =helper.getMedianValue(objects, subject, predicate);
        }else {
            // ManualReviewResolutionStrategy strategy1 = new ManualReviewResolutionStrategy();
            // resolvedObject = strategy1.resolveConflict(objects, subject, predicate);

            DefaultStrategy defaultStrategy = new DefaultStrategy();
            resolvedObject = defaultStrategy.resolveConflict(objects, subject, predicate);
        }
        return resolvedObject;
    }

    private RDFNode getMeamValue(ListMultimap<RDFNode, Integer> objects, Resource subject, Property predicate) {
         if (objects == null || objects.isEmpty()) {
            System.out.println("Input map is null or empty");
            throw new IllegalArgumentException("Input map is null or empty");
        }
        Double MeanValue = 0.0;
        for (Map.Entry<RDFNode, Integer> entry : objects.entries()) {
            RDFNode node = entry.getKey();
            try {
                MeanValue += Double.parseDouble(node.asLiteral().getLexicalForm());
            } catch (Exception e) {
                // System.out.println("The value is not Integer");
                // ManualReviewResolutionStrategy strategy1 = new ManualReviewResolutionStrategy();
                // return strategy1.resolveConflict(objects, subject, predicate);

                RandomStrategy randomStrategy = new RandomStrategy();
                return randomStrategy.resolveConflict(objects, subject, predicate);
            }
               
        }
        RDFNode MeanNode = ResourceFactory.createResource(String.valueOf(MeanValue/objects.size()));
        return MeanNode;
    }



    //Type of resolution strategies
    

    // Define other custom resolution strategies as needed

    private RDFNode getLongestValueForPropertyy(ListMultimap<RDFNode, Integer> objects, Resource subject, Property predicate) {

        if (objects == null || objects.isEmpty()) {
            System.out.println("Input map is null or empty");
            throw new IllegalArgumentException("Input map is null or empty");
        }

        RDFNode longestNode = null;
        int longestString = Integer.MIN_VALUE;

        for (Map.Entry<RDFNode, Integer> entry : objects.entries()) {
            RDFNode node = entry.getKey();

            if (node.toString().length() > longestString) {
                longestNode = node;
                longestString = node.toString().length();
            } else if(node.toString().length() == longestString){
                // ManualReviewResolutionStrategy manualReview = new ManualReviewResolutionStrategy();
                // return manualReview.resolveConflict(objects, subject, predicate);

                RandomStrategy randomStrategy = new RandomStrategy();
                return randomStrategy.resolveConflict(objects, subject, predicate);
            }
        }

        return longestNode;
    }

    private RDFNode getShortestValueForPropertyy(ListMultimap<RDFNode, Integer> objects,Resource subject, Property predicate) {

       if (objects == null || objects.isEmpty()) {
            System.out.println("Input map is null or empty");
             throw new IllegalArgumentException("Input map is null or empty");
        }

        RDFNode longestNode = null;
        int longestString = Integer.MAX_VALUE;

        for (Map.Entry<RDFNode, Integer> entry : objects.entries()) {
            RDFNode node = entry.getKey();

            if (node.toString().length() < longestString) {
                longestNode = node;
                longestString = node.toString().length();
            } else if(node.toString().length() == longestString){
                // ManualReviewResolutionStrategy manualReview = new ManualReviewResolutionStrategy();
                // return manualReview.resolveConflict(objects, subject, predicate);

                DefaultStrategy defaultStrategy = new DefaultStrategy();
                return defaultStrategy.resolveConflict(objects, subject, predicate);
            }
        }

        return longestNode;
    }
    
    private static RDFNode getMinValueForProperty(ListMultimap<RDFNode, Integer> objects, Resource subject, Property predicate) {
        //This Function the node that have the minimum value for the RDFNode
        if (objects == null || objects.isEmpty()) {
            System.out.println("Input map is null or empty");
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
            }else if(value == minValue){
                // ManualReviewResolutionStrategy manualReview = new ManualReviewResolutionStrategy();
                // return manualReview.resolveConflict(objects, subject, predicate);

                DefaultStrategy defaultStrategy = new DefaultStrategy();
                return defaultStrategy.resolveConflict(objects, subject, predicate);
            }
        }

        return minNode;
        
    }

    private static RDFNode getMaxValueForProperty(ListMultimap<RDFNode, Integer> objects, Resource subject, Property predicate) {
        //This Function the node that have the minimum value for the RDFNode
        if (objects == null || objects.isEmpty()) {
            System.out.println("Input map is null or empty");
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
            }else if(value == maxValue){
                // ManualReviewResolutionStrategy manualReview = new ManualReviewResolutionStrategy();
                // return manualReview.resolveConflict(objects, subject, predicate);

                DefaultStrategy defaultStrategy = new DefaultStrategy();
                return defaultStrategy.resolveConflict(objects, subject, predicate);
            }
        }

        return maxNode;
    }
}
