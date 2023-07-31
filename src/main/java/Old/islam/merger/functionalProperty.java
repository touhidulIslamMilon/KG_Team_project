package Old.islam.merger;

import org.apache.jena.ontology.OntModel;

import org.apache.jena.rdf.model.*;

public class functionalProperty {
    
     public  OntModel getFunctionalProperty(Model model) {
        Model mergedModel = ModelFactory.createDefaultModel();
        StmtIterator iter = model.listStatements();
        int nonfunctionalProperty = 0;
        int count = 0;
        while (iter.hasNext()) {
            Statement stmt = iter.next();
            Resource subj = stmt.getSubject();
            String uri = stmt.getSubject().getURI();
            Property  predicate =  stmt.getPredicate();
            Resource resource = model.getResource(uri);
            StmtIterator stmtIterator = model.listStatements(subj, predicate, (RDFNode) null);
            int duplicate=0;
            while(stmtIterator.hasNext()){
                count +=1;

                Statement statement = stmtIterator.next();
                 if(!stmt.getObject().equals(statement.getObject())){
                    duplicate+=1;
                    
                }
            }
            if(duplicate>1){
                 System.out.println(duplicate+" Duplicate: "+predicate.toString());
                 nonfunctionalProperty+=1;
            }
            
        }
         System.out.println(" Not Functional Property: "+nonfunctionalProperty);
         System.out.println("All property: "+count);
         System.out.println("Functional property: "+(count-nonfunctionalProperty));
        return null;
    }
    
}
