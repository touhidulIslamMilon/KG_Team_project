package fusion;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;

public class ConflictManagement {

    public Statement ManageConflict(Statement stmt, Statement stmt1, String uri) {
        Model managed = ModelFactory.createDefaultModel();

        if(stmt.getPredicate().hasProperty(stmt1.getPredicate())){
            System.out.println("Su1: " + stmt.getSubject() + " " + stmt.getPredicate() + " " + stmt.getObject());
        }
        return stmt1;
    }
}
