
//the is a fusion strategy class with multiple method take two object and return one
//boilerplate code for class
package islam.merger;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.riot.RDFDataMgr;

import java.io.InputStream;
import java.util.Date;

public class fusionStrategy {

    public RDFNode resolvePredicateConflict(RDFNode object, RDFNode object2) {
        //check if object is string or not
        String type = objectType(object);
        String type2 = objectType(object2);
        if(type=="date" || type2=="date"){
            return recentDate(object, object2);
        }else if(type=="number" || type2=="number"){
            return average_num(Double.parseDouble(object.toString()), Double.parseDouble(object2.toString()));
        }else if(type=="uri" || type2=="uri"){
            return object;
        }else if(type=="unknown" || type2=="unknown"){
            return longestString(object.toString(), object2.toString());
        }
        return concatinateString(object, object);
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
        
        if (object.isURIResource()) {
            return "uri";
        } else if (isDate(object.toString())) {
            return "date";
            //cheak if a object contain number or not
        } else if (object.toString().matches("-?\\d+(\\.\\d+)?")) {
            return "number";
        } else if (object.isLiteral()) {
            return "string";

        } else {
            return "unknown";
        }
    }
    //cheak if a object contain date or not
    private boolean isDate(String object) {
    
        try {
            Date date = new Date(object);
            return true;
        } catch (Exception e) {
            return false;
        }
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

    public RDFNode uresolvePredicateConflic(RDFNode rdfNode, RDFNode object) {
        return null;
    }

    
    
}
