package FinalPackage.Merging.Strategies;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import de.lmu.ifi.dbs.elki.math.Mean;
import smile.stat.Hypothesis.t;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.yarn.webapp.hamlet.Hamlet.P;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.*;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.XMLSchema;
import org.gradoop.common.model.impl.properties.PropertyValueUtils.Boolean;

public class SementicResolutionStrategy implements Strategy{
    static Map<String, ResolutionStrategy> strategyMap = new HashMap<>();
    Model model = ModelFactory.createDefaultModel();
    static {
        //sting uri and text only support long_vale and short value
        strategyMap.put("string", ResolutionStrategy.LONG_VALUE);
        strategyMap.put("uri", ResolutionStrategy.SHORT_VALUE);
        strategyMap.put("text", ResolutionStrategy.LONG_VALUE);

        //integer, number, boolean and decimal only support mean, median, max_value and min_value
        strategyMap.put("integer", ResolutionStrategy.MEDIAN);
        strategyMap.put("number", ResolutionStrategy.MEDIAN);
        strategyMap.put("boolean", ResolutionStrategy.MAX_VALUE);
        strategyMap.put("decimal", ResolutionStrategy.MEAN);

        //Date, time, and dateTime only support Recent_date and oldest_date
        strategyMap.put("dateTime", ResolutionStrategy.RECENT_DATE);
        strategyMap.put("time", ResolutionStrategy.RECENT_DATE);
        strategyMap.put("date", ResolutionStrategy.OLDEST_DATE);
       
        
    }
    @Override
    public RDFNode resolveConflict(ListMultimap<RDFNode, Integer> objects, Resource subject, Property predicate) {
        Double jacabDis = 0.2;
        RDFNode bestNode = ResourceFactory.createResource("");
        boolean isMean = true;
        HelperFunction helperFunction = new HelperFunction();
        boolean isMedian = true;
        for(RDFNode node : objects.keySet()){
            String type = new HelperFunction().objectType(node);
            // System.out.println("Type: " + type);
            ResolutionStrategy Strategy = strategyMap.get(type);
            if(Strategy !=ResolutionStrategy.MEAN && Strategy !=ResolutionStrategy.MEDIAN){
                isMean = false;
                isMedian = false;
                break;
            }
            if(Strategy != ResolutionStrategy.MEAN ){
                isMean = false;
                break;
            }
            if(Strategy != ResolutionStrategy.MEDIAN ){
                isMedian = false;
                break;
            }
        }
        if(isMean){
            // System.out.println("Mean True");
            try {
                System.out.println("Strategy: " + ResolutionStrategy.MEAN);
                return gerMeanVelue(objects, subject, predicate);

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if(isMedian){
            // System.out.println("Median True");
            System.out.println("Strategy: " + ResolutionStrategy.MEDIAN);
            return helperFunction.getMedianValue(objects, subject, predicate);
            
        }
        for(RDFNode node : objects.keySet()){
            System.out.println("Node: " + node.toString()+" Type: "+helperFunction.objectType(node));
        }
        for (RDFNode key : objects.keySet()) {
            RDFNode node = key;
            //if the node is empty, then we will skip it
            if(node.toString().equals("")|| node == null){
                System.out.println("This node node is empty");
                continue;
            }
            //if the best node is empty, then we will use the second node
            if(bestNode.toString().equals("")|| bestNode == null){
                bestNode = node;
                continue;
            }
            if (helperFunction.calculateJaccardDistance(node.toString(), bestNode.toString()) > jacabDis) {
                
                if(node.isLiteral() && bestNode.isLiteral()|| node.isResource() && bestNode.isResource()){
                    bestNode = fusenode(node, bestNode, predicate, subject);
                }else{
                    bestNode = fusenode(node, bestNode, predicate, subject);
                }

            } else {
                System.out.println("Jaccard Distance: " + helperFunction.calculateJaccardDistance(node.toString(), bestNode.toString()));
                //If the jacabDis is greater than 0.8, it could mean it is just a spelling mistake. we will use the manual review strategy
                ListMultimap<RDFNode, Integer> objectForReview = ArrayListMultimap.create();  
                objectForReview.put(node, 1);
                objectForReview.put(bestNode, 1);  
                ManualReviewResolutionStrategy manualReviewStrategy = new ManualReviewResolutionStrategy();
                System.out.println("Both object have slightly different value.It could be spelling mistake Please select the correct value");
                bestNode = manualReviewStrategy.resolveConflict(objectForReview, subject, predicate);
            }
        }
        return bestNode;
    }

    private RDFNode gerMeanVelue(ListMultimap<RDFNode, Integer> objects, Resource subject, Property predicate) throws ParseException {
        Double sum=0.0;
        for (RDFNode key : objects.keySet()) {
            RDFNode node = key;
            
            sum = sum + Double.parseDouble(node.toString());
        }
        return ResourceFactory.createResource(String.valueOf(sum/objects.size()));
    }
    public RDFNode fusenode(RDFNode node1, RDFNode node2, Property Predicate, Resource subject) {
        HelperFunction helperFunction = new HelperFunction();
        String type1 = helperFunction.objectType(node1);
        String type2 = helperFunction.objectType(node1);
        if(type1 == type2){
               return ResolveTwoObject(node1, node2 , Predicate, subject, type1);
        }else{
            ListMultimap<RDFNode, Integer> objectForReview = ArrayListMultimap.create();  
            objectForReview.put(node1, 1);
            objectForReview.put(node2, 1);  
            ManualReviewResolutionStrategy manualReviewStrategy = new ManualReviewResolutionStrategy();
            System.out.println("One of the value is not number.Please select the correct value");
            return manualReviewStrategy.resolveConflict(objectForReview, subject, Predicate);
        }
    }
    
    //This is a methods that take two RDFNode and return the most recent one
    public RDFNode ResolveTwoObject(RDFNode First, RDFNode Second, Property predicate, Resource subject, String type) {

        RDFNode resolvedObject = null;
        ResolutionStrategy Strategy = strategyMap.get(type);
        HelperFunction helperFunction = new HelperFunction();
        System.out.println("Strategy: " + Strategy);
        if (Strategy == ResolutionStrategy.MAX_VALUE) {
            // System.out.println("Max value");
            try{
                Double var1 = Double.parseDouble(First.toString());
                Double var2 = Double.parseDouble(Second.toString());
                resolvedObject = model.createTypedLiteral(String.valueOf( helperFunction.max_num(var1, var2)));   
            }catch(Exception e){
                //If the jacabDis is greater than 0.8, it could mean it is just a spelling mistake. we will use the manual review strategy
                ListMultimap<RDFNode, Integer> objectForReview = ArrayListMultimap.create();  
                objectForReview.put(First, 1);
                objectForReview.put(Second, 1);  
                ManualReviewResolutionStrategy manualReviewStrategy = new ManualReviewResolutionStrategy();
                System.out.println("One of the value is not number.Please select the correct value");
                resolvedObject = manualReviewStrategy.resolveConflict(objectForReview, subject, predicate);
            }
        } else if (Strategy == ResolutionStrategy.MIN_VALUE) {
            // System.out.println("Min value");
            try{
                Double var1 = Double.parseDouble(First.toString());
                Double var2 = Double.parseDouble(Second.toString());
                resolvedObject =  model.createTypedLiteral(String.valueOf(helperFunction.min_num(var1, var2)));   
            }catch(Exception e){
                //If the jacabDis is greater than 0.8, it could mean it is just a spelling mistake. we will use the manual review strategy
                ListMultimap<RDFNode, Integer> objectForReview = ArrayListMultimap.create();  
                objectForReview.put(First, 1);
                objectForReview.put(Second, 1);  
                ManualReviewResolutionStrategy manualReviewStrategy = new ManualReviewResolutionStrategy();
                System.out.println("One of the value is not number.Please select the correct value");
                resolvedObject = manualReviewStrategy.resolveConflict(objectForReview, subject, predicate);
            }
        } else if(Strategy == ResolutionStrategy.LONG_VALUE){
            // System.out.println("Long value");
            resolvedObject = helperFunction.findLongestString(First, Second);
        } else if(Strategy == ResolutionStrategy.SHORT_VALUE){
            // System.out.println("Short value");
            resolvedObject = helperFunction.findShortestString(First, Second);
        }else if(Strategy == ResolutionStrategy.RECENT_DATE){
            // System.out.println("Recent date");
            try{
                resolvedObject = model.createTypedLiteral(String.valueOf(helperFunction.findMostRecentDate(First, Second)));   
            }catch(Exception e){
                //If there is a error in Conversion then we will use the manual review strategy
                ListMultimap<RDFNode, Integer> objectForReview = ArrayListMultimap.create();  
                objectForReview.put(First, 1);
                objectForReview.put(Second, 1);  
                ManualReviewResolutionStrategy manualReviewStrategy = new ManualReviewResolutionStrategy();
                System.out.println("One of the value is not Date.Please select the correct value");
                resolvedObject = manualReviewStrategy.resolveConflict(objectForReview, subject, predicate);
            }
        }else if(Strategy == ResolutionStrategy.OLDEST_DATE){
            // System.out.println("Oldest date");
            try{
                resolvedObject = model.createTypedLiteral(String.valueOf(helperFunction.findMostOldDate(First, Second)));   
            }catch(Exception e){
                //If there is a error in Conversion then we will use the manual review strategy
                ListMultimap<RDFNode, Integer> objectForReview = ArrayListMultimap.create();  
                objectForReview.put(First, 1);
                objectForReview.put(Second, 1);  
                ManualReviewResolutionStrategy manualReviewStrategy = new ManualReviewResolutionStrategy();
                System.out.println("One of the value is not Date.Please select the correct value");
                resolvedObject = manualReviewStrategy.resolveConflict(objectForReview, subject, predicate);
            }
        }else if(Strategy == ResolutionStrategy.MANUAL_REVIEW){
            // System.out.println("Manual review");
            ListMultimap<RDFNode, Integer> objectForReview = ArrayListMultimap.create();  
                objectForReview.put(First, 1);
                objectForReview.put(Second, 1);  
                ManualReviewResolutionStrategy manualReviewStrategy = new ManualReviewResolutionStrategy();
                System.out.println("Parsing problem. Please select the correct value");
                resolvedObject = manualReviewStrategy.resolveConflict(objectForReview, subject, predicate);
        
        }else {
            // System.out.println("Default");
            ListMultimap<RDFNode, Integer> objectForReview = ArrayListMultimap.create();  
            objectForReview.put(First, 1);
            objectForReview.put(Second, 1);  
            ManualReviewResolutionStrategy manualReviewStrategy = new ManualReviewResolutionStrategy();
            System.out.println("No Strategy Have been Speciried .Please select the correct value");
            resolvedObject = manualReviewStrategy.resolveConflict(objectForReview, subject, predicate);
        }
        return resolvedObject;
    }
    
}
