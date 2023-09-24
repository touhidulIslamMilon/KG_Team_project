package FinalPackage.Merging.Strategies;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.jena.rdf.model.*;
import java.util.Set;
import java.util.regex.Matcher;
import java.text.ParseException;
import java.util.regex.Pattern;
import org.apache.jena.datatypes.xsd.XSDDatatype;

import com.google.common.collect.ListMultimap;
import com.ibm.icu.text.SimpleDateFormat;

public class HelperFunction {
    
    public String objectType(RDFNode object) {
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
            } else if (datatypeURI.equals("http://www.w3.org/2001/XMLSchema#time")) {
                return "time";
            } else {
                return detectStringType(object.toString());
            }
        }else if(object.isURIResource()){
            return detectStringType(object.toString());
        }else if(object.isResource()){
            return detectStringType(object.toString());
        }else{
            return detectStringType(object.toString());
        }
    }
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

    public LocalDate convertToDate(RDFNode node) {
        //this function convert the RDFNode to Date
        if (node == null) {
            throw new IllegalArgumentException("Input node is null");
        }
        LocalDate date = LocalDate.parse(node.toString(), DateTimeFormatter.ISO_LOCAL_DATE);
        return  date;
    }
     //this method take two string and return the longest one
    public RDFNode longestString(String a, String b) {
        RDFNode rdfNodea =ResourceFactory.createResource(a);
        RDFNode rdfNodeb = ResourceFactory.createResource(b);
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
    public static int findMaxInt(int num1, int num2) {
        if (num1 > num2) {
            return num1;
        } else {
            return num2;
        }
    }
    public RDFNode average_num(double a, double b) {
        Number r= (a + b) / 2;
        //convert r to rdfnode
        RDFNode rdfNode = ResourceFactory.createResource(String.valueOf(r));
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
    public  Double findMaxDouble(Double num1, Double num2) {
        if (num1 > num2) {
            return num1;
        } else {
            return num2;
        }
    }
     public  int findMinInt(int num1, int num2) {
        if (num1 < num2) {
            return num1;
        } else {
            return num2;
        }
    }
    
    public  Double findMinDouble(Double num1, Double num2) {
        if (num1 < num2) {
            return num1;
        } else {
            return num2;
        }
    }
     public  boolean containsDate(String inputString) {
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


    public  double calculateJaccardDistance(String str1, String str2) {
        // Convert the input strings into sets of characters
        HashSet<Character> set1 = new HashSet<>();
        HashSet<Character> set2 = new HashSet<>();

        for (char c : str1.toCharArray()) {
            set1.add(c);
        }

        for (char c : str2.toCharArray()) {
            set2.add(c);
        }

        // Calculate the intersection of the sets
        HashSet<Character> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        // Calculate the union of the sets
        HashSet<Character> union = new HashSet<>(set1);
        union.addAll(set2);

        // Calculate the Jaccard distance
        if (union.size() == 0) {
            // Handle the case where both sets are empty
            return 1.0;
        } else {
            return 1.0 - (double) intersection.size() / union.size();
        }
    }
    //create a function that take two sting and return jaccard distance




    private static Set<String> tokenize(String str) {
        String[] tokens = str.split("\\s+"); // Tokenize by whitespace
        Set<String> set = new HashSet<>();
        for (String token : tokens) {
            set.add(token);
        }
        return set;
    }
    private static double calculateIntersectionSize(Set<String> set1, Set<String> set2) {
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        return intersection.size();
    }
    public  RDFNode findLongestString(RDFNode stringLiteral1, RDFNode stringLiteral2) {
        String value1 = stringLiteral1.toString();
        String value2 = stringLiteral2.toString();

        if (value1.length() > value2.length()) {
            return stringLiteral1;
        } else {
            return stringLiteral2;
        }
    }
     public RDFNode findShortestString(RDFNode first, RDFNode second) {
        String value1 = first.toString();
        String value2 = second.toString();
        
        if (value1.length() < value2.length()) {
            return first;
        } else {
            return second;
        }   
    }
    public  Date intToDate(int timestampInSeconds) throws ParseException {
        long timestampInMillis = (long) timestampInSeconds * 1000;
        return new Date(timestampInMillis);
    }
   
    public  int dateToInt(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date=null;
        date = sdf.parse(dateString);
        return (int) (date.getTime() / 1000); // Convert milliseconds to seconds
    }

    //take two literal and return the concatination of them
    public  Literal concatenateLiterals(RDFNode node1, RDFNode node2) {
            String concatenatedValue = node1.toString() + node2.toString();
            return ModelFactory.createDefaultModel().createTypedLiteral(concatenatedValue);
       
    }
    public char[] min_num(Double var1, Double var2) {
        if (var1 < var2) {
            return var1.toString().toCharArray();
        } else {
            return var2.toString().toCharArray();
        }
    }
    public RDFNode getMedianValue(ListMultimap<RDFNode, Integer> objects, Resource subject, Property predicate) {
         if (objects == null || objects.isEmpty()) {
            System.out.println("Input map is null or empty");
            throw new IllegalArgumentException("Input map is null or empty");
        }
        Model model = ModelFactory.createDefaultModel();
        List<Double> allValues = new ArrayList<>();

        // Combine all values from the ListMultimap into a single list
        for (RDFNode value : objects.keys()) {
            try {
                allValues.add(Double.parseDouble(value.toString()) );
            } catch (Exception e) {
                ManualReviewResolutionStrategy strategy1 = new ManualReviewResolutionStrategy();
                System.out.println("The value is not Number");
                return strategy1.resolveConflict(objects, subject, predicate);
            }
        }
        // Sort the list in ascending order
        Collections.sort(allValues);
        System.out.println(allValues.toString());
        int size = objects.size();
        System.out.println("size: "+size);
        if (size % 2 == 1) {
            return ResourceFactory.createResource(String.valueOf(allValues.get(size / 2)));
        } else {
            Double middle1 = allValues.get(size / 2 - 1);
            Double middle2 = allValues.get(size / 2);
            try {
                return ResourceFactory.createResource(String.valueOf( (Double)(middle1 + middle2) / 2));
            } catch (Exception e) {
                System.out.println("The value is not Integer");
                return ResourceFactory.createResource(String.valueOf(allValues.get(size / 2)));
            }
        }
    }
   
    public RDFNode findMostOldDate(RDFNode first, RDFNode second) throws ParseException{
        LocalDate date1 = LocalDate.parse(first.toString());
        LocalDate date2 = LocalDate.parse(second.toString());
        System.out.println(date2.toString());
        System.out.println(date1.toString());
        if (date1.isBefore(date2)) {
            
            return first;
        } else {
            return second;
        }
    }
    public  RDFNode findMostRecentDate(RDFNode dateLiteral1, RDFNode dateLiteral2) throws ParseException{
        
        LocalDate date1 = LocalDate.parse(dateLiteral1.toString());
        LocalDate date2 = LocalDate.parse(dateLiteral2.toString());

        if (date1.isAfter(date2)) {
            return dateLiteral1;
        } else {
            return dateLiteral2;
        }
    }
    public RDFNode findMean(RDFNode first, RDFNode second) throws ParseException {
        double mean = (Double.parseDouble(first.toString()) + Double.parseDouble(second.toString())) / 2;
        return ResourceFactory.createResource(String.valueOf(mean));
    }
}
