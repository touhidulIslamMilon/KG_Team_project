package tests;

import FinalPackage.Merging.ObjectResolution;
import org.apache.jena.rdf.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ObjectResolutionTest {

    // Helper method to create a sample model
    private static Model createSampleModel(Resource subject, Property predicate, String objectValue) {
        Model model = ModelFactory.createDefaultModel();
        model.add(subject, predicate, model.createLiteral(objectValue));
        return model;
    }

    @Test
    public void testGetResolvedObjectValue() {
        List<Model> models = new ArrayList<>();
        Resource subject = ResourceFactory.createResource("http://example.com/person/mary");
        Property genderProperty = ResourceFactory.createProperty("http://example.com/gender");
        Property ageProperty = ResourceFactory.createProperty("http://example.com/age");

        // Create three sample models representing different subject-predicate combinations
        models.add(createSampleModel(subject, genderProperty, "Female"));
        models.add(createSampleModel(subject, genderProperty, "Male"));
        models.add(createSampleModel(subject, genderProperty, "NonBinary"));
        models.add(createSampleModel(subject, ageProperty, "12"));
        models.add(createSampleModel(subject, ageProperty, "12"));

        // Test the subject-predicate combination with a conflict
        RDFNode resolvedObject1 = ObjectResolution.getResolvedObjectValue(models, subject, genderProperty);
        Assert.assertEquals("NonBinary", resolvedObject1.toString()); // Expecting "NonBinary" as it was the last object

        // Test the subject-predicate combination with no conflict
        RDFNode resolvedObject2 = ObjectResolution.getResolvedObjectValue(models, subject, ageProperty);
        Assert.assertEquals("12", resolvedObject2.toString());

    }
}
