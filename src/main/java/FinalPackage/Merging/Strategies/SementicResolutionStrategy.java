package FinalPackage.Merging.Strategies;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.*;
import org.eclipse.rdf4j.model.vocabulary.XMLSchema;

public class SementicResolutionStrategy implements Strategy{
    static Map<String, ResolutionStrategy> strategyMap = new HashMap<>();
    Model model = ModelFactory.createDefaultModel();
    static {
        strategyMap.put("string", ResolutionStrategy.LONG_VALUE);
        strategyMap.put("date", ResolutionStrategy.RECENT_DATE);
        strategyMap.put("integer", ResolutionStrategy.MEAN);
        strategyMap.put("decimal", ResolutionStrategy.MEDIAN);
        strategyMap.put("boolean", ResolutionStrategy.MAX_VALUE);
        strategyMap.put("dateTime", ResolutionStrategy.RECENT_DATE);
        strategyMap.put("text", ResolutionStrategy.LONG_VALUE);
        strategyMap.put("number", ResolutionStrategy.MEAN);
        //strategyMap.put("uri", ResolutionStrategy.LONG_VALUE);
    }
    @Override
    public RDFNode resolveConflict(ListMultimap<RDFNode, Integer> objects, Resource subject, Property predicate) {
        Double jacabDis = 0.8;
        RDFNode bestNode = ResourceFactory.createResource("");
        for (RDFNode key : objects.keySet()) {
            RDFNode node = key;
            HelperFunction helperFunction = new HelperFunction();
            if (helperFunction.calculateJaccardDistance(node.toString(), bestNode.toString()) > jacabDis) {
                bestNode = fusenode(node, bestNode, predicate, subject);
            } else {
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
    public RDFNode fusenode(RDFNode node1, RDFNode node2, Property Predicate, Resource subject) {
        HelperFunction helperFunction = new HelperFunction();
        String type1 = helperFunction.objectType(node1);
        String type2 = helperFunction.objectType(node1);

        if(type1 == type2){
               return ResolveTwoObject(node1, node2 , Predicate, subject, type1);
        }else{
            System.out.println("Not same type");
            System.out.println("Unknown type");
            return helperFunction.concatenateLiterals(node1, node2);
        }
        

    }
    
    //This is a methods that take two RDFNode and return the most recent one
    public RDFNode ResolveTwoObject(RDFNode First, RDFNode Second, Property predicate, Resource subject, String type) {
        System.out.println("Type: " + type);
        RDFNode resolvedObject = null;
        ResolutionStrategy Strategy = strategyMap.get(type);
        ResolutionStrategy strategy = strategyMap.get(predicate);
        HelperFunction helperFunction = new HelperFunction();
        if (strategy == ResolutionStrategy.MAX_VALUE) {
            try{
                Double var1 = Double.parseDouble(First.toString());
                Double var2 = Double.parseDouble(Second.toString());
                resolvedObject = model.createTypedLiteral(String.valueOf( helperFunction.max_num(var1, var2)),  XSDDatatype.XSDlong);   
            }catch(Exception e){
                //If the jacabDis is greater than 0.8, it could mean it is just a spelling mistake. we will use the manual review strategy
                ListMultimap<RDFNode, Integer> objectForReview = ArrayListMultimap.create();  
                objectForReview.put(First, 1);
                objectForReview.put(Second, 1);  
                ManualReviewResolutionStrategy manualReviewStrategy = new ManualReviewResolutionStrategy();
                System.out.println("One of the value is not number.Please select the correct value");
                resolvedObject = manualReviewStrategy.resolveConflict(objectForReview, subject, predicate);
            }
        } else if (strategy == ResolutionStrategy.MIN_VALUE) {
            try{
                Double var1 = Double.parseDouble(First.toString());
                Double var2 = Double.parseDouble(Second.toString());
                resolvedObject =  model.createTypedLiteral(String.valueOf(helperFunction.min_num(var1, var2)),  XSDDatatype.XSDlong);   
            }catch(Exception e){
                //If the jacabDis is greater than 0.8, it could mean it is just a spelling mistake. we will use the manual review strategy
                ListMultimap<RDFNode, Integer> objectForReview = ArrayListMultimap.create();  
                objectForReview.put(First, 1);
                objectForReview.put(Second, 1);  
                ManualReviewResolutionStrategy manualReviewStrategy = new ManualReviewResolutionStrategy();
                System.out.println("One of the value is not number.Please select the correct value");
                resolvedObject = manualReviewStrategy.resolveConflict(objectForReview, subject, predicate);
            }
        } else if(strategy == ResolutionStrategy.LONG_VALUE){
            resolvedObject = helperFunction.findLongestString(First, Second);
        } else if(strategy == ResolutionStrategy.SHORT_VALUE){
            resolvedObject = helperFunction.findShortestString(First, Second);
        }else if(strategy == ResolutionStrategy.RECENT_DATE){
            try{
                resolvedObject = model.createTypedLiteral(String.valueOf(helperFunction.findMostRecentDate(First, Second)),  XSDDatatype.XSDdateTime);   
            }catch(Exception e){
                //If there is a error in Conversion then we will use the manual review strategy
                ListMultimap<RDFNode, Integer> objectForReview = ArrayListMultimap.create();  
                objectForReview.put(First, 1);
                objectForReview.put(Second, 1);  
                ManualReviewResolutionStrategy manualReviewStrategy = new ManualReviewResolutionStrategy();
                System.out.println("One of the value is not Date.Please select the correct value");
                resolvedObject = manualReviewStrategy.resolveConflict(objectForReview, subject, predicate);
            }
        }else if(strategy == ResolutionStrategy.OLDEST_DATE){
            try{
                resolvedObject = model.createTypedLiteral(String.valueOf(helperFunction.findMostOldDate(First, Second)),  XSDDatatype.XSDdateTime);   
            }catch(Exception e){
                //If there is a error in Conversion then we will use the manual review strategy
                ListMultimap<RDFNode, Integer> objectForReview = ArrayListMultimap.create();  
                objectForReview.put(First, 1);
                objectForReview.put(Second, 1);  
                ManualReviewResolutionStrategy manualReviewStrategy = new ManualReviewResolutionStrategy();
                System.out.println("One of the value is not Date.Please select the correct value");
                resolvedObject = manualReviewStrategy.resolveConflict(objectForReview, subject, predicate);
            }
        }else if(strategy == ResolutionStrategy.MANUAL_REVIEW){
            ListMultimap<RDFNode, Integer> objectForReview = ArrayListMultimap.create();  
                objectForReview.put(First, 1);
                objectForReview.put(Second, 1);  
                ManualReviewResolutionStrategy manualReviewStrategy = new ManualReviewResolutionStrategy();
                System.out.println("Parsing problem. Please select the correct value");
                resolvedObject = manualReviewStrategy.resolveConflict(objectForReview, subject, predicate);
        }else if(strategy == ResolutionStrategy.MEAN){
            try {
                resolvedObject = helperFunction.findMean(First, Second);
            } catch (ParseException e) {
                ListMultimap<RDFNode, Integer> objectForReview = ArrayListMultimap.create();  
                objectForReview.put(First, 1);
                objectForReview.put(Second, 1);  
                ManualReviewResolutionStrategy manualReviewStrategy = new ManualReviewResolutionStrategy();
                System.out.println("Parsing problem. Please select the correct value");
                resolvedObject = manualReviewStrategy.resolveConflict(objectForReview, subject, predicate);
            }
        }else if(strategy == ResolutionStrategy.MEDIAN){
            ListMultimap<RDFNode, Integer> objectForReview = ArrayListMultimap.create();  
            objectForReview.put(First, 1);
            objectForReview.put(Second, 1);  
            resolvedObject = helperFunction.getMedianValue(objectForReview, subject, predicate);
            
            
        }else {
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
