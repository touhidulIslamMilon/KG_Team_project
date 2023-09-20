package tests;

import FinalPackage.LoadRDF;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoadRDFTest {

    public static void main(String[] args) {
        String RDF_FILE = "src/test/testResources/starwars1Test.rdf";

        // Test getModel
        Model readModel = LoadRDF.getModel(RDF_FILE);
        System.out.println("Testing getModel:");
        System.out.println("Is the model null? " + (readModel == null));
        System.out.println();

        // Test getFileCreationDate
        Date creationDate = LoadRDF.getFileCreationDate(RDF_FILE);
        Date expectedCreationDate = createDate(2023, 9, 20, 0, 0, 0);
        System.out.println("Testing getFileCreationDate:");
        System.out.println("Is the creation date null? " + (creationDate == null));
        System.out.println("Is the creation date as expected? " + (creationDate.equals(expectedCreationDate)));
        System.out.println();

        // Test getModelDateMap
        System.out.println("Testing getModelDateMap:");
        Map<Model, Date> modelDateMap = LoadRDF.getModelDateMap(RDF_FILE);
        System.out.println("Is the modelDateMap null? " + (modelDateMap == null));
        System.out.println("Is the modelDateMap empty? " + (modelDateMap != null && modelDateMap.isEmpty()));

        // Test assignPriorityToLatest
        Map<Model, Date> testModelDateMap = new HashMap<>();
        Model model1 = ModelFactory.createDefaultModel();
        Date date1 = createDate(2023, 9, 20, 0, 0, 0);
        testModelDateMap.put(model1, date1);
        Model model2 = ModelFactory.createDefaultModel();
        Date date2 = createDate(2023, 10, 27, 0, 0, 0);
        testModelDateMap.put(model2, date2);

        System.out.println("Testing assignPriorityToLatest:");
        Map<Model, Integer> modelPriorityMap = LoadRDF.assignPriorityToLatest(testModelDateMap);
        System.out.println("Is the modelPriorityMap null? " + (modelPriorityMap == null));
        System.out.println("Is model2 prioritized over model1? " + (modelPriorityMap != null && modelPriorityMap.get(model2) > modelPriorityMap.get(model1)));
    }

    // Helper method to create a Date object
    private static Date createDate(int year, int month, int day, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, minute, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}