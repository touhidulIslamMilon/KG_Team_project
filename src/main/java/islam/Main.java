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
        //OntModel fusedKnowledgeGraph = fusion.mergeKnowledgeGraphslength(ontModel1, ontModel2);
        //OntModel fusedKnowledgeGraph = fusion.mergeKnowledgeGraphslink(ontModel1, ontModel2);
        
        OntModel fusedKnowledgeGraph = fusion.mergeKnowledgeGraphslength(ontModel1, ontModel2);
        OntModel fusedKnowledgeGraph2 = fusion.mergeKnowledgeGraphslength(ontModel2, ontModel1);
        //OntModel fusedKnowledgeGraph = fusion.mergeKnowledgeGraphs(ontModel1, ontModel2);
        //OntModel fusedKnowledgeGraph = fusion.mergeKnowledgeGraphsrule2(ontModel1, ontModel2);


        //union model 
        Model fusedModels1 = fusedKnowledgeGraph.union(fusedKnowledgeGraph2);

        // Print the fused knowledge graph in Turtle format
        fusedKnowledgeGraph.write(System.out, "TURTLE");
        fusedKnowledgeGraph2.write(System.out, "TURTLE");
        fusedModels1.write(System.out, "TURTLE");
        




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







