package FinalPackage.Merging;

import org.apache.jena.ontology.*;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.ontology.FunctionalProperty;

import java.util.*;

public class FunctionalPropertyDetector {

    /*
        Build a SPARQL query to find all distinct predicates

     */
    public static List<Property> getFunctionalPredicates(List<Model> models) {
        List<Set<Property>> predicateSets = new ArrayList<>();

        String queryString = "SELECT DISTINCT ?p WHERE { ?s ?p ?o . }";
        Query query = QueryFactory.create(queryString);

        // Find the set of predicates for each dataset separately
        for (Model model : models) {
            Set<Property> predicateSet = new HashSet<>();
            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                ResultSet results = qexec.execSelect();
                while (results.hasNext()) {
                    QuerySolution soln = results.nextSolution();
                    RDFNode predicateNode = soln.get("p");
                    if (predicateNode.isURIResource()) {
                        String predicateURI = predicateNode.asResource().getURI();
                        predicateSet.add(ResourceFactory.createProperty(predicateURI));
                    }
                }
            }
            predicateSets.add(predicateSet);
        }

        // Find the intersection of predicate sets from all datasets
        Set<Property> commonPredicates = new HashSet<>(predicateSets.get(0));
        for (int i = 1; i < predicateSets.size(); i++) {
            commonPredicates.retainAll(predicateSets.get(i));
        }

        // Find functional predicates with a count of 1 in each dataset
        Map<Property, Integer> predicateCountMap = new HashMap<>();
        queryString = "SELECT ?p (COUNT(DISTINCT ?o) AS ?count) WHERE { ?s ?p ?o . } GROUP BY ?p";
        query = QueryFactory.create(queryString);
        for (Model model : models) {
            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                ResultSet results = qexec.execSelect();
                while (results.hasNext()) {
                    QuerySolution soln = results.nextSolution();
                    RDFNode predicateNode = soln.get("p");
                    RDFNode countNode = soln.get("count");
                    if (predicateNode.isURIResource() && countNode.isLiteral()) {
                        String predicateURI = predicateNode.asResource().getURI();
                        if (commonPredicates.contains(ResourceFactory.createProperty(predicateURI))) {
                            int count = countNode.asLiteral().getInt();
                            predicateCountMap.put(ResourceFactory.createProperty(predicateURI), predicateCountMap.getOrDefault(predicateURI, 0) + count);
                        }
                    }
                }
            }
        }

        Set<Property> functionalProperties = new HashSet<>();
        for (Property predicate : predicateCountMap.keySet()) {
            int totalCount = predicateCountMap.get(predicate);
            if (commonPredicates.contains(predicate) && totalCount <= models.size()) {
                functionalProperties.add(predicate);
            }
        }

        return new ArrayList<>(functionalProperties);
    }


    // My function worked properly only with this function below
    public static boolean isFunctionalProperty(Model model, Property property) {
        // Iterate over all subjects in the model.
        ResIterator subjects = model.listSubjects();
        while (subjects.hasNext()) {
            Resource subject = subjects.next();

            // For each subject, count the number of distinct objects linked by the property.
            NodeIterator objects = model.listObjectsOfProperty(subject, property);
            int objectCount = 0;
            while (objects.hasNext()) {
                objects.next();
                objectCount++;

                // If a subject is linked to more than one object by the property, it's not functional.
                if (objectCount > 1) {
                    return false;
                }
            }
        }

        // If no subject is linked to more than one object by the property, it is functional.
        return true;
    }

    // Method to check if a property is functional for a single model

    public static boolean isFunctionalProperty(Model model, Resource subject, Property predicate) {
        RDFNode commonObject = null;

        StmtIterator iter = model.listStatements(subject, predicate, (RDFNode) null);
        if (iter.hasNext()) {
            commonObject = iter.nextStatement().getObject();
            while (iter.hasNext()) {
                RDFNode object = iter.nextStatement().getObject();
                if (!commonObject.equals(object)) {
                    return false; // Property is not functional for this model
                }
            }
        }

        return true; // Property is functional for this model
    }



}

