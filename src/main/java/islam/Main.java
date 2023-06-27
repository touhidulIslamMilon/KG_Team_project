package islam;

import islam.merger.ConflictManagement;
import islam.merger.InOu;
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



        Model mergedModel = mergedModel(model1,model2);
        System.out.println( "#Conflict models" );
        mergedModel.write(System.out,"TURTLE");

        StmtIterator iter1 = model1.listStatements();
        StmtIterator iter2 = model2.listStatements();

        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, model1);


        StmtIterator marge = mergedModel.listStatements();

        //print all statements in marge
        while (iter1.hasNext()){
            Statement stmt1 = iter1.nextStatement();

            // we define not
            Property invfunctionalproperty = ontModel.getInverseFunctionalProperty(stmt1.getSubject().getURI());
            System.out.println("Inverse Functional property: "+ invfunctionalproperty);
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

        }

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









//        while (iter1.hasNext()) {
//            Statement stmt = iter1.next();
//            Resource subj = stmt.getSubject();
//
//            // Add the mapped statements from model2 to the merged model
//            while (iter2.hasNext()) {
//                Statement stmt1 = iter2.next();
//                Resource subj1 = stmt1.getSubject();
//
////                if(stmt.getSubject().equals(stmt1.getSubject())){
////                    matched.add(stmt);
////                    matched.add(stmt1);
////                    System.out.println("Su: " + stmt1 + " " + stmt1.getPredicate() + " " + stmt1.getObject());
////                }
////                if(stmt.getObject().equals(stmt1.getObject())){
////                    matched.add(stmt);
////                    matched.add(stmt1);
////                    System.out.println("Oj: " + stmt1 + " " + stmt1.getPredicate() + " " + stmt1.getObject());
////                }
////                if(stmt.getPredicate().equals(stmt1.getPredicate())){
////                    matched.add(stmt);
////                    matched.add(stmt1);
////                    System.out.println("Pre: " + stmt1.getSubject() + " " + stmt1.getPredicate() + " " + stmt1.getObject());
////                    System.out.println("Pre1: " + stmt.getSubject() + " " + stmt.getPredicate() + " " + stmt.getObject());
////                }
//
//                if(subj1.hasProperty(stmt.getPredicate())){
//                    matched.add(subj1.getProperty(stmt.getPredicate()));
//
//                    StmtIterator matchedlist = matched.listStatements();
//                    while (matchedlist.hasNext()){
//                        Statement macthed = matchedlist.next();
//                        if (macthed == stmt.getSubject()){
//                            System.out.println("Subject: " + subj1 + " " + stmt1.getPredicate() + " " + stmt1.getObject());
//                        }
//                        if (macthed.getObject()==stmt.getObject()){
//                            System.out.println("Object: " + subj1 + " " + stmt1.getPredicate() + " " + stmt1.getObject());
//                        }
//                        if (macthed.getPredicate()== stmt.getPredicate()){
//                            System.out.println("Predicate: " + subj1 + " " + stmt1.getPredicate() + " " + stmt1.getObject());
//                        }
//                        //System.out.println("FN: " + stmt2 + " " + stmt2.getPredicate() + " " + stmt2.getObject());
//                    }
//                    System.out.println("FN: " + stmt1.getSubject() + " " + stmt1.getPredicate() + " " + stmt1.getObject());
//                    System.out.println("FN1: " + stmt.getSubject() + " " + stmt.getPredicate() + " " + stmt.getObject());
//                }else{
//                    Property pred1 = stmt1.getPredicate();
//                    RDFNode obj1 = stmt1.getObject();
//                    mergedModel.add(subj1, pred1, obj1);
//                }
//            }
//            Property pred = stmt.getPredicate();
//            RDFNode obj = stmt.getObject();
//            mergedModel.add(subj, pred, obj);
//
//        }