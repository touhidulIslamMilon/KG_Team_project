package FinalPackage;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class LoadRDF {

    public static Model getModel(String filename){
        Model model = ModelFactory.createDefaultModel();
        model.read(filename);
        return model;
    }

    public static Map<Model, Date> getModelDateMap(String filename) {
        Map<Model, Date> modelDateMap = new HashMap<>();

        // Create an empty Jena Model
        Model model = ModelFactory.createDefaultModel();

        // Load the RDF data from the file
        model.read(filename);

        // Get the creation date of the RDF file
        Date creationDate = getFileCreationDate(filename);

        // Associate the model with its creation date
        modelDateMap.put(model, creationDate);

        return modelDateMap;
    }

    public static Date getFileCreationDate(String filePath) {
        try {
            Path path = FileSystems.getDefault().getPath(filePath);
            BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);

            // Get the creation time attribute
            long creationTimeMillis = attributes.creationTime().toMillis();

            // Convert the creation time to a Date object
            Date creationDate = new Date(creationTimeMillis);

            //System.out.println("File Creation Date: " + creationDate);
            return creationDate;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<Model, Integer> assignPriorityToLatest(Map<Model, Date> modelDateMap) {
        Map<Model, Integer> modelPriorityMap = new HashMap<>();

        // Create a list of entries from the input map, sorted by date in descending order
        List<Map.Entry<Model, Date>> sortedEntries = new ArrayList<>(modelDateMap.entrySet());
        sortedEntries.sort(Comparator.comparing(Map.Entry::getValue));

        // Assign priorities (integers) to models based on their order in the sorted list
        int priority = 1;
        for (Map.Entry<Model, Date> entry : sortedEntries) {
            modelPriorityMap.put(entry.getKey(), priority);
            priority++;
        }

        return modelPriorityMap;
    }




}


/* //Use for Resource Folder
    public static Date getFileCreationDate(String filename) {
        // Use the ClassLoader to load the file as a resource
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(filename);

        if (inputStream != null) {
            try {
                // Create a temporary file to represent the resource
                Path tempFile = Files.createTempFile("resource", ".tmp");

                // Copy the resource InputStream to the temporary file
                Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

                // Get the creation time attribute of the temporary file
                BasicFileAttributes attributes = Files.readAttributes(tempFile, BasicFileAttributes.class);

                // Delete the temporary file
                Files.delete(tempFile);

                // Return the creation time as a Date
                return new Date(attributes.creationTime().toMillis());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    // Close the input stream
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.err.println("File not found in resources: " + filename);
        }

        return null;
    }
*/