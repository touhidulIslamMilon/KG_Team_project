package FinalPackage;

import de.uni_mannheim.informatik.dws.melt.matching_data.TrackRepository;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoadRDF {

    public static Model getModel(String filename) {
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

            //System.out.println("File Creation Date: " + creationDate);
            return new Date(creationTimeMillis);
        } catch (IOException e) {
            // Log the error instead of printing the stack trace
            Logger logger = Logger.getLogger(LoadRDF.class.getName());
            logger.log(Level.SEVERE, "An error occurred while getting file creation date", e);
        }
        return null;
    }

    public static Map<Model, Integer> assignPriorityToLatest(Map<Model, Date> modelDateMap) {
        Map<Model, Integer> modelPriorityMap = new HashMap<>();

        // Create a list of entries from the input map, sorted by date in descending order
        List<Map.Entry<Model, Date>> sortedEntries = new ArrayList<>(modelDateMap.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue());

        // Assign priorities (integers) to models based on their order in the sorted list
        int priority = 1;
        for (Map.Entry<Model, Date> entry : sortedEntries) {
            modelPriorityMap.put(entry.getKey(), priority);
            priority++;
        }

        return modelPriorityMap;
    }

    public static List<Model> readAllTestCases() {
        List<Model> testModels = new ArrayList<>();

        Model swtorKG = TrackRepository.Knowledgegraph.V4.getTestCase("starwars-swtor").getTargetOntology(OntModel.class);
        testModels.add(swtorKG);

        Model swgKG = TrackRepository.Knowledgegraph.V4.getTestCase("starwars-swg").getTargetOntology(OntModel.class);
        testModels.add( swgKG);

        Model marvelKG = TrackRepository.Knowledgegraph.V4.getTestCase("marvelcinematicuniverse-marvel").getTargetOntology(OntModel.class);
        testModels.add(marvelKG);

        Model mbetaKG = TrackRepository.Knowledgegraph.V4.getTestCase("memoryalpha-memorybeta").getTargetOntology(OntModel.class);
        testModels.add(mbetaKG);

        Model stexpandedKG = TrackRepository.Knowledgegraph.V4.getTestCase("memoryalpha-stexpanded").getTargetOntology(OntModel.class);
        testModels.add(stexpandedKG);

        return testModels;
    }

    public static Model readTestCase(String filename) {
        return TrackRepository.Knowledgegraph.V4.getTestCase(filename).getTargetOntology(OntModel.class);
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