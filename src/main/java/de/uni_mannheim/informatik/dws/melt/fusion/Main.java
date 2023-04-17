package de.uni_mannheim.informatik.dws.melt.fusion;

import de.uni_mannheim.informatik.dws.melt.matching_data.TrackRepository;
import de.uni_mannheim.informatik.dws.melt.matching_jena.OntologyCacheJena;
import java.io.File;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.util.iterator.ExtendedIterator;


public class Main {
    
    public static void main(String[] args){
                       
        //If you work with files:
        //OntModel swtorKG = OntologyCacheJena.get(new File("swtor.rdf"));
        
        //will automatically download the files for you
        OntModel swtorKG = TrackRepository.Knowledgegraph.V4.getTestCase("starwars-swtor").getTargetOntology(OntModel.class);

        //print out the entire dataset
        swtorKG.write(System.out, "RDF/XML-ABBREV");

        //iterate over triples
        ExtendedIterator<Triple> triples = swtorKG.getGraph().find(Node.ANY, Node.ANY, Node.ANY);
        while(triples.hasNext()){
            Triple t = triples.next();
            System.out.println(t.toString());
            break;
        }
        
        //search for specific triples
        Node genderProperty = NodeFactory.createURI("http://dbkwik.webdatacommons.org/swtor.wikia.com/property/planet");
        triples = swtorKG.getGraph().find(Node.ANY, genderProperty, Node.ANY);
        System.out.println("New Data");
        while(triples.hasNext()){
            Triple t = triples.next();
            System.out.println("This is the touple: "+t);
            break;
        }
        System.out.println("New Data");
        //list all properties:
        ExtendedIterator<OntProperty> properties = swtorKG.listAllOntProperties();
        while(properties.hasNext()){
            OntProperty property = properties.next();
            System.out.println(property);
        }


        
    }
    
}
