package FinalPackage.Merging.Strategies;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import com.google.common.collect.ListMultimap;

import java.util.Map;
import java.util.Scanner;

import org.apache.jena.rdf.model.*;

public class ManualReviewResolutionStrategy implements Strategy {
    @Override
    public RDFNode resolveConflict(ListMultimap<RDFNode, Integer> objects, Resource subject, Property predicate) {
        // Print the objects for manual review
        System.out.println("Conflict Resolution - Manual Review:");
        System.out.println("Subject: " + subject + "\nPredicate: " + predicate);
        int index = 0;
        
        for (RDFNode object : objects.keySet()) {
            index++;
            System.out.println(index + ". Object: " + object);
        }
        System.out.println(index + 1 + ". Manual Edit: " );
        // Ask the user to choose an object by entering the corresponding index
        Scanner scanner = new Scanner(System.in);
        int userChoice = -1;
        while (userChoice < 1 || userChoice > objects.size()+1) {
            System.out.print("Enter the index of the selected object (1-" +(index +1) + "): ");
            try {
                userChoice = scanner.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid index.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
        if(userChoice == index + 1) {
            System.out.println("Please edit the object manually");
            return ResourceFactory.createResource(scanner.next());
        }
        // Return the selected object based on user choice
        int selectedIndex = userChoice - 1;
        return (RDFNode) objects.keySet().toArray()[selectedIndex];
    }
}
