package uni_ma.kg_merger;

import de.uni_mannheim.informatik.dws.melt.matching_data.TrackRepository;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;

public class KGMerger {

    public static void main(String[] args) {
        // Create two RDF models representing the knowledge graphs
        Model model1 = ModelFactory.createDefaultModel();
        Model model2 = ModelFactory.createDefaultModel();

        // Load data into the models from RDF files or other sources
        model1.read("testA.rdf");
        System.out.println("Graph A");
        model1.write(System.out, "RDF/XML-ABBREV");
        model2.read("testB.rdf");
        System.out.println("Graph B");
        model1.write(System.out, "RDF/XML-ABBREV");

        // Create a new model to hold the merged knowledge graph
        Model mergedModel = ModelFactory.createDefaultModel();
        mergedModel = model1.union(model2);

        // Output the merged model to an RDF file or other destination
        System.out.println("Merged");
        mergedModel.write(System.out, "RDF/XML-ABBREV");
    }

}

