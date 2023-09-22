
//the is a fusion strategy class with multiple method take two object and return one
//boilerplate code for class
package FinalPackage.Merging.Strategies;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashSet;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Arrays;
public class fusionStrategy {

    public Set<RDFNode>  fusion(Set<RDFNode> allObjects, Resource subject, Property predicate) {
    
        // It get through all the object and return only the unique ones
        //
        //Set<RDFNode> allNode= allObjects.keySet();

        String[]  MultiplePredicateString = {"Sibling", "Parents", "Friends", "Examples"};
        List<String> MultiplePredicate = new ArrayList<>();

        MultiplePredicate.addAll(0, MultiplePredicate);
        //cheak there is more then two unique object for same subject and predicate
       if(getUiqueRDF(allObjects).size()>2){

             // return value base on different stragegy.
            
            
            // if predicate containg plural string or predicate indicate someting plural only then 
            // we go throgugh confilict resolution function. Otherwise we return all the unique value from the list
            if(MultiplePredicate.contains(predicate.toString()) || checkPlural(predicate.toString())){

                return getUiqueRDF(allObjects);

            }else{


                //return one node which have height frequency
                //majorityVote(allObjects);
            
                 //return one node which have height weight
                //heigestWeight(allObjects);

                 //return one node which have height weight * frequency
                //findNodeWithHighestWeight(allObjects);

                //from a list of node return the node which have been created most recently
                //findMostRecentNode(allObjects);
                 
                Set<RDFNode> returnNodes = new HashSet<>();
                //returnNodes.add(resolveConflict(allObjects));
                return returnNodes;
                
            }
            
        }else{
            // if Number of unique node is less then 2 then return all the unique nodes
            return getUiqueRDF(allObjects);
        }
	}

    // It get through all the object and return only the unique ones
    private Set<RDFNode> getUiqueRDF(Set<RDFNode> allObjects) {
        return allObjects.stream()
                                    .distinct()
                                    .collect(Collectors.toSet());
    }

    //this fluction cheaks if the string is singular or plural
    static boolean checkPlural(String str)
	{
	   int  n=str.length();
	    if(str.charAt(n-1)=='s')
	    {
	        return true;
	    }
	    return false;
	}
  
   

    //this method take two number and return mean of them as a object
    
    
    //this mathod take two dates data type and return the most recent one
    public RDFNode recentDate(RDFNode a, RDFNode b) {
        Date date1 = new Date(a.toString());
        Date date2 = new Date(b.toString());
        if (date1.after(date2)) {
            return a;
        } else {
            return b;
        }
    }
    

     //this mathod take two dates data type and return the most old one
    public Object oldDate(Object a, Object b) {
        Date date1 = new Date(a.toString());
        Date date2 = new Date(b.toString());
        if (date1.before(date2)) {
            return a;
        } else {
            return b;
        }
    }

   

    //this method take array of number and return the max one
    public double max_num_a(double[] a) {
        double max = a[0];
        for (int i = 1; i < a.length; i++) {
            if (max < a[i]) {
                max = a[i];
            }
        }
        return max;
    }

    //this method take array of number and return the min one
    public double min_num_a(double[] a) {
        double min = a[0];
        for (int i = 1; i < a.length; i++) {
            if (min > a[i]) {
                min = a[i];
            }
        }
        return min;
    }

    //this method take two rdf node and return union of them
    
    //this method take two list of string and return the union of them without duplicate
    public String[] union_string(String[] a, String[] b) {
        String[] c = new String[a.length + b.length];
        int i = 0;
        for (String s : a) {
            c[i] = s;
            i++;
        }
        for (String s : b) {
            if (!isExist(c, s)) {
                c[i] = s;
                i++;
            }
        }
        
        return c;
    }
    
    //this is the isExist method that check if the string is exist in the array or not
    public boolean isExist(String[] a, String b) {
        for (String s : a) {
            if (s.equals(b)) {
                return true;
            }
        }
        return false;
    }

    //this method take two string and retrun one if they are equal otherwise return a array contain both of them
    public String[] equal_string(String a, String b) {
        if (a.equals(b)) {
            String[] c = {a};
            return c;
        } else {
            String[] c = {a, b};
            return c;
        }
    }

    private static RDFNode resolvePredicateConflictfrist(RDFNode object, RDFNode object1) {

        return object;
    }
    private static RDFNode resolvePredicateConflictlast(RDFNode object, RDFNode object1) {

        return object1;
    }
    

    //create function that will take two object and return first one
    //if the length of the first one is greater than the second one 
    //else return the second one
    public static RDFNode resolvePredicateConflictlenght(RDFNode object, RDFNode object1) {
        if(object.toString().length()>object1.toString().length()){
            return object;
        }
        return object1;
    }
    

    public static int getIntersectionSize(Set<String> set1, Set<String> set2) {
        // Calculate the size of the intersection of two sets
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        return intersection.size();
    }

    public static int getUnionSize(Set<String> set1, Set<String> set2) {
        // Calculate the size of the union of two sets
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        return union.size();
    }
}
