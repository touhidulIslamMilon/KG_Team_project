package uni_ma.kg_merger;

import de.uni_mannheim.informatik.dws.melt.matching_data.TrackRepository;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

public class KGMerger {

    public static void main(String[] args) {
        // Create two RDF models representing the knowledge graphs
        Model model1 = ModelFactory.createDefaultModel();
        Model model2 = ModelFactory.createDefaultModel();

        // Load data into the models from RDF files or other sources
        model1.read("testA.rdf");
        model2.read("testB.rdf");

        // Create a new model to hold the merged knowledge graph
        Model mergedModel = ModelFactory.createDefaultModel();

        // Add the statements from the first model to the merged model
        StmtIterator iter1 = model1.listStatements();
        while (iter1.hasNext()) {
            Statement stmt = iter1.next();
            mergedModel.add(stmt);
        }

        // Add the statements from the second model to the merged model
        StmtIterator iter2 = model2.listStatements();
        while (iter2.hasNext()) {
            Statement stmt = iter2.next();
            mergedModel.add(stmt);
        }

        // Output the merged model to an RDF file or other destination
        mergedModel.write(System.out, "RDF/XML-ABBREV");
    }

}

