package fusion.merger;

import org.apache.jena.rdf.model.*;

public class ConflictManagement {

    public Statement ManageConflict(Statement stmt, Statement stmt1) {
        Model managed = ModelFactory.createDefaultModel();

        if(stmt.getPredicate().hasProperty(stmt1.getPredicate())){
            System.out.println("Su1: " + stmt.getSubject() + " " + stmt.getPredicate() + " " + stmt.getObject());
        }
        return stmt1;
    }
    static public Model DetectConflict(Model model1, Model model2) {
        Model mergedModel = ModelFactory.createDefaultModel();
        StmtIterator iter1 = model1.listStatements();
        StmtIterator iter2 = model2.listStatements();
        Model conflict = ModelFactory.createDefaultModel();
        Model fusedModels = ModelFactory.createDefaultModel();
        while (iter1.hasNext()) {
            Statement stmt = iter1.next();
            Resource subj = stmt.getSubject();

            // Add the mapped statements from model2 to the merged model
            while (iter2.hasNext()) {
                Statement stmt1 = iter2.next();
                String uri = stmt1.getSubject().getURI();
                //if the subject of model1 is equal to the subject of model2
                Resource resource = model2.getResource(uri);
                Resource resource1 = model1.getResource(uri);
                if(stmt.getSubject().hasURI(uri)){

                    //if the predicate of model1 is equal to the predicate of model2

                    Property stmtpredicate = stmt.getPredicate();
                    //System.out.println(resource.getProperty(stmt.getPredicate()));
                    Statement conflict1 = resource.getProperty(stmtpredicate);
                    Statement conflict2 = resource1.getProperty(stmtpredicate);
                    if(conflict1!=null ){

                        conflict.add(conflict1);

                        System.out.println("1: "+conflict1);

                        // If the predicate of model1 is equal to the predicate of model2
                        if(conflict2!=null&&conflict1.getPredicate().equals(conflict2.getPredicate())){
                            RDFNode res = resolvePredicateConflict(conflict1.getObject(),conflict2.getObject());
                            mergedModel.add(stmt1.getSubject(), stmt1.getPredicate(),res);
                            fusedModels.add(stmt1.getSubject(), stmt1.getPredicate(),res);
                            conflict.add(conflict2);
                            System.out.println("2: "+conflict2);
                        }
                    }else {
                        fusedModels.add(stmt1.getSubject(), stmt1.getPredicate(), stmt1.getObject());
                    }
                    break;
                }
                fusedModels.add(stmt1.getSubject(), stmt1.getPredicate(), stmt1.getObject());
            }
//
        }

        conflict.write(System.out,"TURTLE");
        fusedModels.write(System.out,"rdf/xml");

        return mergedModel;

    }
    private static RDFNode resolvePredicateConflict(RDFNode object, RDFNode object1) {
        if(object.toString().length()>object1.toString().length()){
            return object;
        }
        return object1;
    }
}
