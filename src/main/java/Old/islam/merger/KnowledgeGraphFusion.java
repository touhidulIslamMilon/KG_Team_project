package Old.islam.merger;
//import FinalPackage.Merging.Strategies.fusionStrategy;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import FinalPackage.Old.normlaisationFunctions;


public class KnowledgeGraphFusion {


   
    public static OntModel mergeKnowledgeGraphs(Model model1, Model model2) {
        normlaisationFunctions normlaisationFunctions = new normlaisationFunctions();

        //!SECTION
        //normalise the model data
        /* model1 = normlaisationFunctions.normalizeModel(model1);
        model2 = normlaisationFunctions.normalizeModel(model1);  */

        Model mergedModel = ModelFactory.createDefaultModel();
        StmtIterator iter = model1.listStatements();
        int nonfunctionalProperty = 0;
        mergedModel = model1.union(model2);
        mergedModel.write(System.out,"TURTLE");
        int count = 0;
        while (iter.hasNext()) {
            Statement stmt = iter.next();
            Resource subj = stmt.getSubject();
            String uri = stmt.getSubject().getURI();
            Property  predicate =  stmt.getPredicate();
            Resource resource = model2.getResource(uri);
            StmtIterator stmtIterator = model2.listStatements(subj, predicate, (RDFNode) null);
            int duplicate=0;
            while(stmtIterator.hasNext()){
                count +=1;
                Statement statement = stmtIterator.next();
                if(!stmt.getObject().equals(statement.getObject())){
                    duplicate+=1;
                    System.out.println(duplicate+" Duplicate: "+predicate.toString());
                    
                }
                System.out.println(statement);
                System.out.println(stmt);

                //!SECTION conflict management goes here
                //fusionStrategy fusionStrategy = new fusionStrategy();
                /*RDFNode res = fusionStrategy.resolvePredicateConflict(statement.getObject(),stmt.getObject());
                
                System.out.println(res);
                mergedModel.remove(subj, predicate, statement.getObject());
                mergedModel.remove(subj, predicate, stmt.getObject());
                mergedModel.add(stmt.getSubject(), stmt.getPredicate(),res);*/
            }
            if(duplicate>=1){
                
                nonfunctionalProperty+=1;
            }
            
        }
         System.out.println(" Not Functional Property: "+nonfunctionalProperty);
         System.out.println("All property: "+count);
         System.out.println("Functional property: "+(count-nonfunctionalProperty));
        
        OntModel ontModelont = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, mergedModel);


        return ontModelont;
    }

    private static RDFNode resolvePredicateConflictfrist(RDFNode object, RDFNode object1) {
        return object;
    }
    private static RDFNode resolvePredicateConflictlast(RDFNode object, RDFNode object1) {
        return object1;
    }
    

    //create function that will take two object and return first one
    //if the length of the first one is greater than the second one 
    //else return the second one
    private static RDFNode resolvePredicateConflictlenght(RDFNode object, RDFNode object1) {
        if(object.toString().length()>object1.toString().length()){
            return object;
        }
        return object1;
    }

    public OntModel mergeKnowledgeGraphsfirst(OntModel model1, Model model2) {
        normlaisationFunctions normlaisationFunctions = new normlaisationFunctions();

        //!SECTION
        //normalise the model data
        /* model1 = normlaisationFunctions.normalizeModel(model1);
        model2 = normlaisationFunctions.normalizeModel(model1);  */

        Model mergedModel = ModelFactory.createDefaultModel();
        StmtIterator iter = model1.listStatements();
        int nonfunctionalProperty = 0;
        mergedModel = model1.union(model2);
        mergedModel.write(System.out,"TURTLE");
        int count = 0;
        while (iter.hasNext()) {
            Statement stmt = iter.next();
            Resource subj = stmt.getSubject();
            String uri = stmt.getSubject().getURI();
            Property  predicate =  stmt.getPredicate();
            Resource resource = model2.getResource(uri);
            StmtIterator stmtIterator = model2.listStatements(subj, predicate, (RDFNode) null);
            int duplicate=0;
            while(stmtIterator.hasNext()){
                count +=1;
                Statement statement = stmtIterator.next();
                if(!stmt.getObject().equals(statement.getObject())){
                    duplicate+=1;
                    System.out.println(duplicate+" Duplicate: "+predicate.toString());
                    
                }
                System.out.println(statement);
                System.out.println(stmt);

                //!SECTION conflict management goes here
                //RDFNode res = resolvePredicateConflictlenght(statement.getObject(),stmt.getObject());

                //if there is a conflict then return the first one
                RDFNode res = resolvePredicateConflictfrist(statement.getObject(),stmt.getObject());

                //if there is a conflict then return the last one
                //RDFNode res = resolvePredicateConflictlast(statement.getObject(),stmt.getObject());
                
                System.out.println(res);
                mergedModel.remove(subj, predicate, statement.getObject());
                mergedModel.remove(subj, predicate, stmt.getObject());
                mergedModel.add(stmt.getSubject(), stmt.getPredicate(),res);
            }
            if(duplicate>=1){
                
                nonfunctionalProperty+=1;
            }
            
        }
         System.out.println(" Not Functional Property: "+nonfunctionalProperty);
         System.out.println("All property: "+count);
         System.out.println("Functional property: "+(count-nonfunctionalProperty));
        
        OntModel ontModelont = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, mergedModel);


        return ontModelont;
    }
}
