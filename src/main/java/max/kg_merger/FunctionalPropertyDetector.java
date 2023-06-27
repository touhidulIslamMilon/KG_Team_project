package max.kg_merger;

import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.ontology.FunctionalProperty;

public class FunctionalPropertyDetector {

    // Gives back an ExtendedIterator of all properties of an ontModel
    public static ExtendedIterator<OntProperty> getAllProperties(OntModel ontModel){
        return ontModel.listAllOntProperties();
    }

    // Gives back an ExtendedIterator of all functional properties of an ontModel
    public static ExtendedIterator<FunctionalProperty> getFunctionalProperties(OntModel ontModel){
        return ontModel.listFunctionalProperties();
    }

    // Gives back a boolean value if a property is functional
    public static boolean isFunctional(Model model, Property property){
        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, model);
        OntProperty ontProperty = ontModel.createOntProperty(property.getURI());

        return ontProperty.isFunctionalProperty();
    }



}
