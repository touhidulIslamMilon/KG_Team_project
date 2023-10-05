package FinalPackage.analyzeGraph;

import FinalPackage.Merging.FunctionalPropertyFinder;
import FinalPackage.Merging.ObjectResolution;
import org.apache.jena.rdf.model.*;

import java.util.*;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ConflictDatabase {

    private final Connection databaseConnection;

    public ConflictDatabase() {
        // Initialize the database connection (using SQLite in-memory database for this example)
        try {
            Class.forName("org.sqlite.JDBC");
            databaseConnection = DriverManager.getConnection("jdbc:sqlite::memory:");
            createTable();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Failed to initialize the database connection.", e);
        }
    }

    private void createTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS conflicts " +
                "(subject TEXT, predicate TEXT, object TEXT)";
        try (PreparedStatement statement = databaseConnection.prepareStatement(createTableSQL)) {
            statement.executeUpdate();
        }
    }

    public void findConflicts(List<Model> models) {
        Model targetModel = ModelFactory.createDefaultModel();
        Model resolvedModel = ModelFactory.createDefaultModel();

        List<Property> functionalProperties = FunctionalPropertyFinder.findFunctionalProperties(models);
        System.out.println("Func ended");

        for (Model model : models) {
            StmtIterator iter = model.listStatements();
            while (iter.hasNext()) {
                Statement stmt = iter.nextStatement();
                Resource subject = stmt.getSubject();
                Property predicate = stmt.getPredicate();

                if (!resolvedModel.contains(subject, predicate)) {
                    if (!targetModel.contains(subject, predicate)) {
                        targetModel.add(stmt);
                    } else {
                        targetModel.removeAll(subject, predicate, null);
                        if (functionalProperties.contains(predicate)) {
                            addConflictingObjects(models, subject, predicate);
                            targetModel.add(stmt);
                            resolvedModel.add(stmt);
                        } else {
                            Model allStatements = ObjectResolution.getDistinctStatements(models, subject, predicate);
                            targetModel.add(allStatements);
                            resolvedModel.add(stmt);

                        }
                    }

                }
            }
        }
    }

    public void addConflictingObjects(List<Model> models, Resource subject, Property predicate) {
        List<RDFNode> allObjects = ObjectResolution.getAllObjects(models, subject, predicate);

        RDFNode firstObject = allObjects.iterator().next();
        for (RDFNode object : allObjects) {
            if (!firstObject.equals(object)) {
                insertConflict(subject, predicate, allObjects);
            }
        }
    }

    public void insertConflict(Resource subject, Property predicate, List<RDFNode> conflictingObjects) {
        String insertSQL = "INSERT INTO conflicts (subject, predicate, object) VALUES (?, ?, ?)";
        try (PreparedStatement statement = databaseConnection.prepareStatement(insertSQL)) {
            String subjectStr = subject.toString();
            String predicateStr = predicate.toString();
            for (RDFNode object : conflictingObjects) {
                String objectStr = object.toString();
                statement.setString(1, subjectStr);
                statement.setString(2, predicateStr);
                statement.setString(3, objectStr);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert conflict into the database.", e);
        }
    }


    public void printAllConflicts() {
        String selectAllSQL = "SELECT subject, predicate, object FROM conflicts";
        try (PreparedStatement statement = databaseConnection.prepareStatement(selectAllSQL);
             ResultSet resultSet = statement.executeQuery()) {
            // Use a map to group conflicts by subject and predicate
            Map<String, Map<String, List<String>>> conflictGroups = new HashMap<>();
            while (resultSet.next()) {
                String subject = resultSet.getString("subject");
                String predicate = resultSet.getString("predicate");
                String object = resultSet.getString("object");

                // Create a key for grouping based on subject and predicate
                String key = subject + " - " + predicate;

                // Get or create a group for this key
                conflictGroups.computeIfAbsent(key, k -> new HashMap<>())
                        .computeIfAbsent("objects", k -> new ArrayList<>())
                        .add(object);
            }

            if (conflictGroups.isEmpty()) {
                System.out.println("No conflicts found.");
            } else {
                System.out.println("Conflicts:");
                for (Map.Entry<String, Map<String, List<String>>> entry : conflictGroups.entrySet()) {
                    String[] keyParts = entry.getKey().split(" - ");
                    String subject = keyParts[0];
                    String predicate = keyParts[1];
                    List<String> objects = entry.getValue().get("objects");

                    System.out.println("Subject: " + subject + "\nPredicate: " + predicate);
                    System.out.println("Objects: " + String.join(", ", objects));
                    System.out.println(); // Add a separator between conflict groups
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve conflicts from the database.", e);
        }


        /*String selectAllSQL = "SELECT subject, predicate, object FROM conflicts";
        try (PreparedStatement statement = databaseConnection.prepareStatement(selectAllSQL);
             ResultSet resultSet = statement.executeQuery()) {
            List<String> conflicts = new ArrayList<>();
            while (resultSet.next()) {
                String subject = resultSet.getString("subject");
                String predicate = resultSet.getString("predicate");
                String object = resultSet.getString("object");
                conflicts.add(String.format("Subject: %s, Predicate: %s, Object: %s", subject, predicate, object));
            }
            if (conflicts.isEmpty()) {
                System.out.println("No conflicts found.");
            } else {
                System.out.println("Conflicts:");
                for (String conflict : conflicts) {
                    System.out.println(conflict);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve conflicts from the database.", e);
        }*/
    }

    // Add methods to query conflicts from the database as needed

    public void close() {
        try {
            databaseConnection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to close the database connection.", e);
        }
    }



}



/*
@Getter
public class ConflictDatabase {

    private final Map<SubjectPredicatePair, List<RDFNode>> conflicts;


    public ConflictDatabase(){
        this.conflicts = new HashMap<>();
    }

    public void findConflicts (List<Model> models) {
        Model targetModel = ModelFactory.createDefaultModel();
        Model resolvedModel = ModelFactory.createDefaultModel();

        List<Property> functionalProperties = FunctionalPropertyFinder.findFunctionalProperties(models);
        System.out.println("Func ended");

        for (Model model : models) {
            StmtIterator iter = model.listStatements();
            while (iter.hasNext()) {
                Statement stmt = iter.nextStatement();
                Resource subject = stmt.getSubject();
                Property predicate = stmt.getPredicate();

                if (!resolvedModel.contains(subject, predicate)) {
                    if (!targetModel.contains(subject, predicate)) {
                        targetModel.add(stmt);
                    } else {
                        targetModel.removeAll(subject, predicate, null);
                        if (functionalProperties.contains(predicate)) {
                            addConflictingObjects(models, subject, predicate);
                            targetModel.add(stmt);
                            resolvedModel.add(stmt);
                        } else {
                            Model allStatements = ObjectResolution.getDistinctStatements(models, subject, predicate);
                            targetModel.add(allStatements);
                            resolvedModel.add(stmt);

                        }
                    }

                }
            }
        }
    }

    public void addConflictingObjects(List<Model> models, Resource subject, Property predicate) {
        List<RDFNode> allObjects = ObjectResolution.getAllObjects(models, subject, predicate);

        RDFNode firstObject = allObjects.iterator().next();
        for (RDFNode object : allObjects) {
            if (!firstObject.equals(object)) {
                SubjectPredicatePair pair = new SubjectPredicatePair(subject, predicate);
                conflicts.put(pair, allObjects);
            }
        }
    }



    @Getter
    public static class SubjectPredicatePair {
        private final Resource subject;
        private final Property predicate;

        public SubjectPredicatePair(Resource subject, Property predicate) {
            this.subject = subject;
            this.predicate = predicate;
        }

        @Override
        public int hashCode() {
            return Objects.hash(subject, predicate);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            SubjectPredicatePair other = (SubjectPredicatePair) obj;
            return Objects.equals(subject, other.getSubject()) && Objects.equals(predicate, other.getPredicate());
        }
    }

}
*/
