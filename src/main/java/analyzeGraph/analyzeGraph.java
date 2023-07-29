package analyzeGraph;

import alsu.WeightedModel;
import org.apache.jena.rdf.model.*;
import scala.sys.Prop;

import javax.security.auth.Subject;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static max.kg_merger.FunctionalPropertyDetector.isFunctionalProperty;

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
    public static Set<Property> countNumericPredicates(Model model) {
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

    // whether the object/subject  is literal or resource
    // whether we have data type properties and how many
}







