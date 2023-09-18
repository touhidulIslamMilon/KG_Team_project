package FinalPackage;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class LoadRDF {

    public static Model getModel(String filename){
        Model model = ModelFactory.createDefaultModel();
        model.read(filename);
        return model;
    }

    public static Map<Model, Date> getModelDate(String filename) {
        Map<Model, Date> modelDateMap = new HashMap<>();

        // Create an empty Jena Model
        Model model = ModelFactory.createDefaultModel();

        // Load the RDF data from the file
        FileManager.get().readModel(model, filename);

        // Get the creation date of the RDF file
        Date creationDate = getFileCreationDate(filename);

        // Associate the model with its creation date
        modelDateMap.put(model, creationDate);

        return modelDateMap;
    }

    private static Date getFileCreationDate(String filename) {
        File file = new File(filename);
        try {
            java.nio.file.Path path = file.toPath();
            BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
            return new Date(attributes.creationTime().toMillis());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Map<Model, Integer> assignPriorityToLatest(Map<Model, Date> modelDateMap) {
        Map<Model, Integer> modelPriorityMap = new HashMap<>();

        // Create a list of entries from the input map, sorted by date in descending order
        List<Map.Entry<Model, Date>> sortedEntries = new ArrayList<>(modelDateMap.entrySet());
        sortedEntries.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));

        // Assign priorities (integers) to models based on their order in the sorted list
        int priority = 1;
        for (Map.Entry<Model, Date> entry : sortedEntries) {
            modelPriorityMap.put(entry.getKey(), priority);
            priority++;
        }

        return modelPriorityMap;
    }




}
