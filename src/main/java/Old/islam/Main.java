package Old.islam;

import Old.islam.merger.ConflictManagement;
import Old.islam.merger.InOu;
import Old.islam.merger.KnowledgeGraphFusion;
import FinalPackage.Merging.LoadRDF;

import java.util.*;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.springframework.util.StopWatch;

import org.apache.jena.rdf.model.*;

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

        /* 
       //!SECTION --------- this is the code for calculate functional property
        //code for calculate functional property
        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, model);
        functionalProperty functionalProperty = new functionalProperty();
        OntModel listfunctionalProperty = functionalProperty.getFunctionalProperty(model); */

        //marge functional marger knowledge graph
        KnowledgeGraphFusion fusion = new KnowledgeGraphFusion();
        Model fusedKnowledge= ModelFactory.createDefaultModel();

        //fusedModels.write(System.out);


        OntModel fusedKnowledgeGraph = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, fusedKnowledge);


       


        // Print the fused knowledge graph in Turtle format*/
         //!SECTION --------- this is the code for knowledge graph fusion
        //OntModel fusedKnowledgeGraph = fusion.mergeKnowledgeGraphslength(model1, model2);
        OntModel mergeKnowledgeGraphsfirst = fusion.mergeKnowledgeGraphs(model1, model2);

        
         //!SECTION --------- this is the code for knowledge graph fusion
        //marge knowledge graph using marger knowledge graph function in accordance with the priority
        List<KnowledgeGraph> knowledgeGraphs = new ArrayList<>();
        knowledgeGraphs.add(new KnowledgeGraph(1,model2,new Date(1234567890L))); // Higher priority
        knowledgeGraphs.add(new KnowledgeGraph(2,model1,new Date(2345678901L)));

        //Short base on priority
        //knowledgeGraphs = sortKnowledgeGraphsByPriority(knowledgeGraphs);

        //Short base on date
        knowledgeGraphs = sortKnowledgeGraphsByDate(knowledgeGraphs);

        while(knowledgeGraphs.size() >= 1) {
            KnowledgeGraph graph1 = knowledgeGraphs.get(0);
            knowledgeGraphs.remove(graph1);
            fusedKnowledgeGraph = fusion.mergeKnowledgeGraphsfirst(fusedKnowledgeGraph,graph1.getGraph());
        }

         //!SECTION --------- this is the code for knowledge graph date

        fusedKnowledgeGraph.write(System.out, "TURTLE");
        


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
     public static List<KnowledgeGraph> sortKnowledgeGraphsByPriority(List<KnowledgeGraph> graphs) {
        // Sort the list in ascending order based on priority
        Collections.sort(graphs, Comparator.comparingInt(KnowledgeGraph::getPriority));
        return graphs;
    }
    public static List<KnowledgeGraph> sortKnowledgeGraphsByDate(List<KnowledgeGraph> graphs) {
        // Sort the list in ascending order based on date
        Collections.sort(graphs, Comparator.comparing(KnowledgeGraph::getDate));
        return graphs;
    }
    

}







