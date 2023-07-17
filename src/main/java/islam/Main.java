package islam;

import islam.merger.ConflictManagement;
import islam.merger.InOu;
import islam.merger.KnowledgeGraphFusion;
import islam.merger.functionalProperty;
import max.loadRDF.LoadRDF;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.springframework.util.StopWatch;

import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;

public class Main {
    
    public static void main(String[] args){

        //measure time of execution
        //will help to increase the efficiency of the code
        StopWatch watch = new StopWatch();
        watch.start();

        Model model1 = ModelFactory.createDefaultModel();
        Model model2 = ModelFactory.createDefaultModel();
        // use the RDFDataMgr to find the input file
        InOu inOu = new InOu();
        
        model1 = inOu.inputModel("/Users/mdtouhidulislam/Documents/testA.rdf");
        model2 = inOu.inputModel("/Users/mdtouhidulislam/Documents/testB.rdf");
        

        System.out.println( "#testA" );
        // write it to standard out
        model1.write(System.out,"TURTLE");

        System.out.println( "#testB" );
        model2.write(System.out,"TURTLE");

        System.out.println( "#FUSED models" );
        //Model fusedModels = modelA.union(modelB);
        //create a custom union model
        Model fusedModels = ModelFactory.createDefaultModel();

        //fusedModels.write(System.out);


        OntModel ontModel2 = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, model2);
        OntModel ontModel1 = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, model1);
        //Model mergedModel = mergedModel(model1,model2);


        System.out.println( "#Conflict models" );
        //mergedModel.write(System.out,"TURTLE");

        StmtIterator iter1 = model1.listStatements();
        StmtIterator iter2 = model2.listStatements();
        Model model = LoadRDF.getModel("swtor.rdf");

        
        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, model);
        functionalProperty functionalProperty = new functionalProperty();
        //OntModel listfunctionalProperty = functionalProperty.getFunctionalProperty(model);


        //marge functional marger knowledge graph
        KnowledgeGraphFusion fusion = new KnowledgeGraphFusion();
        //SOntModel fusedKnowledgeGraph = fusion.mergeKnowledgeGraphslength(ontModel1, ontModel2);
        OntModel fusedKnowledgeGraph = fusion.mergeKnowledgeGraphslink(ontModel1, ontModel2);
        
        //OntModel fusedKnowledgeGraph = fusion.mergeKnowledgeGraphsrule4(ontModel1, ontModel2);
        //OntModel fusedKnowledgeGraph = fusion.mergeKnowledgeGraphs(ontModel1, ontModel2);
        //OntModel fusedKnowledgeGraph = fusion.mergeKnowledgeGraphsrule2(ontModel1, ontModel2);

        // Create a property priority map (higher priority has a lower numerical value)
      /*   Map<Property, Integer> propertyPriority = new HashMap<>();
        propertyPriority.put(ontModel1.getProperty("my:property1"), 1);
        propertyPriority.put(ontModel1.getProperty("my:property2"), 2);
        propertyPriority.put(ontModel2.getProperty("my:property1"), 1);
        propertyPriority.put(ontModel2.getProperty("my:property2"), 2);
        OntModel fusedKnowledgeGraph = fusion.mergeKnowledgeGraphspriority(ontModel1, ontModel2,propertyPriority); */






        // Print the fused knowledge graph
       /*  OntModel knowledgeGraph1 = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        Model model11 = RDFDataMgr.loadModel("path/to/knowledgeGraph1.ttl");
        knowledgeGraph1.addSubModel(model11);

        // Load knowledgeGraph2 from Turtle file
        OntModel knowledgeGraph2 = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        Model model21 = RDFDataMgr.loadModel("path/to/knowledgeGraph2.ttl");
        knowledgeGraph2.addSubModel(model21);

        // Perform knowledge graph fusion
        OntModel fusedKnowledgeGrap = fusion.mergeKnowledgeGraphs(knowledgeGraph1, knowledgeGraph2); */

        // Print the fused knowledge graph in Turtle format
        fusedKnowledgeGraph.write(System.out, "TURTLE");
        




        //StmtIterator marge = mergedModel.listStatements();

        //print all statements in marge
        /* while (iter1.hasNext()){
            Statement stmt1 = iter1.nextStatement();

            // we define not
            //Property invfunctionalproperty = ontModel.getInverseFunctionalProperty(stmt1.getSubject().getURI());
            //System.out.println("Inverse Functional property: "+ invfunctionalproperty);
            while (marge.hasNext()){
                Statement stmt2 = marge.nextStatement();
                if (stmt1.getSubject().hasURI(stmt2.getSubject().getURI())){
                    System.out.println("Su1: "+ stmt1);
                    break;
                }
            }
            fusedModels.add(stmt1.getSubject(), stmt1.getPredicate(), stmt1.getObject());

        }


        while (iter2.hasNext()){
            Statement stmt1 = iter2.nextStatement();
            while (marge.hasNext()){
                Statement stmt2 = marge.nextStatement();
                if (stmt1.getSubject().hasURI(stmt2.getSubject().getURI())){
                    System.out.println("Su1: "+ stmt2);
                    break;
                }
            }
            fusedModels.add(stmt1.getSubject(), stmt1.getPredicate(), stmt1.getObject());

        } */

        //System.out.println( "#FUSED models" );
        //fusedModels.write(System.out,"rdf/xml");

        watch.stop();
        System.out.println( "Eecution time: "+  watch.getTotalTimeMillis() + " ms" );
    }

   

    private static Model mergedModel(Model model1, Model model2) {
        Model mergedModel = ModelFactory.createDefaultModel();
        ConflictManagement conflictManagement = new ConflictManagement();
        mergedModel = conflictManagement.DetectConflict(model1,model2);

        //cheak if the subject of model1 is equal to the subject of model2

        return mergedModel;
    }


}







