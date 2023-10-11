package FinalPackage;

import FinalPackage.Merging.Merger;
import org.apache.jena.rdf.model.*;

import java.util.*;

import static FinalPackage.LoadRDF.getModel;
import static FinalPackage.LoadRDF.readAllTestCases;
import static FinalPackage.Merging.FunctionalPropertyFinder.findFunctionalProperties;
import static FinalPackage.analyzeGraph.SophisticatedAnalysis.*;
import static FinalPackage.analyzeGraph.SophisticatedAnalysis.showPredicatesAndObjects2;
import static FinalPackage.analyzeGraph.analyzeGraph.*;
import static FinalPackage.conflictDetection.ConflictFinder.*;
import static FinalPackage.analyzeGraph.analyzeGraph.*;

public class MainForConflictResolutionDisplay {
    public static void main(String[] args) {
        List<Model> models = readAllTestCases();
        Model mergedModel = Merger.mergeGraphs(models);

        System.out.println("For string conflict");
        retrieveStatementsForPredicate(mergedModel, "http://www.w.org/2000/01/rdf-schema#label");

        System.out.println("For integer conflict");
        retrieveStatementsForPredicate(mergedModel, "http://dbkwik.webdatacommons.org/memory-beta.wikia.com/property/insignia");

        System.out.println("For decimal conflict");
        retrieveStatementsForPredicate(mergedModel, "http://dbkwik.webdatacommons.org/memory-beta.wikia.com/property/dp");

        System.out.println("For date conflict");
        retrieveStatementsForPredicate(mergedModel, "http://dbkwik.webdatacommons.orq/stexpanded.wikia.com/property/originaldate");

        System.out.println("For URI conflict");
        retrieveStatementsForPredicate(mergedModel, "http://dbkwik.webdatacommons.org/memory-beta.wikia.com/property/servicePeriod");


    }
}
