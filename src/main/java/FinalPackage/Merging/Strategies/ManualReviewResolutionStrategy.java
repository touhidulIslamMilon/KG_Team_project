package FinalPackage.Merging.Strategies;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import com.google.common.collect.ListMultimap;

import java.util.Map;
import java.util.Scanner;

public class ManualReviewResolutionStrategy implements Strategy {
    @Override
    public RDFNode resolveConflict(ListMultimap<RDFNode, Integer> objects, Resource subject, Property predicate) {
        // Print the objects for manual review
        System.out.println("Conflict Resolution - Manual Review:");
        int index = 0;
        for (RDFNode object : objects.keySet()) {
            index++;
            System.out.println(index + ". Object: " + object);
        }

        // Ask the user to choose an object by entering the corresponding index
        Scanner scanner = new Scanner(System.in);
        int userChoice = -1;
        while (userChoice < 1 || userChoice > objects.size()) {
            System.out.print("Enter the index of the selected object (1-" + index + "): ");
            try {
                userChoice = scanner.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid index.");
                scanner.nextLine(); // Clear the invalid input
            }
        }

        // Return the selected object based on user choice
        int selectedIndex = userChoice - 1;
        return (RDFNode) objects.keySet().toArray()[selectedIndex];
    }
}
