package islam;

import islam.merger.ConflictManagement;
import islam.merger.InOu;
import max.loadRDF.LoadRDF;

import java.net.URI;

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
        model1.write(System.out);

        System.out.println( "#testB" );
        model2.write(System.out);

        System.out.println( "#FUSED models" );
        //Model fusedModels = modelA.union(modelB);
        //create a custom union model
        Model fusedModels = ModelFactory.createDefaultModel();

        //fusedModels.write(System.out);



        //Model mergedModel = mergedModel(model1,model2);


        System.out.println( "#Conflict models" );
        //mergedModel.write(System.out,"TURTLE");

        StmtIterator iter1 = model1.listStatements();
        StmtIterator iter2 = model2.listStatements();
        Model model = LoadRDF.getModel("swtor.rdf");

        OntModel ontModel1 = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, model1);
        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, model);
        OntModel functionalProperty = getFunctionalProperty(model);
        


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

    private static OntModel getFunctionalProperty(Model model) {
        Model mergedModel = ModelFactory.createDefaultModel();
        StmtIterator iter = model.listStatements();
        int nonfunctionalProperty = 0;
        int count = 0;
        while (iter.hasNext()) {
            Statement stmt = iter.next();
            Resource subj = stmt.getSubject();
            String uri = stmt.getSubject().getURI();
            Property  property =  stmt.getPredicate();
            Resource resource = model.getResource(uri);
            StmtIterator stmtIterator = model.listStatements(subj, property, (RDFNode) null);
            int duplicate=0;
            while(stmtIterator.hasNext()){
                count +=1;
                duplicate+=1;
                Statement statement = stmtIterator.next();
            }
            if(duplicate>1){
                 System.out.println(duplicate+" Duplicate: "+property.toString());
                 nonfunctionalProperty+=1;
            }
            
        }
         System.out.println(" Not Functional Property: "+nonfunctionalProperty);
         System.out.println("All property: "+count);
         System.out.println("Functional property: "+(count-nonfunctionalProperty));
        return null;
    }

    private static Model mergedModel(Model model1, Model model2) {
        Model mergedModel = ModelFactory.createDefaultModel();
        ConflictManagement conflictManagement = new ConflictManagement();
        mergedModel = conflictManagement.DetectConflict(model1,model2);

        //cheak if the subject of model1 is equal to the subject of model2

        return mergedModel;
    }


}







