package max.kg_merger;

import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.ontology.FunctionalProperty;

public class FunctionalPropertyDetector {

    public static boolean isFunctionalProperty(Model model, Property property) {
        StmtIterator stmtIterator = model.listStatements(null, property, (RDFNode) null);
        int objectCount = 0;
        while (stmtIterator.hasNext()) {
            stmtIterator.next();
            objectCount++;
            if (objectCount > 1) {
                return false;
            }
        }
        return true;
    }



}
