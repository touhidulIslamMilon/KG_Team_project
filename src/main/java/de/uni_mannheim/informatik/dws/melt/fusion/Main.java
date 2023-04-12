package de.uni_mannheim.informatik.dws.melt.fusion;

import de.uni_mannheim.informatik.dws.melt.matching_data.TrackRepository;
import de.uni_mannheim.informatik.dws.melt.matching_jena.OntologyCacheJena;
import java.io.File;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.util.iterator.ExtendedIterator;


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
        triples = swtorKG.getGraph().find(Node.ANY, genderProperty, Node.ANY);
        while(triples.hasNext()){
            Triple t = triples.next();
            System.out.println(t);
            break;
        }
        
        //list all properties:
        ExtendedIterator<OntProperty> properties = swtorKG.listAllOntProperties();
        while(properties.hasNext()){
            OntProperty property = properties.next();
            System.out.println(property);
        }
        
    }
    
}
