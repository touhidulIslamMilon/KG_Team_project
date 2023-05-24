package uni_ma.kg_merger;

import de.uni_mannheim.informatik.dws.melt.matching_data.TrackRepository;
import org.apache.jena.ontology.FunctionalProperty;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class KGMerger {

    public static void main(String[] args) {
        // Create two RDF models representing the knowledge graphs
        Model model1 = ModelFactory.createDefaultModel();
        Model model2 = ModelFactory.createDefaultModel();
        Model model3 = ModelFactory.createDefaultModel();

        // Test 1 Merge two simple graphs
        // Load data into the models from RDF files or other sources
        System.out.println("Test 1");

        model1.read("testA.rdf");
        System.out.println("Graph A");
        model1.write(System.out, "RDF/XML-ABBREV");

        model2.read("testB.rdf");
        System.out.println("Graph B");
        model2.write(System.out, "RDF/XML-ABBREV");

        // Create a new model to hold the merged knowledge graph
        Model mergedModel = ModelFactory.createDefaultModel();
        mergedModel = model1.union(model2);

        // Output the merged model to an RDF file or other destination
        System.out.println("Merged");
        mergedModel.write(System.out, "RDF/XML-ABBREV");



        System.out.println("\nTest 2");
        model3.read("testProperties.rdf");

        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, model3);
        ExtendedIterator<OntProperty> propertyIterator = ontModel.listAllOntProperties();
        while (propertyIterator.hasNext()) {
            OntProperty property = propertyIterator.next();
            System.out.println("Property: " + property.getLocalName() + " " + property.getURI());
            if (FunctionalPropertyDetector.isFunctional(ontModel, property)){
                System.out.println("yes");
            }
        }

        ExtendedIterator<FunctionalProperty> propIt2 = ontModel.listFunctionalProperties();
        while (propIt2.hasNext()) {
            FunctionalProperty property = propIt2.next();
            System.out.println("Functional Property: " + property.getLocalName() + " " + property.getURI());
        }



    }

}

