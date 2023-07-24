package islam.normalisation;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

public class normlaisationFunctions {

    //Entity Normalization
    public static String normalizeCase(String oldEntity) {
        // Implement the function based on your specific requirements
        return oldEntity.toLowerCase();
    }
    public static String normalizeWhitespace(String entity) {
        return entity.trim().replaceAll("\\s+", " ");
    }

    public static String normalizeSpecialCharacters(String entity) {
        // Here we are replacing all non-alphanumeric characters with an underscore.
        // You can adjust this based on your specific needs.
        return entity.replaceAll("[^a-zA-Z0-9]", "_");
    }

    //URI Normalization
    public static String normalizeURI(String oldURI) {
        // Implement the function based on your specific requirements
        return null;
    }

    //Date Normalization

    private static final String[] dateFormats = {"dd/MM/yyyy", "MM-dd-yyyy", "yyyy.MM.dd"};
    public static String normalizeDate(String oldDateString) {

        for (String format : dateFormats) {
            try {
                DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                        .appendPattern(format)
                        .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                        .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                        .parseStrict()
                        .toFormatter();

                LocalDate date = LocalDate.parse(oldDateString, formatter);
                return date.toString();
            } catch (DateTimeParseException e) {
                // This format did not work, try the next one
            }
        }
        // None of the formats worked
        return null;
    }

    public static Model normalizeDates(Model model) {
        Model normalizedModel = ModelFactory.createDefaultModel();
        StmtIterator iterator = model.listStatements();

        while (iterator.hasNext()) {
            Statement stmt = iterator.nextStatement();
            Resource subject = stmt.getSubject();
            Property predicate = stmt.getPredicate();
            RDFNode object = stmt.getObject();

            if (object.isLiteral()) {
                String potentialDateString = object.asLiteral().getString();
                String normalizedDate = normalizeDate(potentialDateString);
                if (normalizedDate != null) {
                    // The string was successfully parsed as a date and normalized
                    object = normalizedModel.createTypedLiteral(normalizedDate, XSDDatatype.XSDdate);
                } else {
                    // The string could not be parsed as a date in any of the provided formats, leave it as is

                }
            }

            Statement normalizedStatement = ResourceFactory.createStatement(subject, predicate, object);
            normalizedModel.add(normalizedStatement);
        }
        return normalizedModel;
    }

    public static  Model normalizeModel(Model originalModel) {
        Model normalizedModel = ModelFactory.createDefaultModel();
        StmtIterator iter = originalModel.listStatements();
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement();
            // Normalize the subject
            Resource oldSubject = stmt.getSubject();
            String oldSubjectUri = oldSubject.getURI();
            String newSubjectUri = normalizeCase(oldSubjectUri);
            newSubjectUri = normalizeWhitespace(newSubjectUri);
            newSubjectUri = normalizeSpecialCharacters(newSubjectUri);
            Resource newSubject = ResourceFactory.createResource(newSubjectUri);

            // Normalize the predicate
            Property oldPredicate = stmt.getPredicate();
            String oldPredicateUri = oldPredicate.getURI();
            String newPredicateUri = normalizeCase(oldPredicateUri);
            newPredicateUri = normalizeWhitespace(newPredicateUri);
            newPredicateUri = normalizeSpecialCharacters(newPredicateUri);
            Property newPredicate = ResourceFactory.createProperty(newPredicateUri);

            // Normalize the object
            RDFNode oldObject = stmt.getObject();
            RDFNode newObject = oldObject;
            if (oldObject.isResource()) {
                Resource oldObjectResource = oldObject.asResource();
                String oldObjectUri = oldObjectResource.getURI();
                String newObjectUri = normalizeCase(oldObjectUri);
                newObjectUri = normalizeWhitespace(newObjectUri);
                newObjectUri = normalizeSpecialCharacters(newObjectUri);
                newObject = ResourceFactory.createResource(newObjectUri);
            } else if (oldObject.isLiteral()) {
                Literal oldLiteral = oldObject.asLiteral();
                String oldLiteralValue = oldLiteral.getString();
                String newLiteralValue = normalizeCase(oldLiteralValue);
                newLiteralValue = normalizeWhitespace(newLiteralValue);
                newLiteralValue = normalizeSpecialCharacters(newLiteralValue);
                newObject = ResourceFactory.createStringLiteral(newLiteralValue);
            }
            // Create a new statement with the normalized subject, predicate, and object, and add it to the normalized model
            Statement newStatement = ResourceFactory.createStatement(newSubject, newPredicate, newObject);
            normalizedModel.add(newStatement);
            normalizedModel=normalizeDates(normalizedModel);
        }
        return normalizedModel;
    }

}
