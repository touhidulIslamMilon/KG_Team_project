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
        Set<Property> functionalProperties = new HashSet<>();

        String queryString = "SELECT DISTINCT ?p ?s (COUNT(DISTINCT ?o) AS ?count) WHERE { ?s ?p ?o . } GROUP BY ?p ?s";
        Query query = QueryFactory.create(queryString);

        for (Model model : models) {
            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                ResultSet results = qexec.execSelect();
                while (results.hasNext()) {
                    QuerySolution soln = results.nextSolution();
                    RDFNode predicateNode = soln.get("p");
                    RDFNode countNode = soln.get("count");
                    if (predicateNode.isURIResource() && countNode.isLiteral()) {
                        Property predicate = ResourceFactory.createProperty(predicateNode.asResource().getURI());
                        int count = countNode.asLiteral().getInt();
                        if (count <= 1) {
                            functionalProperties.add(predicate);
                        } else {
                            functionalProperties.remove(predicate);
                        }
                    }
                }
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

