
//the is a fusion strategy class with multiple method take two object and return one
//boilerplate code for class
package FinalPackage.Merging;

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
        //Set<RDFNode> allObjects= allObjects.keySet();

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
                majorityVote(allObjects);
            
                 //return one node which have height weight
                //heigestWeight(allObjects);

                 //return one node which have height weight * frequency
                //findNodeWithHighestWeight(allObjects);

                //from a list of node return the node which have been created most recently
                //findMostRecentNode(allObjects);
                 
                Set<RDFNode> returnNodes = new HashSet<>();
                returnNodes.add(resolveConflict(allObjects));
                return returnNodes;
                
            }
            
        }else{
            // if Number of unique node is less then 2 then return all the unique nodes
            return getUiqueRDF(allObjects);
        }
	}


    private RDFNode resolveConflict(Set<RDFNode> allNode) {
        Double jacabDis= 0.8;
        RDFNode bestNode = allNode.iterator().next();
        for (RDFNode node : allNode) {
            if(calculateJaccardDistance(node.toString(),bestNode.toString())<jacabDis){
                bestNode = fusenode(node, bestNode);
            }else{
                // TODO 
                // chose one of the node if jacab distance is more then specified
            }
        }
        return bestNode;
    }
    public static double calculateJaccardDistance(String text1, String text2) {
        // Tokenize the texts into sets of words
        Set<String> set1 = tokenize(text1);
        Set<String> set2 = tokenize(text2);
        
        // Calculate the intersection and union sizes
        int intersectionSize = getIntersectionSize(set1, set2);
        int unionSize = getUnionSize(set1, set2);
        
        // Calculate the Jaccard similarity
        double jaccardSimilarity = (double) intersectionSize / unionSize;
        
        // Calculate the Jaccard distance
        double jaccardDistance = 1.0 - jaccardSimilarity;
        
        return jaccardDistance;
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
    public static RDFNode heigestWeight(Map<RDFNode, Double> nodeWeights) {
        if (nodeWeights == null || nodeWeights.isEmpty()) {
            return null;
        }

        RDFNode highestWeightNode = null;
        double highestWeight = Double.NEGATIVE_INFINITY;

        for (Entry<RDFNode, Double> entry : nodeWeights.entrySet()) {
            RDFNode node = entry.getKey();
            double weight = entry.getValue();

            if (weight > highestWeight) {
                highestWeight = weight;
                highestWeightNode = node;
            }
        }

        return highestWeightNode;
    }
    public static RDFNode findMostRecentNode(Map<RDFNode, Double> nodes) {
        RDFNode mostRecentNode = null;
        Date mostRecentDate = null;
        Map<RDFNode, Date> nodeCreationMap = convertMapToDates(nodes);

        for (Map.Entry<RDFNode, Date> entry : nodeCreationMap.entrySet()) {
            Date currentDate = entry.getValue();

            if (mostRecentDate == null || currentDate.after(mostRecentDate)) {
                mostRecentDate = currentDate;
                mostRecentNode = entry.getKey();
            }
        }

        return mostRecentNode;
    }

    public static RDFNode findNodeWithHighestWeight(Map<RDFNode, Double> nodeWeights) {
        Map<RDFNode, Double> weightedCounts = new HashMap<>();

        // Calculate the weighted count for each RDF node
        for (Map.Entry<RDFNode, Double> entry : nodeWeights.entrySet()) {
            RDFNode node = entry.getKey();
            double weight = entry.getValue();
            weightedCounts.put(node, weight * countNodeFrequency(node, nodeWeights.keySet()));
        }

        // Find the node with the highest weighted count
        RDFNode highestWeightedNode = null;
        double highestWeight = Double.NEGATIVE_INFINITY;

        for (Map.Entry<RDFNode, Double> entry : weightedCounts.entrySet()) {
            RDFNode node = entry.getKey();
            double weightedCount = entry.getValue();
            if (weightedCount > highestWeight) {
                highestWeight = weightedCount;
                highestWeightedNode = node;
            }
        }

        return highestWeightedNode;
    }

    public static int countNodeFrequency(RDFNode node, Set<RDFNode> nodeSet) {
        int frequency = 0;
        for (RDFNode n : nodeSet) {
            if (n.equals(node)) {
                frequency++;
            }
        }
        return frequency;
    }

    public static RDFNode majorityVote(Set<RDFNode> nodes) {
        Map<RDFNode, Integer> frequencyMap = new HashMap<>();
        
        // Count the frequency of each RDFNode
        for (RDFNode node : nodes) {
            frequencyMap.put(node, frequencyMap.getOrDefault(node, 0) + 1);
        }
        
        // Find the node with the highest frequency
        RDFNode highestFrequencyNode = null;
        int highestFrequency = 0;

        for (Map.Entry<RDFNode, Integer> entry : frequencyMap.entrySet()) {
            RDFNode node = entry.getKey();
            int frequency = entry.getValue();
            
            if (frequency > highestFrequency) {
                highestFrequency = frequency;
                highestFrequencyNode = node;
            }
        }
        
        return highestFrequencyNode;
    }
    
    public RDFNode fusenode(RDFNode node1, RDFNode node2) {
        String type1 = objectTypeType(node1);
        String type2 = objectTypeType(node1);

        if(type1 == type2){
                switch (type1) {
                case "string":
                        //TODO - This is where the new literal is created
                        // return concatination of two literals
                        //return longest literal
                        RDFNode longest = findLongestString(node1, node2);
                        //return the literal with the most capital letters
                        return concatenateLiterals(node1, node2);
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
                        concatenateLiterals(node1, node2);
                        //return longest literal
                        findLongestString(node1, node2);
                        //return the literal with the most capital letters
                        return concatenateLiterals(node1, node2);
                    }
                case "text":
                    //TODO - This is where the new literal is created
                        concatenateLiterals(node1, node2);
                        //return longest literal
                        findLongestString(node1, node2);
                        //return the literal with the most capital letters
                        return concatenateLiterals(node1, node2);
                case "decimal":
                    try{
                        //if both literal are decimal then return the average
                        Double num1 =  Double.parseDouble(node1.toString());
                        Double num2 = Double.parseDouble(node2.toString());
                        Double max= findMaxDouble(num1,num2);
                        Double min=  findMinDouble(num1,num2);
                        Double average = (num1+num2)/2;
                        Literal averageLiteral = ModelFactory.createDefaultModel().createTypedLiteral(average,  XSDDatatype.XSDdecimal);
                       
                        return averageLiteral;
                        
                    }catch(Exception e){

                        //TODO - This is where the new literal is created
                       concatenateLiterals(node1, node2);
                        //return longest literal
                        findLongestString(node1, node2);
                        //return the literal with the most capital letters
                        return concatenateLiterals(node1, node2);
                    }
                    
                case "integer":
                    try{
                        //if both literal are decimal then return the average
                        int num1 = Integer.parseInt(node1.toString());
                        int num2 = Integer.parseInt(node2.toString());

                        Double average =  ((double) num1 + num2) / 2.0;
                        int max= findMaxInt(num1,num2);
                        int min=  findMinInt(0, 0);
                        RDFNode averageNode = ModelFactory.createDefaultModel().createTypedLiteral(average,  XSDDatatype.XSDint);
                        return averageNode;
                        
                    }catch(Exception e){

                        //TODO - This is where the new literal is created
                       concatenateLiterals(node1, node2);
                        //return longest literal
                        findLongestString(node1, node2);
                        //return the literal with the most capital letters
                        return concatenateLiterals(node1, node2);
                    }
                case "boolean":
                    //TODO - This is where the new literal is creat
                    
                    //return the literal with the most capital letter
                    concatenateLiterals(node1, node2);
                    return findLongestString(node1, node2);
                
                case "date":
                    //if both are date then return the recent date
                    if(containsDate(node1.toString()) && containsDate(node2.toString())){
                        RDFNode recentDate = findMostRecentDate(node1, node2);
                        //TODO - This is where the new literal is created
                        // we will return recentDate as a literal
                        return recentDate;
                    }else{

                        //TODO - This is where the new literal is creat
                        
                        //return the literal with the most capital letter
                        concatenateLiterals(node1, node2);
                        return findLongestString(node1, node2);
                    }
                case "dateTime":
                    RDFNode recentDate = findMostRecentDate(node1,  node2);
                    //TODO - This is where the new literal is created
                        // we will return recentDate as a literal
                    return recentDate;
                default:
                    //TODO - This is where the new literal is creat
                    
                    //return the literal with the most capital letter
                    concatenateLiterals(node1, node2);
                    return findLongestString(node1, node2);
            }
        }else{
            System.out.println("Not same type");
            //TODO - This is where the new literal is creat
             // return concatination of two literals
            System.out.println("Unknown type");
            return concatenateLiterals(node1, node2);
        }
        

    }



    private String objectTypeType(RDFNode object) {
        if(object.isLiteral()){
            Literal literal = object.asLiteral();
            String datatypeURI = literal.getDatatypeURI();
            if (datatypeURI == null) {
                return detectStringType(object.toString());
            } else if (datatypeURI.equals("http://www.w3.org/2001/XMLSchema#string")) {
                return detectStringType(object.toString());
            } else if (datatypeURI.equals("http://www.w3.org/2001/XMLSchema#decimal")) {
                return "decimal";
            } else if (datatypeURI.equals("http://www.w3.org/2001/XMLSchema#integer")) {
                return "integer";
            } else if (datatypeURI.equals("http://www.w3.org/2001/XMLSchema#boolean")) {
                return "boolean";
            } else if (datatypeURI.equals("http://www.w3.org/2001/XMLSchema#date")) {
                return "date";
            } else if (datatypeURI.equals("http://www.w3.org/2001/XMLSchema#dateTime")) {
                return "dateTime";
            } else {
                return detectStringType(object.toString());
            }
        }else if(object.isURIResource()){
            return "uri";
        }else if(object.isResource()){
            return detectStringType(object.toString());
        }else{
            return detectStringType(object.toString());
        }
    }

    //get a string and return type of text, number, decimal, date, uri, unknown
    public static String detectStringType(String input) {
        // Regular expressions for pattern matching
        String uriRegex = "^(http|https|ftp)://[\\w\\p{Punct}]+$";
        String decimalRegex = "^[+-]?\\d*\\.\\d+$";
        String integerRegex = "^[+-]?\\d+$";
        String dateRegex = "^\\d{4}-\\d{2}-\\d{2}$";

        // Pattern objects for matching
        Pattern uriPattern = Pattern.compile(uriRegex);
        Pattern decimalPattern = Pattern.compile(decimalRegex);
        Pattern integerPattern = Pattern.compile(integerRegex);
        Pattern datePattern = Pattern.compile(dateRegex);

        // Text detection
        if (!input.isEmpty() && !input.matches(uriRegex) &&
            !input.matches(decimalRegex) && !input.matches(integerRegex) &&
            !input.matches(dateRegex)) {
            return "string";
        }

        // URI detection
        if (uriPattern.matcher(input).matches()) {
            return "uri";
        }

        // Decimal detection
        if (decimalPattern.matcher(input).matches()) {
            return "decimal";
        }

        // Integer detection
        if (integerPattern.matcher(input).matches()) {
            return "number";
        }
        // Date detection
        try {
            LocalDate.parse(input, DateTimeFormatter.ISO_LOCAL_DATE);
            return "date";
        } catch (DateTimeException e) {
            return "string";
        }

    }

    public static RDFNode findLongestString(RDFNode stringLiteral1, RDFNode stringLiteral2) {
        String value1 = stringLiteral1.toString();
        String value2 = stringLiteral2.toString();

        if (value1.length() > value2.length()) {
            return stringLiteral1;
        } else {
            return stringLiteral2;
        }
    }
   
    public static boolean containsDate(String inputString) {
        String[] dateFormats = {
                "\\d{4}-\\d{2}-\\d{2}",     // YYYY-MM-DD
                "\\d{2}/\\d{2}/\\d{4}",     // MM/DD/YYYY
                "\\d{2}-\\d{2}-\\d{4}",     // MM-DD-YYYY
                "\\d{2}\\.\\d{2}\\.\\d{4}", // MM.DD.YYYY
                "\\d{1,2}/\\d{1,2}/\\d{2}", // M/D/YY or MM/DD/YY
                "\\d{1,2}-\\d{1,2}-\\d{2}", // M-D-YY or MM-DD-YY
                "\\d{1,2}\\.\\d{1,2}\\.\\d{2}" // M.D.YY or MM.DD.YY
                // You can add more date formats based on your specific needs
        };

        for (String dateFormat : dateFormats) {
            Pattern pattern = Pattern.compile(dateFormat);
            Matcher matcher = pattern.matcher(inputString);
            if (matcher.find()) {
                return true;
            }
        }

        return false;
    }


    //take two literal and return the concatination of them
    public static Literal concatenateLiterals(RDFNode node1, RDFNode node2) {
            String concatenatedValue = node1.toString() + node2.toString();
            return ModelFactory.createDefaultModel().createTypedLiteral(concatenatedValue,  XSDDatatype.XSDstring);
       
    }

    //this method take two number and return mean of them as a object
    
    public RDFNode average_num(double a, double b) {
        Number r= (a + b) / 2;
        //convert r to rdfnode
        RDFNode rdfNode = ModelFactory.createDefaultModel().createTypedLiteral(r);
        return rdfNode;
    }

    //this method take two number and return max of them
    public double max_num(double a, double b) {
        if (a > b) {
            return a;
        } else {
            return b;
        }
    }
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
    public static RDFNode findMostRecentDate(RDFNode dateLiteral1, RDFNode dateLiteral2) {
        LocalDate date1 = LocalDate.parse(dateLiteral1.toString());
        LocalDate date2 = LocalDate.parse(dateLiteral2.toString());

        if (date1.isAfter(date2)) {
            return dateLiteral1;
        } else {
            return dateLiteral2;
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

    //this method take two string and return the longest one
    public RDFNode longestString(String a, String b) {
        RDFNode rdfNodea = ModelFactory.createDefaultModel().createLiteral(a);
        RDFNode rdfNodeb = ModelFactory.createDefaultModel().createLiteral(b);
        if (a.length() > b.length()) {
            return rdfNodea;
        } else {
            return rdfNodeb;
        }
    }

    //this method take two string and return the shortest one
    public String shortestString(String a, String b) {
        if (a.length() < b.length()) {
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
    public static int findMaxInt(int num1, int num2) {
        if (num1 > num2) {
            return num1;
        } else {
            return num2;
        }
    }
    public static Double findMaxDouble(Double num1, Double num2) {
        if (num1 > num2) {
            return num1;
        } else {
            return num2;
        }
    }
     public static int findMinInt(int num1, int num2) {
        if (num1 < num2) {
            return num1;
        } else {
            return num2;
        }
    }
    public static Double findMinDouble(Double num1, Double num2) {
        if (num1 < num2) {
            return num1;
        } else {
            return num2;
        }
    }
    public static Map<RDFNode, Date> convertMapToDates(Map<RDFNode, Double> nodeDoubleMap) {
        Map<RDFNode, Date> nodeDateMap = new HashMap<>();

        for (Map.Entry<RDFNode, Double> entry : nodeDoubleMap.entrySet()) {
            RDFNode node = entry.getKey();
            Double doubleValue = entry.getValue();

            // Convert the Double value to a Date assuming it represents milliseconds since epoch
            Date dateValue = new Date(doubleValue.longValue());
            
            nodeDateMap.put(node, dateValue);
        }

        return nodeDateMap;
    }
   
	public static Set<String> tokenize(String text) {
        // Split the text into words (tokens) using whitespace as the delimiter
        String[] tokens = text.split("\\s+");
        return new HashSet<>(Arrays.asList(tokens));
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
