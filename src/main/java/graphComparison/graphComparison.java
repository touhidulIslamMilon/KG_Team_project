package graphComparison;
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

    // s and predicate same, object different -> conflict. Print them out
    // number of connections per entity
    // similarity measure
}
