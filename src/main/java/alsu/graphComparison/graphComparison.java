package alsu.graphComparison;
import org.apache.jena.rdf.model.*;

import java.util.*;

public class graphComparison {


    // function for counting similar predicates
    public static int countSimilarProperties(Model model1, Model model2) {
        // Create two sets to hold the properties of the two models
        Set<Property> properties1 = new HashSet<>();
        Set<Property> properties2 = new HashSet<>();

        // Extract properties from the first model
        StmtIterator iter1 = model1.listStatements();
        while (iter1.hasNext()) {
            Statement stmt = iter1.nextStatement();
            properties1.add(stmt.getPredicate());
        }

        // Extract properties from the second model
        StmtIterator iter2 = model2.listStatements();
        while (iter2.hasNext()) {
            Statement stmt = iter2.nextStatement();
            properties2.add(stmt.getPredicate());
        }

        // Count the number of similar properties
        // (we use the built-in retainAll method of the Set class, which retains only the elements in this set that are contained in the specified collection)
        properties1.retainAll(properties2);
        return properties1.size();
    }

    // function for counting the number of similar objects
    public static int countSimilarObjects(Model model1, Model model2) {
        Set<RDFNode> objectSet1 = new HashSet<>();
        StmtIterator iter1 = model1.listStatements();
        while (iter1.hasNext()) {
            Statement stmt = iter1.next();
            objectSet1.add(stmt.getObject());
        }

        int count = 0;
        StmtIterator iter2 = model2.listStatements();
        while (iter2.hasNext()) {
            Statement stmt = iter2.next();
            if (objectSet1.contains(stmt.getObject())) {
                count++;
            }
        }

        return count;
    }

    public static int countSimilarSubjects(Model model1, Model model2) {
        Set<Resource> subjectSet1 = new HashSet<>();
        StmtIterator iter1 = model1.listStatements();
        while (iter1.hasNext()) {
            Statement stmt = iter1.next();
            if (!subjectSet1.contains(stmt.getSubject())) {
                subjectSet1.add(stmt.getSubject());
            }
        }
        int count = 0;
        Set<Resource> subjectSet2 = new HashSet<>();
        StmtIterator iter2 = model2.listStatements();
        while (iter2.hasNext()) {
            Statement stmt = iter2.next();
            if (!subjectSet2.contains(stmt.getSubject())) {
                subjectSet2.add(stmt.getSubject());

            }
        }


        if (subjectSet2.size() > subjectSet1.size()) {
            Iterator<Resource> iterator = subjectSet2.iterator();
            while (iterator.hasNext()) {
                Resource element = iterator.next();
                if (subjectSet1.contains(element)) {
                    count++;
                }
            }
        } else {
            Iterator<Resource> iterator1 = subjectSet1.iterator();
            while (iterator1.hasNext()) {
                Resource element = iterator1.next();
                if (subjectSet2.contains(element)) {
                    count++;
                }
            }
        }
        return count;
    }

    // comparing the size of two graphs

    // showing the same predicates with their types

    public static Map<String, Set<String>> comparePredicatesAndObjectTypes(Model model1, Model model2) {
        Map<String, Set<String>> sharedPredicatesWithTypes = new HashMap<>();

        StmtIterator iter1 = model1.listStatements();
        while (iter1.hasNext()) {
            Statement stmt1 = iter1.nextStatement();
            Property predicate1 = stmt1.getPredicate();

            StmtIterator iter2 = model2.listStatements();
            while (iter2.hasNext()) {
                Statement stmt2 = iter2.nextStatement();
                Property predicate2 = stmt2.getPredicate();

                if (predicate1.equals(predicate2)) {
                    // If predicates are the same, add the object types to the map
                    String objectType1 = stmt1.getObject().getClass().getSimpleName();
                    String objectType2 = stmt2.getObject().getClass().getSimpleName();

                    if (sharedPredicatesWithTypes.containsKey(predicate1.getURI())) {
                        sharedPredicatesWithTypes.get(predicate1.getURI()).add(objectType1);
                        sharedPredicatesWithTypes.get(predicate1.getURI()).add(objectType2);
                    } else {
                        Set<String> newSet = new HashSet<>();
                        newSet.add(objectType1);
                        newSet.add(objectType2);
                        sharedPredicatesWithTypes.put(predicate1.getURI(), newSet);
                    }
                }
            }
        }

        return sharedPredicatesWithTypes;
    }


    // s and predicate same, object different -> conflict. Print them out and count them.

    public static Map<Resource, Map<Property, Set<RDFNode>>> getSubjectsWithDifferentObjects(Set<Model> models) {
        Map<Resource, Map<Property, Set<RDFNode>>> subjectsWithDifferentObjects = new HashMap<>();

        for (Model model : models) {
            StmtIterator stmtIterator = model.listStatements();

            while (stmtIterator.hasNext()) {
                Statement stmt = stmtIterator.nextStatement();
                Resource subject = stmt.getSubject();
                Property predicate = stmt.getPredicate();
                RDFNode object = stmt.getObject();

                subjectsWithDifferentObjects.computeIfAbsent(subject, k -> new HashMap<>())
                        .computeIfAbsent(predicate, k -> new HashSet<>())
                        .add(object);
            }
        }

        subjectsWithDifferentObjects.entrySet().removeIf(e -> e.getValue().values().stream().allMatch(objects -> objects.size() <= 1));

        return subjectsWithDifferentObjects;
    }

    // displaying the above output in a normal way

    public static void displaySubjectsObjects(Map<Resource, Map<Property, Set<RDFNode>>> subjectsObjectsMap) {
        for (Map.Entry<Resource, Map<Property, Set<RDFNode>>> entry : subjectsObjectsMap.entrySet()) {
            System.out.println("Subject: " + entry.getKey().getURI());

            for (Map.Entry<Property, Set<RDFNode>> subEntry : entry.getValue().entrySet()) {
                System.out.println("\tPredicate: " + subEntry.getKey().getURI());

                for (RDFNode object : subEntry.getValue()) {
                    if (object.isResource()) {
                        System.out.println("\t\tObject: " + object.asResource().getURI());
                    } else {
                        System.out.println("\t\tObject: " + object.asLiteral().getString());
                    }
                }
            }
        }
    }

    // number of predicates per subject.

    public static Map<Resource, Integer> countConnections(Set<Model> models) {
        Map<Resource, Integer> connectionCount = new HashMap<>();

        for (Model model : models) {
            StmtIterator stmtIterator = model.listStatements();

            while (stmtIterator.hasNext()) {
                Statement stmt = stmtIterator.nextStatement();
                Resource subject = stmt.getSubject();

                if (connectionCount.containsKey(subject)) {
                    connectionCount.put(subject, connectionCount.get(subject) + 1);
                } else {
                    connectionCount.put(subject, 1);
                }
            }
        }

        return connectionCount;
    }

    // display the above function in a normal way

    public static void compareGraphConnections(Map<Resource, Integer> connections1, Map<Resource, Integer> connections2) {
        for (Resource subject : connections1.keySet()) {
            if (connections2.containsKey(subject)) {
                int difference = Math.abs(connections1.get(subject) - connections2.get(subject));
                System.out.println("Subject: " + subject.getURI()
                        + " has " + connections1.get(subject) + " predicates in the first graph and "
                        + connections2.get(subject) + " predicates in the second graph.");
            } else {
                System.out.println("Subject: " + subject.getURI()
                        + " is only present in the first graph and has " + connections1.get(subject) + " predicates.");
            }
        }

        for (Resource subject : connections2.keySet()) {
            if (!connections1.containsKey(subject)) {
                System.out.println("Subject: " + subject.getURI()
                        + " is only present in the second graph and has " + connections2.get(subject) + " predicates.");
            }
        }
    }


    // number of connections per entity
    // similarity measure
}
