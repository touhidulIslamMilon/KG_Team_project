package islam.merger;

import islam.merger.ConflictManagement;
import islam.merger.InOu;
import max.loadRDF.LoadRDF;

import java.net.URI;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.springframework.util.StopWatch;

import org.apache.jena.rdf.model.*;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.StmtIterator;

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
    
}
