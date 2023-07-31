package max;

import max.kg_merger.*;
import max.loadRDF.LoadRDF;
import org.apache.jena.rdf.model.*;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        Model model1 = LoadRDF.getModel("graph1.rdf");
        System.out.println("Graph 1");
        model1.write(System.out, "RDF/XML-ABBREV");

        Model model2 = LoadRDF.getModel("graph2.rdf");
        System.out.println("Graph 2");
        model2.write(System.out, "RDF/XML-ABBREV");

        Model model3 = LoadRDF.getModel("graph3.rdf");
        System.out.println("Graph 3");
        model3.write(System.out, "RDF/XML-ABBREV");

        Model model4 = LoadRDF.getModel("swtor.rdf");

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

        Model mergedModel = Merger.mergeGraphs(Merger.mergeGraphs(model1, model2), model1);

        // Output the merged model to an RDF file or other destination
        System.out.println("Merged");
        mergedModel.write(System.out, "RDF/XML-ABBREV");
        */
        /*
            Test 4: Merge more than two graphs
         */
        List<Model> models = new ArrayList<>();
        models.add(model1);
        models.add(model2);
        models.add(model3);


        Model mergedModel = Merger.mergeGraphs(models);
        System.out.println("Merged");
        mergedModel.write(System.out, "RDF/XML-ABBREV");


    }
}
