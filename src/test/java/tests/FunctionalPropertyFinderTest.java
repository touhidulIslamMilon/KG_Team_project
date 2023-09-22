package tests;

import FinalPackage.Merging.FunctionalPropertyFinder;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResourceFactory;

import java.util.ArrayList;
import java.util.List;

public class FunctionalPropertyFinderTest {
    public static void main(String[] args) {
        // Load RDF models from the provided RDF files
        Model model1 = ModelFactory.createDefaultModel();
        model1.read("src/test/testResources/starwars1Test.rdf");

        Model model2 = ModelFactory.createDefaultModel();
        model2.read("src/test/testResources/starwars2Test.rdf");

        Model model3 = ModelFactory.createDefaultModel();
        model3.read("src/test/testResources/starwars3Test.rdf");

        List<Model> models = new ArrayList<>();
        models.add(model1);
        models.add(model2);
        models.add(model3);

        // Test the FunctionalPropertyFinder class
        List<Property> functionalProperties = FunctionalPropertyFinder.findFunctionalProperties(models);
        List<Property> expectedProperties = new ArrayList<>();

        // Print the functional properties found
        System.out.println("Functional Properties:");
        for (Property property : functionalProperties) {
            System.out.println(property);
        }
        if (FunctionalPropertyFinder.getThresholdPercentage() < (double) 2 / 9) {
            // Define the URIs for the properties
            String nameURI = "http://example.org/starwars/name";
            String speciesURI = "http://example.org/starwars/species";
            String genderURI = "http://example.org/starwars/gender";

            // Create properties using the ResourceFactory
            Property nameProperty = ResourceFactory.createProperty(nameURI);
            Property speciesProperty = ResourceFactory.createProperty(speciesURI);
            Property genderProperty = ResourceFactory.createProperty(genderURI);

            // Create a list of these properties
            expectedProperties.add(nameProperty);
            expectedProperties.add(speciesProperty);
            expectedProperties.add(genderProperty);
        } else {
            // Define the URIs for the properties
            String lightsaberURI = "http://example.org/starwars/lightsaber_color";
            String nameURI = "http://example.org/starwars/name";
            String speciesURI = "http://example.org/starwars/species";
            String genderURI = "http://example.org/starwars/gender";

            // Create properties using the ResourceFactory
            Property lightsaberProperty = ResourceFactory.createProperty(lightsaberURI);
            Property nameProperty = ResourceFactory.createProperty(nameURI);
            Property speciesProperty = ResourceFactory.createProperty(speciesURI);
            Property genderProperty = ResourceFactory.createProperty(genderURI);

            // Create a list of these properties
            expectedProperties.add(lightsaberProperty);
            expectedProperties.add(nameProperty);
            expectedProperties.add(speciesProperty);
            expectedProperties.add(genderProperty);
        }
        System.out.println("Expected Properties:");
        for (Property property : expectedProperties) {
            System.out.println(property);
        }

        System.out.println("Do we have the right functional properties? " + arePropertyListsEqual(functionalProperties, expectedProperties));
    }


    public static boolean arePropertyListsEqual(List<Property> list1, List<Property> list2) {
        // Check if the lists are the same size
        if (list1.size() != list2.size()) {
            return false;
        }

        // Iterate through the lists and compare each property
        for (int i = 0; i < list1.size(); i++) {
            Property property1 = list1.get(i);
            Property property2 = list2.get(i);

            // Compare properties based on their URIs
            if (!property1.getURI().equals(property2.getURI())) {
                return false; // Properties are not equal
            }
        }

        // If we reach here, the lists are equal
        return true;
    }

}
