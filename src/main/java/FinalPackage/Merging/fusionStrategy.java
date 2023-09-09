
//the is a fusion strategy class with multiple method take two object and return one
//boilerplate code for class
package FinalPackage.Merging;

import org.apache.avro.LogicalTypes.Decimal;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.jena.riot.RDFDataMgr;

import java.io.InputStream;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class fusionStrategy {

    public RDFNode fusion(Set<RDFNode> allObjects, Resource subject, Property predicate) {
        
       if(allObjects.size()>2){
            return majorityVote(allObjects);
        }else{
            //iterate over all objects and check if they are literals
            Iterator  allObjectsiterate=  allObjects.iterator();
            StringBuilder fusedValue = new StringBuilder();

            for (RDFNode node : allObjects) {
                
                fusedValue.append(node);
            }
            return null;
        }
	}
    public static RDFNode majorityVote(Set<RDFNode> nodes) {
        Map<String, Integer> countMap = new HashMap<>();

        // Count occurrences of each RDFNode (considering them as strings)
        for (RDFNode node : nodes) {
            String nodeValue = node.toString(); // Convert the RDFNode to a string value
            countMap.put(nodeValue, countMap.getOrDefault(nodeValue, 0) + 1);
        }

        // Find the RDFNode with the highest count (majority vote)
        int maxCount = 0;
        String majorityNodeValue = null;
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                majorityNodeValue = entry.getKey();
            }
        }

        // Return the majority vote as an RDFNode
        return nodes.iterator().next().getModel().createTypedLiteral(majorityNodeValue);
    }
    
    public Literal fuseLiteral(Literal literal1, Literal literal2) {
        String type1 = literal1.getDatatypeURI();
        String type2 = literal2.getDatatypeURI();
        if(type1==type2){
                switch (type1) {
                case "string":
                    //TODO - This is where the new literal is created
                        // return concatination of two literals
                        Literal concatinate = concatenateLiterals(literal1, literal2);
                        //return longest literal
                        Literal longest = findLongestString(literal1, literal2);
                        //return the literal with the most capital letters
                        return concatinate;  
                case "number":
                     try{
                        //if both are number the return the average
                        Double num1 = literal1.getDouble();
                        Double num2 = literal2.getDouble();
                        Double average = (num1+num2)/2;
                        Literal averageLiteral = ModelFactory.createDefaultModel().createTypedLiteral(average,  XSDDatatype.XSDdecimal);
                        //TODO - This is where the new literal is created
                        // we will return average as a literal
                        return averageLiteral;
                    }catch(Exception e){
                        System.out.println("Not a number");
                        //TODO - This is where the new literal is created
                        concatenateLiterals(literal1, literal2);
                        //return longest literal
                        findLongestString(literal1, literal2);
                        //return the literal with the most capital letters
                        return concatenateLiterals(literal1, literal2);  
                    }
                case "text":
                    //TODO - This is where the new literal is created
                        concatenateLiterals(literal1, literal2);
                        //return longest literal
                        findLongestString(literal1, literal2);
                        //return the literal with the most capital letters
                        return concatenateLiterals(literal1, literal2);
                case "decimal":
                    try{
                        //if both literal are decimal then return the average
                        Double num1 = literal1.getDouble();
                        Double num2 = literal2.getDouble();
                        Double average = (num1+num2)/2;
                        Literal averageLiteral = ModelFactory.createDefaultModel().createTypedLiteral(average,  XSDDatatype.XSDdecimal);
                        //TODO - This is where the new literal is created
                        // we will return average as a literal
                        return averageLiteral;
                        
                    }catch(Exception e){

                        //TODO - This is where the new literal is created
                       concatenateLiterals(literal1, literal2);
                        //return longest literal
                        findLongestString(literal1, literal2);
                        //return the literal with the most capital letters
                        return concatenateLiterals(literal1, literal2);
                    }
                    
                case "integer":
                    try{
                        //if both literal are decimal then return the average
                        Double num1 = literal1.getDouble();
                        Double num2 = literal2.getDouble();
                        Double average = (num1+num2)/2;
                        Literal averageLiteral = ModelFactory.createDefaultModel().createTypedLiteral(average,  XSDDatatype.XSDinteger);
                        //TODO - This is where the new literal is created
                        // we will return average as a literal
                        return averageLiteral;
                        
                    }catch(Exception e){

                        //TODO - This is where the new literal is created
                       concatenateLiterals(literal1, literal2);
                        //return longest literal
                        findLongestString(literal1, literal2);
                        //return the literal with the most capital letters
                        return concatenateLiterals(literal1, literal2);
                    }
                case "boolean":
                    //TODO - This is where the new literal is created
                    
                        findLongestString(literal1, literal2);
                        //return the literal with the most capital letters
                        return concatenateLiterals(literal1, literal2);
                
                case "date":
                    //if both are date then return the recent date
                    if(containsDate(literal1.toString()) && containsDate(literal2.toString())){
                        Literal recentDate = findMostRecentDate(literal1, literal2);
                        Literal averageLiteral = ModelFactory.createDefaultModel().createTypedLiteral(recentDate,  XSDDatatype.XSDdate);
                        //TODO - This is where the new literal is created
                        // we will return recentDate as a literal
                        return averageLiteral;
                    }else{

                        //TODO - This is where the new literal is creat
                        findLongestString(literal1, literal2);
                        //return the literal with the most capital letters
                        return concatenateLiterals(literal1, literal2);
                    }
                case "dateTime":
                    Literal recentDate = findMostRecentDate(literal1, literal2);
                    Literal recentDateLiteral = ModelFactory.createDefaultModel().createTypedLiteral(recentDate,  XSDDatatype.XSDdateTime);
                    //TODO - This is where the new literal is created
                        // we will return recentDate as a literal
                    return recentDateLiteral;
                default:
                    //TODO - This is where the new literal is creat
                    findLongestString(literal1, literal2);
                    //return the literal with the most capital letters
                    return concatenateLiterals(literal1, literal2);
            }
        }else{
            System.out.println("Not same type");
            //TODO - This is where the new literal is creat
             // return concatination of two literals
            System.out.println("Unknown type");
            return concatenateLiterals(literal1, literal2);
        }
        

    }


    public static Literal findLongestString(Literal stringLiteral1, Literal stringLiteral2) {
        String value1 = stringLiteral1.getString();
        String value2 = stringLiteral2.getString();

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

    public static boolean containsDecimal(String inputString) {
        // Regular expression to match decimal numbers
        String regex = "[-+]?\\d*\\.\\d+";
        return inputString.matches(regex);
    }


    

    //this method take two object and return the concatination of them with coma as a rdfnode
    public RDFNode concatinateString(RDFNode object, RDFNode object2) {
        //return concatination of two string as a rdfnode
        String r = object.toString() + "," + object2.toString();
        //convert r to rdfnode
        RDFNode rdfNode = ModelFactory.createDefaultModel().createLiteral(r);
        return rdfNode;
    }
    //get and string and return if it contain date
   
    
    private String objectType(RDFNode object) {
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
            return "resource";
        }else{
            return "unknown";
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
    
    public static String getLiteralType(Literal literal) {
        String datatypeURI = literal.getDatatypeURI();

        if (datatypeURI == null) {
            return "string";
        } else if (datatypeURI.equals("http://www.w3.org/2001/XMLSchema#string")) {
            return "text";
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
            return "unknown";
        }
    }

    //take two literal and return the concatination of them
    public static Literal concatenateLiterals(Literal literal1, Literal literal2) {
            String concatenatedValue = literal1.getString() + literal2.getString();
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
    public static Literal findMostRecentDate(Literal dateLiteral1, Literal dateLiteral2) {
        LocalDate date1 = LocalDate.parse(dateLiteral1.getString());
        LocalDate date2 = LocalDate.parse(dateLiteral2.getString());

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

    public void fuseLiterals(Literal literal1, Literal literal2) {
    }

	
    
}
