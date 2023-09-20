package tests;

import FinalPackage.LoadRDF;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class LoadRDFTest {

    private static final String RDF_FILE = "starwars1.rdf"; // Update the file path

    @Test
    public void testGetModel() {
        Model readModel = LoadRDF.getModel(RDF_FILE);
        assertNotNull(readModel);

        Model expectedModel = ModelFactory.createDefaultModel();
        expectedModel.read(RDF_FILE);

        assertTrue(readModel.isIsomorphicWith(expectedModel));
    }

    @Test
    public void testGetModelDate() {
        // Load the RDF file and get the model-date mapping
        Map<Model, Date> modelDateMap = LoadRDF.getModelDate(RDF_FILE);

        Model expectedModel = ModelFactory.createDefaultModel();
        expectedModel.read(RDF_FILE);

        // Ensure that the map is not null and not empty
        assertNotNull(modelDateMap);
        assertFalse(modelDateMap.isEmpty());

        // For each expected model and date, compare it with the loaded data
        for (Map.Entry<Model, Date> entry : modelDateMap.entrySet()) {
            Model readModel = entry.getKey();
            Date creationDate = entry.getValue();

            //Create expectedDate
            Date expectedCreationDate = createDate(2023, 9, 20, 0, 0, 0); // Wed Sep 20 CEST 2023

            // Create a Calendar instance and set it to the specified Date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(creationDate);

            // Reset the hour, minute, and second components to zero
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            // Get the modified Date object
            Date dateWithTimeReset = calendar.getTime();

            // Perform assertions based on your criteria
            assertTrue(readModel.isIsomorphicWith(expectedModel)); // Implement this method to check if the model is the same.
            assertEquals(dateWithTimeReset, expectedCreationDate); // Implement this method to get the expected date for the model.
        }
    }


    @Test
    public void testGetFileCreationDate() {
        //Create expectedDate
        Date expectedCreationDate = createDate(2023, 9, 20, 0, 0, 0); // Wed Sep 20 CEST 2023

        Date creationDate = LoadRDF.getFileCreationDate(RDF_FILE);
        // Create a Calendar instance and set it to the specified Date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(creationDate);

        // Reset the hour, minute, and second components to zero
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // Get the modified Date object
        Date dateWithTimeReset = calendar.getTime();

        assertNotNull(creationDate);
        assertEquals(expectedCreationDate, dateWithTimeReset);
    }

    @Test
    public void testAssignPriorityToLatest() {
        // Create a map with two entities (Model objects) and different dates
        Map<Model, Date> modelDateMap = new HashMap<>();

        // Create Model1 with an earlier date
        Model model1 = ModelFactory.createDefaultModel();
        Date date1 = createDate(2023, 9, 20, 0, 0, 0);
        modelDateMap.put(model1, date1);

        // Create Model2 with a later date
        Model model2 = ModelFactory.createDefaultModel();
        Date date2 = createDate(2023, 10,27, 0, 0, 0); // Date2 is at 12:00:00
        modelDateMap.put(model2, date2);

        // Test the assignPriorityToLatest method
        Map<Model, Integer> modelPriorityMap = LoadRDF.assignPriorityToLatest(modelDateMap);
        assertNotNull(modelPriorityMap);
        assertFalse(modelPriorityMap.isEmpty());

        // Ensure that the sorting worked correctly
        assertTrue(modelPriorityMap.get(model1) < modelPriorityMap.get(model2));

    }


    // Helper method to create a Date object
    private Date createDate(int year, int month, int day, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, minute, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}