package max;

import max.kg_merger.*;
import max.loadRDF.LoadRDF;
import org.apache.jena.rdf.model.*;

public class Main {

    public static void main(String[] args) {

        Model model1 = LoadRDF.getModel("testA.rdf");
        System.out.println("Graph A");
        model1.write(System.out, "RDF/XML-ABBREV");

        Model model2 = LoadRDF.getModel("testB.rdf");
        System.out.println("Graph B");
        model2.write(System.out, "RDF/XML-ABBREV");

        Model model3 = LoadRDF.getModel("swtor.rdf");

        /*
            Test 1: Merge two graphs
         */

        /*
        System.out.println("Test 1");

        // Create a new model to hold the merged knowledge graph
        Model mergedModel = ModelFactory.createDefaultModel();
        mergedModel = model1.union(model2);

        // Output the merged model to an RDF file or other destination
        System.out.println("Merged");
        mergedModel.write(System.out, "RDF/XML-ABBREV");
         */


        /*
            Test 2: Functional Properties
         */
        /*
        System.out.println("\nTest 2");

        int count = 0;
        int functional = 0;

        StmtIterator stmtIterator = model3.listStatements();
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

            if (FunctionalPropertyDetector.isFunctionalProperty(model3, predicate)){
                functional++;
            }
        }

        System.out.println("\nFunctionalProperties");

        StmtIterator stmtIterator2 = model3.listStatements();
        while (stmtIterator2.hasNext()) {
            Statement statement = stmtIterator2.next();
            Property predicate = statement.getPredicate();

            if (FunctionalPropertyDetector.isFunctionalProperty(model3, predicate)){
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
        */

        /*
            Test 3: Merge two graphs
         */
        Model mergedModel = Merger.mergeGraphs(model1, model2);

        // Output the merged model to an RDF file or other destination
        System.out.println("Merged");
        mergedModel.write(System.out, "RDF/XML-ABBREV");




    }
}
