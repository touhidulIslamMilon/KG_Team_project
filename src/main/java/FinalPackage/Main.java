package FinalPackage;

import FinalPackage.Merging.Merger;
import org.apache.jena.rdf.model.*;

import java.util.*;

public class Main {

    public static void main(String[] args) {


        Model model1 = LoadRDF.getModel("starwars1.rdf");
        System.out.println("Graph 1");
        model1.write(System.out, "RDF/XML-ABBREV");


        Model model2 = LoadRDF.getModel("starwars2.rdf");
        System.out.println("Graph 2");
        model2.write(System.out, "RDF/XML-ABBREV");

        Model model3 = LoadRDF.getModel("starwars3.rdf");
        System.out.println("Graph 3");
        model3.write(System.out, "RDF/XML-ABBREV");


        //Model model4 = LoadRDF.getModel("swtor.rdf");
        //Model model5 = LoadRDF.getModel("starwars.rdf");
        //Model model6 = LoadRDF.getModel("swg.rdf");
        System.out.println("Read");


        /*
            Test 4: Merge more than two graphs
        */
        List<Model> models = new ArrayList<>();
        models.add(model1);
        models.add(model2);
        models.add(model3);

        Map<Model, Integer> modelPriorities = new HashMap<>();
        modelPriorities.put(model1, 1);
        modelPriorities.put(model2, 2);
        modelPriorities.put(model3, 3);
        Model mergedModel1 = Merger.mergeGraphs(modelPriorities);
        System.out.println("Merged1");
        mergedModel1.write(System.out, "RDF/XML-ABBREV");

        Model mergedModel = Merger.mergeGraphs(models);
        System.out.println("Merged");
        mergedModel.write(System.out, "RDF/XML-ABBREV");


    }
}

//Old

        /*
            Test 2: Functional Properties


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

        StmtIterator stmtIterator2 = model4.listStatements();
        while (stmtIterator2.hasNext()) {
            Statement statement = stmtIterator2.next();
            Property predicate = statement.getPredicate();
            count++;

            if (FunctionalPropertyDetector.isFunctionalProperty(model3, predicate)){
                functional++;
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

        System.out.println("\nNonFunctionalProperties");

        StmtIterator stmtIterator3 = model3.listStatements();
        while (stmtIterator3.hasNext()) {
            Statement statement = stmtIterator3.next();
            Property predicate = statement.getPredicate();

            if (!FunctionalPropertyDetector.isFunctionalProperty(model3, predicate)){
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
