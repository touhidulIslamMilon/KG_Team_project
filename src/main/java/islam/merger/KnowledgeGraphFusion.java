package islam.merger;
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
        StmtIterator iter1 = model1.listStatements();
        StmtIterator iter2 = model2.listStatements();
        Model conflict = ModelFactory.createDefaultModel();
        Model fusedModels = ModelFactory.createDefaultModel();

        while (iter1.hasNext()) {
            Statement statement1 = iter1.next();
            Resource subject1 = statement1.getSubject();
            Property predicate1 = statement1.getPredicate();
            String uri1 = statement1.getSubject().getURI();
            // Add the mapped statements from model2 to the merged model
            Resource resource = model2.getResource(uri1);
            
            while (iter2.hasNext()) {
                Statement statement2 = iter2.next();
                Resource subject2 = statement2.getSubject();
                Property predicate2 = statement2.getPredicate();
 
                //if the subject of model1 is equal to the subject of model2
                
                Resource resource1 = model1.getResource(uri1);
                if(statement1.getSubject().hasURI(uri1)){

                    //if the predicate of model1 is equal to the predicate of model2

                    Property stmtpredicate = statement1.getPredicate();
                    //System.out.println(resource.getProperty(stmt.getPredicate()));
                    Statement conflict1 = resource.getProperty(stmtpredicate);
                    Statement conflict2 = resource1.getProperty(stmtpredicate);
                    if(conflict1!=null ){

                        conflict.add(conflict1);
                        

                        System.out.println("1: "+conflict1);

                        // If the predicate of model1 is equal to the predicate of model2
                        if(conflict2!=null&&conflict1.getPredicate().equals(conflict2.getPredicate())){
                            RDFNode res = resolvePredicateConflict(conflict1.getObject(),conflict2.getObject());
                            fusedModels.add(statement1.getSubject(), statement1.getPredicate(),res);
                            conflict.add(conflict2);
                            System.out.println("2: "+conflict2);
                        }else{
                            fusedModels.add(statement1.getSubject(), statement1.getPredicate(),conflict1.getObject());
                        }
                    }else {
                        fusedModels.add(statement1.getSubject(), statement1.getPredicate(), statement1.getObject());
                    }
                    break;
                }else{
                    fusedModels.add(statement1.getSubject(), statement1.getPredicate(), statement1.getObject());
                }

                
            }
        }
        OntModel ontModelont = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, fusedModels);


        return ontModelont;
    }

    private static RDFNode resolvePredicateConflict(RDFNode object, RDFNode object1) {
        if(object.toString().length()>object1.toString().length()){
            return object;
        }
        return object1;
    }
    
    
}
