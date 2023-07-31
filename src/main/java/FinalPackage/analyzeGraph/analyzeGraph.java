package FinalPackage.analyzeGraph;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static FinalPackage.Merging.FunctionalPropertyDetector.isFunctionalProperty;


public class analyzeGraph {
    public static int numberOfPredicates(Model model) {
        // Create a set to store unique predicates
        Set<Property> uniquePredicates = new HashSet<>();

        // Create a StmtIterator to iterate over each statement in the model
        StmtIterator stmtIterator = model.listStatements();

        // For each statement, add its predicate to the set
        while (stmtIterator.hasNext()) {
            Statement stmt = stmtIterator.next();
            uniquePredicates.add(stmt.getPredicate());
        }

        // The size of the set is the number of unique predicates
        return uniquePredicates.size();
    }

    public static int numberOfSubjects(Model model) {
        // Create a set to store unique subjects
        Set<Resource> uniqueSubjects = new HashSet<>();

        // Create a StmtIterator to iterate over each statement in the model
        StmtIterator stmtIterator = model.listStatements();

        // For each statement, add its subject to the set
        while (stmtIterator.hasNext()) {
            Statement stmt = stmtIterator.next();
            uniqueSubjects.add(stmt.getSubject());
        }
        // The size of the set is the number of unique subjects
        return uniqueSubjects.size();
    }

    public static int numberOfObjects(Model model) {
        // Create a set to store unique objects
        Set<RDFNode> uniqueObjects = new HashSet<>();

        // Create a StmtIterator to iterate over each statement in the model
        StmtIterator stmtIterator = model.listStatements();

        // For each statement, add its object to the set
        while (stmtIterator.hasNext()) {
            Statement stmt = stmtIterator.next();
            uniqueObjects.add(stmt.getObject());
        }

        // The size of the set is the number of unique objects
        return uniqueObjects.size();
    }

    public static int countFunctionalProperties(Model model) {
        // Create a set to store unique functional properties
        Set<Property> uniqueFunctionalProperties = new HashSet<>();

        // Create a StmtIterator to iterate over each statement in the model
        StmtIterator stmtIterator = model.listStatements();

        // For each statement, check if its predicate is functional and unique
        while (stmtIterator.hasNext()) {
            Property property = stmtIterator.next().getPredicate();

            if (isFunctionalProperty(model, property) && !uniqueFunctionalProperties.contains(property)) {
                uniqueFunctionalProperties.add(property);
            }
        }

        // The size of the set is the number of unique functional properties
        return uniqueFunctionalProperties.size();
    }

    // counting the number of numeric predicates
    public static Set<Property> countNumericObjects(Model model) {
        Set<Property> numericPredicates = new HashSet<>();
        StmtIterator stmtIterator = model.listStatements();
        // Regular expression to check if the object is a number
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

        while (stmtIterator.hasNext()) {
            Statement stmt = stmtIterator.nextStatement();
            RDFNode object = stmt.getObject();

            if (object.isLiteral()) {
                Matcher matcher = pattern.matcher(object.asLiteral().getString());
                if (matcher.matches()) {
                    numericPredicates.add(stmt.getPredicate());
                }
            }
        }
        return numericPredicates;
    }

    // counting the number of literals


    public static Set<Property> countStringPredicates(Model model) {
        Set<Property> stringPredicates = new HashSet<>();
        StmtIterator stmtIterator = model.listStatements();

        // Regular expression patterns to exclude numeric and normalized date literals
        Pattern numericPattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        Pattern datePattern = Pattern.compile("\\d{4}_\\d{2}_\\d{2}");  // This matches "yyyy_MM_dd"

        while (stmtIterator.hasNext()) {
            Statement stmt = stmtIterator.nextStatement();
            RDFNode object = stmt.getObject();

            if (object.isLiteral()) {
                String literalString = object.asLiteral().getString();

                // Check if the literal is neither numeric nor a normalized date
                if (!numericPattern.matcher(literalString).matches() &&
                        !datePattern.matcher(literalString).matches()) {
                    stringPredicates.add(stmt.getPredicate());
                }
            }
        }

        return stringPredicates;
    }

    // function which counts the number of subjects that have a resource as an object

    public static int countSubjectsWithResourceObjects(Model model) {
        Set<Resource> subjectsWithResourceObjects = new HashSet<>();
        StmtIterator stmtIterator = model.listStatements();

        while (stmtIterator.hasNext()) {
            Statement stmt = stmtIterator.nextStatement();
            RDFNode object = stmt.getObject();

            if (object.isResource()) {
                subjectsWithResourceObjects.add(stmt.getSubject());
            }
        }
        return subjectsWithResourceObjects.size();
    }

    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{2}_\\d{2}_\\d{4}");

    public static Set<Property> getDatePredicates(Model model) {
        Set<Property> datePredicates = new HashSet<>();
        StmtIterator stmtIterator = model.listStatements();

        while (stmtIterator.hasNext()) {
            Statement stmt = stmtIterator.nextStatement();
            RDFNode object = stmt.getObject();

            if (object.isLiteral() && DATE_PATTERN.matcher(object.asLiteral().getString()).matches()) {
                datePredicates.add(stmt.getPredicate());
            }
        }

        return datePredicates;
    }


    // print out the functional properties !!!



    // whether the object/subject  is literal or resource
    // whether we have data type properties and how many
}







