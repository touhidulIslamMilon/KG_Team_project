package FinalPackage.Merging.Strategies;

import java.util.Map;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import com.google.common.collect.ListMultimap;

public class SementicResolutionStrategy implements Strategy{

    @Override
    public RDFNode resolveConflict(ListMultimap<RDFNode, Integer> objects, Resource subject, Property predicate) {
        Double jacabDis= 0.8;
        RDFNode bestNode = null;
        for (Map.Entry<RDFNode, Integer> entry : objects.entries()) {
            RDFNode node = entry.getKey();
            HelperFunction helperFunction = new HelperFunction();
            if(helperFunction.calculateJaccardDistance(node.toString(),bestNode.toString())<jacabDis){
                bestNode = fusenode(node, bestNode);
            }else{
                // TODO 
                // chose one of the node if jacab distance is more then specified
            }
        }
        return bestNode;
    }
    public RDFNode fusenode(RDFNode node1, RDFNode node2) {
        HelperFunction helperFunction = new HelperFunction();
        String type1 = helperFunction.objectType(node1);
        String type2 = helperFunction.objectType(node1);

        if(type1 == type2){
                switch (type1) {
                case "string":
                        //TODO - This is where the new literal is created
                        // return concatination of two literals
                        //return longest literal
                        RDFNode longest = helperFunction.findLongestString(node1, node2);
                        //return the literal with the most capital letters
                        return helperFunction.concatenateLiterals(node1, node2);
                case "number":
                     try{
                        //if both are number the return the average
                        Double num1 =  Double.parseDouble(node1.toString());
                        Double num2 = Double.parseDouble(node2.toString());
                        Double average = (num1+num2)/2;
                        Literal averageLiteral = ModelFactory.createDefaultModel().createTypedLiteral(average,  XSDDatatype.XSDdecimal);
                        //TODO - This is where the new literal is created
                        // we will return average as a literal
                        return averageLiteral;
                    }catch(Exception e){
                        System.out.println("Not a number");
                        //TODO - This is where the new literal is created
                        helperFunction.concatenateLiterals(node1, node2);
                        //return longest literal
                        helperFunction.findLongestString(node1, node2);
                        //return the literal with the most capital letters
                        return helperFunction.concatenateLiterals(node1, node2);
                    }
                case "text":
                    //TODO - This is where the new literal is created
                        helperFunction.concatenateLiterals(node1, node2);
                        //return longest literal
                        helperFunction.findLongestString(node1, node2);
                        //return the literal with the most capital letters
                        return helperFunction.concatenateLiterals(node1, node2);
                case "decimal":
                    try{
                        //if both literal are decimal then return the average
                        Double num1 =  Double.parseDouble(node1.toString());
                        Double num2 = Double.parseDouble(node2.toString());
                        Double max= helperFunction.findMaxDouble(num1,num2);
                        Double min=  helperFunction.findMinDouble(num1,num2);
                        Double average = (num1+num2)/2;
                        Literal averageLiteral = ModelFactory.createDefaultModel().createTypedLiteral(average,  XSDDatatype.XSDdecimal);
                       
                        return averageLiteral;
                        
                    }catch(Exception e){

                        //TODO - This is where the new literal is created
                        helperFunction.concatenateLiterals(node1, node2);
                        //return longest literal
                        helperFunction.findLongestString(node1, node2);
                        //return the literal with the most capital letters
                        return helperFunction.concatenateLiterals(node1, node2);
                    }
                    
                case "integer":
                    try{
                        //if both literal are decimal then return the average
                        int num1 = Integer.parseInt(node1.toString());
                        int num2 = Integer.parseInt(node2.toString());

                        Double average =  ((double) num1 + num2) / 2.0;
                        int max = helperFunction.findMaxInt(num1,num2);
                        int min=  helperFunction.findMinInt(num1,num2);
                        RDFNode averageNode = ModelFactory.createDefaultModel().createTypedLiteral(average,  XSDDatatype.XSDint);
                        return averageNode;
                        
                    }catch(Exception e){

                        //TODO - This is where the new literal is created
                       helperFunction.concatenateLiterals(node1, node2);
                        //return longest literal
                        helperFunction.findLongestString(node1, node2);
                        //return the literal with the most capital letters
                        return helperFunction.concatenateLiterals(node1, node2);
                    }
                case "boolean":
                    //TODO - This is where the new literal is creat
                    
                    //return the literal with the most capital letter
                    helperFunction.concatenateLiterals(node1, node2);
                    return helperFunction.findLongestString(node1, node2);
                
                case "date":
                    //if both are date then return the recent date
                    if(helperFunction.containsDate(node1.toString()) && helperFunction.containsDate(node2.toString())){
                        RDFNode recentDate = helperFunction.findMostRecentDate(node1, node2);
                        //TODO - This is where the new literal is created
                        // we will return recentDate as a literal
                        return recentDate;
                    }else{

                        //TODO - This is where the new literal is creat
                        
                        //return the literal with the most capital letter
                        helperFunction.concatenateLiterals(node1, node2);
                        return helperFunction.findLongestString(node1, node2);
                    }
                case "dateTime":
                    RDFNode recentDate = helperFunction.findMostRecentDate(node1,  node2);
                    //TODO - This is where the new literal is created
                        // we will return recentDate as a literal
                    return recentDate;
                default:
                    //TODO - This is where the new literal is creat
                    
                    //return the literal with the most capital letter
                    helperFunction.concatenateLiterals(node1, node2);
                    return helperFunction.findLongestString(node1, node2);
            }
        }else{
            System.out.println("Not same type");
            //TODO - This is where the new literal is creat
             // return concatination of two literals
            System.out.println("Unknown type");
            return helperFunction.concatenateLiterals(node1, node2);
        }
        

    }
    
}
