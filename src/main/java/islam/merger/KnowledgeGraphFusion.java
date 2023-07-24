package islam.merger;
import org.antlr.v4.runtime.atn.SemanticContext.Predicate;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.util.Iterator;
import java.util.Map;
import java.util.Iterator;


public class KnowledgeGraphFusion {

    
   
    public static OntModel mergeKnowledgeGraphslength(OntModel model1, OntModel model2) {
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
                RDFNode res = resolvePredicateConflict(statement.getObject(),stmt.getObject());
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



    private static RDFNode resolvePredicateConflict(RDFNode object, RDFNode object1) {
        if(object.toString().length()>object1.toString().length()){
            return object;
        }
        return object1;
    }
    
    
}
