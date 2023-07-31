package Old.uni_ma;

import de.uni_mannheim.informatik.dws.melt.matching_data.TrackRepository;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.semanticweb.owlapi.io.RDFNode;

import com.google.j2objc.annotations.Property;


public class Main {
    
    public static void main(String[] args){
                       
        //If you work with files:
        //OntModel swtorKG = OntologyCacheJena.get(new File("swtor.rdf"));
        
        //will automatically download the files for you
        OntModel swtorKG = TrackRepository.Knowledgegraph.V4.getTestCase("starwars-swtor").getTargetOntology(OntModel.class);
        
           
        //iterate over triples
        ExtendedIterator<Triple> triples = swtorKG.getGraph().find(Node.ANY, Node.ANY, Node.ANY);
        while(triples.hasNext()){
            Triple t = triples.next();
            System.out.println(t);
            break;
        }
        
        //search for specific triples
        Node genderProperty = NodeFactory.createURI("http://dbkwik.webdatacommons.org/swtor.wikia.com/property/gender");
        Node planetProperty = NodeFactory.createURI("http://dbkwik.webdatacommons.org/swtor.wikia.com/property/planet");
        triples = swtorKG.getGraph().find(Node.ANY, genderProperty, Node.ANY);
        while(triples.hasNext()){
            Triple t = triples.next();

            System.out.println(t);
            System.out.println("There is the Object: "+t.getObject());
            System.out.println("There is the subject: "+t.getSubject());
            System.out.println("There is the predicate: "+t.getPredicate());
            System.out.println("There is the discription: "+t.getClass().getName());
            break;
        }
        
        //list all properties:
        ExtendedIterator<OntProperty> properties = swtorKG.listAllOntProperties();
        while(properties.hasNext()){
            OntProperty property = properties.next();
            System.out.println(property);
        }

        //List all resources:
       ExtendedIterator<Statement> statements = swtorKG.listStatements();
       while (statements.hasNext()) {
           Statement statement = statements.next();
           Resource subject = statement.getSubject();
           System.out.println(subject.getURI());
       }
       //print out rdf file
       // swtorKG.write(System.out, "RDF/XML-ABBREV");
        //list all statements
        StmtIterator iter = swtorKG.listStatements();
        // print out the predicate, subject and object of each statement
        // while (iter.hasNext()) {
        //     Statement stmt      = iter.nextStatement();  // get next statement
        //     Resource  subject   = stmt.getSubject();     // get the subject
        //     Property  predicate = (Property) stmt.getPredicate();   // get the predicate
        //     RDFNode   object    = (RDFNode) stmt.getObject();      // get the object
        //     System.out.print(subject.toString());
        //     System.out.print(" " + predicate.toString() + " ");
        //     if (object instanceof Resource) {
        //         System.out.print(object.toString());
        //     } else {
        //         // object is a literal
        //         System.out.print(" \"" + object.toString() + "\"");
        //     }
        //     System.out.println(" .");
        // }

        // print each subject, object and predicate
        //write text subject, object, and predicate before each subject, object, and predicate

        while (iter.hasNext()) {
            Statement stmt      = iter.nextStatement();  // get next statement
            Resource  subject   = stmt.getSubject();     // get the subject
            Property  predicate = (Property) stmt.getPredicate();   // get the predicate
            RDFNode   object    = (RDFNode) stmt.getObject();      // get the object
            System.out.println("Subject: "+subject.toString());
            System.out.println("Predicate: "+predicate.toString());
            System.out.println("Object: "+object.toString());
        }
        //write file in xml formate
        // write file in xml formate
        //swtorKG.write(System.out, "RDF/XML-ABBREV");
    }
    
}
