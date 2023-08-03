package FinalPackage.analyzeGraph;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Pattern datePattern = Pattern.compile("\\d{2}.\\d{2}.\\d{4}");  // This matches "yyyy-MM-dd"

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
            } else if (object.isResource()) {
                stringPredicates.add(stmt.getPredicate());
            }
        }

        return stringPredicates;
    }


    // function which counts the number of subjects that have a resource as an object

    public static Set<Resource> countSubjectsWithResourceObjects(Model model) {
        Set<Resource> subjectsWithResourceObjects = new HashSet<>();
        StmtIterator stmtIterator = model.listStatements();

        while (stmtIterator.hasNext()) {
            Statement stmt = stmtIterator.nextStatement();
            RDFNode object = stmt.getObject();

            if (object.isResource()) {
                subjectsWithResourceObjects.add(stmt.getSubject());
            }
        }
        return subjectsWithResourceObjects;
    }

    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{2}.\\d{2}.\\d{4}");

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

    // number of predicates per subject
    public static Map<Resource, Integer> countPredicatesPerSubject(Model model) {
        Map<Resource, Integer> predicateCountPerSubject = new HashMap<>();

        StmtIterator stmtIterator = model.listStatements();
        while (stmtIterator.hasNext()) {
            Statement stmt = stmtIterator.nextStatement();
            Resource subject = stmt.getSubject();
            predicateCountPerSubject.put(subject, predicateCountPerSubject.getOrDefault(subject, 0) + 1);
        }

        // Create a sorted map from the original one
        Map<Resource, Integer> sortedMap = new LinkedHashMap<>();
        predicateCountPerSubject.entrySet().stream()
                .sorted(Map.Entry.<Resource, Integer>comparingByValue().reversed())
                .forEachOrdered(entry -> sortedMap.put(entry.getKey(), entry.getValue()));

        return sortedMap;
    }

    //Distribution of types of the nodes (How many statements do we have with “Person”, “Animal” types
    // (from the most to the least frequent types))
    public static Map<Resource, Integer> countTypes(Model model) {
        Map<Resource, Integer> typeCount = new HashMap<>();
        StmtIterator stmtIterator = model.listStatements();

        while (stmtIterator.hasNext()) {
            Statement stmt = stmtIterator.nextStatement();

            if (stmt.getPredicate().equals(RDF.type)) {
                Resource type = stmt.getObject().asResource();

                if (typeCount.containsKey(type)) {
                    typeCount.put(type, typeCount.get(type) + 1);
                } else {
                    typeCount.put(type, 1);
                }
            }
        }

        return typeCount;
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

    // the frequency of subjects
    public static Map<Resource, Integer> countSubjectFrequencies(Model model) {
        Map<Resource, Integer> subjectFrequencies = new HashMap<>();
        StmtIterator statements = model.listStatements();

        while (statements.hasNext()) {
            Resource subject = statements.nextStatement().getSubject();
            subjectFrequencies.put(subject, subjectFrequencies.getOrDefault(subject, 0) + 1);
        }

        return subjectFrequencies;
    }

    //counting the most frequent objects for the most frequent predicates
    public static Map<RDFNode, Integer> countObjectFrequencies(Model model, Property predicate) {
        Map<RDFNode, Integer> objectFrequencies = new HashMap<>();
        StmtIterator statements = model.listStatements(null, predicate, (RDFNode) null);

        while (statements.hasNext()) {
            RDFNode object = statements.nextStatement().getObject();
            objectFrequencies.put(object, objectFrequencies.getOrDefault(object, 0) + 1);
        }

        return objectFrequencies;
    }

    // print out the functional properties !!!



    // whether the object/subject  is literal or resource
    // whether we have data type properties and how many
}







