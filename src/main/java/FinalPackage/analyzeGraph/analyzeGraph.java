package FinalPackage.analyzeGraph;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static FinalPackage.Merging.FunctionalPropertyDetector.getFunctionalProperties;
import static FinalPackage.Merging.FunctionalPropertyDetector.isFunctionalProperty;


public class analyzeGraph {
    public static Set<Property> numberOfPredicates(Model model) {
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
        return uniquePredicates;
    }

    public static Set<Resource> numberOfSubjects(Model model) {
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
        return uniqueSubjects;
    }

    public static Set<RDFNode> numberOfObjects(Model model) {
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
        return uniqueObjects;
    }


    // counting the number of numeric predicates
    public static Set<Property> countNumericPredicates(Model model) {
        Set<Property> numericPredicates = new HashSet<>();
        StmtIterator stmtIterator = model.listStatements();

        // Regular expression to check if the object is a number, including scientific notation
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?([eE][-+]?\\d+)?");

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

        // Regular expression to match numbers
        Pattern numericPattern = Pattern.compile("-?\\d+(\\.\\d+)?");

        // Regular expression to match a variety of date formats (not exhaustive)
        Pattern datePattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2})|(\\d{2}/\\d{2}/\\d{4})|(\\d{2}-[a-zA-Z]{3}-\\d{4})|(\\d{2} [a-zA-Z]{3} \\d{4})");

        while (stmtIterator.hasNext()) {
            Statement stmt = stmtIterator.nextStatement();
            RDFNode object = stmt.getObject();

            if (object.isLiteral()) {
                String literalString = object.asLiteral().getString();
                Matcher numericMatcher = numericPattern.matcher(literalString);
                Matcher dateMatcher = datePattern.matcher(literalString);

                // Check if the literal is neither numeric nor a date
                if (!numericMatcher.matches() && !dateMatcher.matches()) {
                    stringPredicates.add(stmt.getPredicate());
                }
            }
        }

        // Get the set of date predicates
        Set<Property> datePredicates = getDatePredicates(model);

        // Remove the date predicates from the set of string predicates
        stringPredicates.removeAll(datePredicates);

        return stringPredicates;
    }

    // function which counts the number of subjects that have a resource as an object

    public static Set<Resource> countSubjectsWithResourceObjects(Model model) {
        Set<Resource> subjectsWithResourceObjects = new HashSet<>();
        StmtIterator stmtIterator = model.listStatements();

        while (stmtIterator.hasNext()) {
            Statement stmt = stmtIterator.nextStatement();
            RDFNode object = stmt.getObject();
            Property predicate = stmt.getPredicate();

            // Skip if the predicate is rdf:type
            if (predicate.equals(RDF.type)) {
                continue;
            }

            if (object.isResource()) {
                subjectsWithResourceObjects.add(stmt.getSubject());
            }
        }
        return subjectsWithResourceObjects;
    }


    private static final Pattern DATE_PATTERN = Pattern.compile(
            "(\\d{4}-\\d{2}-\\d{2})|" +  // yyyy-MM-dd
                    "(\\d{2}/\\d{2}/\\d{4})|" +  // dd/MM/yyyy
                    "(\\d{2}-[a-zA-Z]{3}-\\d{4})|" +  // dd-MMM-yyyy
                    "(\\d{2} [a-zA-Z]{3} \\d{4})|" +  // dd MMM yyyy
                    "(\\d{2}\\.[0]{1}\\d{1}\\.[0-9]{4})|" +  // dd.mm.yyyy
                    "(\\d{2}\\.[0-9]{2}\\.[0-9]{2})"  // dd.mm.yy
    );

    public static Set<Property> getDatePredicates(Model model) {
        Set<Property> datePredicates = new HashSet<>();
        StmtIterator stmtIterator = model.listStatements();

        while (stmtIterator.hasNext()) {
            Statement stmt = stmtIterator.nextStatement();
            RDFNode object = stmt.getObject();

            if (object.isLiteral()) {
                String literalString = object.asLiteral().getString();
                Matcher dateMatcher = DATE_PATTERN.matcher(literalString);

                if (dateMatcher.matches()) {
                    datePredicates.add(stmt.getPredicate());
                }
            }
        }
        return datePredicates;
    }

    //number of connected nodes
    public static List<Set<Resource>> findConnectedComponents(Model model) {
        List<Set<Resource>> connectedComponents = new ArrayList<>();
        Set<Resource> visited = new HashSet<>();

        ResIterator resIterator = model.listSubjects();

        while (resIterator.hasNext()) {
            Resource subject = resIterator.next();

            if (!visited.contains(subject)) {
                Set<Resource> component = new HashSet<>();
                dfs(model, subject, visited, component);
                connectedComponents.add(component);
            }
        }

        return connectedComponents;
    }


    private static void dfs(Model model, Resource start, Set<Resource> visited, Set<Resource> component) {
        Stack<Resource> stack = new Stack<>();
        stack.push(start);

        while (!stack.isEmpty()) {
            Resource current = stack.pop();

            if (visited.contains(current)) {
                continue;
            }

            visited.add(current);
            component.add(current);

            // Iterate over all statements where current is the subject
            StmtIterator stmtIterator = current.listProperties();
            while (stmtIterator.hasNext()) {
                Statement stmt = stmtIterator.next();
                RDFNode object = stmt.getObject();
                if (object.isResource() && !visited.contains(object.asResource())) {
                    stack.push(object.asResource());
                }
            }

            // Iterate over all statements where current is the object
            StmtIterator stmtIterator2 = model.listStatements(null, null, current);
            while (stmtIterator2.hasNext()) {
                Statement stmt = stmtIterator2.next();
                Resource subject = stmt.getSubject();
                if (!visited.contains(subject)) {
                    stack.push(subject);
                }
            }
        }
    }

    // the frequency of predicates
    public static Map<Property, Integer> countPredicateFrequencies(Model model) {
        Map<Property, Integer> predicateFrequencies = new HashMap<>();
        StmtIterator statements = model.listStatements();

        while (statements.hasNext()) {
            Property predicate = statements.nextStatement().getPredicate();
            predicateFrequencies.put(predicate, predicateFrequencies.getOrDefault(predicate, 0) + 1);
        }

        return predicateFrequencies;
    }

    // display functional properties

    public static List<Property> getFunctionalPropertiesInDescOrder(List<Model> models) {
        // Get the functional properties using the original function
        List<Property> functionalProperties = getFunctionalProperties(models);

        // Sort the list in descending alphabetical order based on their local names
        Collections.sort(functionalProperties, new Comparator<Property>() {
            public int compare(Property p1, Property p2) {
                return p2.getLocalName().compareTo(p1.getLocalName());
            }
        });

        return functionalProperties;
    }

    // return predicates that have resource as object

    public static Set<Property> countPredicatesWithResourceObjects(Model model) {
        Set<Property> predicatesWithResourceObjects = new HashSet<>();
        StmtIterator stmtIterator = model.listStatements();

        while (stmtIterator.hasNext()) {
            Statement stmt = stmtIterator.nextStatement();
            RDFNode object = stmt.getObject();
            Property predicate = stmt.getPredicate();

            // Skip if the predicate is rdf:type
            if (predicate.equals(RDF.type)) {
                continue;
            }

            if (object.isResource()) {
                predicatesWithResourceObjects.add(predicate);
            }
        }
        return predicatesWithResourceObjects;
    }


}







