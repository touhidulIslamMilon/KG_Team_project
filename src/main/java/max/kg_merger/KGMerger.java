package max.kg_merger;

import org.apache.jena.ontology.FunctionalProperty;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.iterator.ExtendedIterator;


public class KGMerger {

    public static void main(String[] args) {
        // Create two RDF models representing the knowledge graphs
        Model model1 = ModelFactory.createDefaultModel();
        Model model2 = ModelFactory.createDefaultModel();
        Model model3 = ModelFactory.createDefaultModel();
        Model model4 = ModelFactory.createDefaultModel();

        // Test 1 Merge two simple graphs
        // Load data into the models from RDF files or other sources
        System.out.println("Test 1");

        model1.read("testA.rdf");
        System.out.println("Graph A");
        model1.write(System.out, "RDF/XML-ABBREV");

        model2.read("testB.rdf");
        System.out.println("Graph B");
        model2.write(System.out, "RDF/XML-ABBREV");

        // Create a new model to hold the merged knowledge graph
        Model mergedModel = ModelFactory.createDefaultModel();
        mergedModel = model1.union(model2);

        // Output the merged model to an RDF file or other destination
        System.out.println("Merged");
        mergedModel.write(System.out, "RDF/XML-ABBREV");



        System.out.println("\nTest 2");
        model3.read("testProperties.rdf");

        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, model3);
        ExtendedIterator<OntProperty> propertyIterator = ontModel.listAllOntProperties();
        while (propertyIterator.hasNext()) {
            OntProperty property = propertyIterator.next();
            System.out.println("Property: " + property.getLocalName() + " " + property.getURI());
            if (FunctionalPropertyDetector.isFunctional(ontModel, property)){
                System.out.println("yes");
            }
        }

        ExtendedIterator<FunctionalProperty> propIt2 = ontModel.listFunctionalProperties();
        while (propIt2.hasNext()) {
            FunctionalProperty property = propIt2.next();
            System.out.println("Functional Property: " + property.getLocalName() + " " + property.getURI());
        }

        System.out.println("\nTest 3");
        model4.read("swtor.rdf");
        int count = 0;
        int functional = 0;

        StmtIterator stmtIterator = model4.listStatements();
        while (stmtIterator.hasNext()) {
            Statement statement = stmtIterator.next();
            count ++;
            Property predicate = statement.getPredicate();
            Resource subject = statement.getSubject();
            System.out.println("Subject: " + subject.getURI() + "\nPredicate: " + predicate.getURI());
            RDFNode object = statement.getObject();
            if (object.isResource()) {
                Resource resource = object.asResource();
                System.out.println("Object (Resource): " + resource.getURI() + "\n");
            } else if (object.isLiteral()) {
                Literal literal = object.asLiteral();
                System.out.println("Object (Literal): " + literal.getLexicalForm() + "\n");
            }

            if (isFunctionalProperty(model4, predicate)){
                functional++;
            }
        }

        System.out.println("\nFunctionalProperties");

        StmtIterator stmtIterator2 = model4.listStatements();
        while (stmtIterator2.hasNext()) {
            Statement statement = stmtIterator2.next();
            Property predicate = statement.getPredicate();

            if (isFunctionalProperty(model4, predicate)){
                Resource subject = statement.getSubject();
                RDFNode object = statement.getObject();
                if (object.isResource()) {
                    Resource resource = object.asResource();
                    System.out.println("Subject: " + subject.getURI() + "\nPredicate: " + predicate.getURI());
                    System.out.println("Object (Resource): " + resource.getURI() + "\n");
                } else if (object.isLiteral()) {
                    Literal literal = object.asLiteral();
                    System.out.println("Subject: " + subject.getURI() + "\nPredicate: " + predicate.getURI());
                    System.out.println("Object (Literal): " + literal.getLexicalForm() + "\n");
                }
            }

        }

        System.out.println("All pairs: " + count);
        System.out.println("Functional pairs: " + functional);


    }

    private static boolean isFunctionalProperty(Model model, Property property) {
        StmtIterator stmtIterator = model.listStatements(null, property, (RDFNode) null);
        int objectCount = 0;
        while (stmtIterator.hasNext()) {
            stmtIterator.next();
            objectCount++;
            if (objectCount > 1) {
                return false;
            }
        }
        return true;
    }

}

