package FinalPackage;

import FinalPackage.Merging.Merger;
import org.apache.jena.rdf.model.Model;

import java.util.List;

import static FinalPackage.LoadRDF.readAllTestCases;
import static FinalPackage.conflictDetection.ConflictFinder.identifyAndPrintConflicts;

public class mainForConflictDetection {
    public static void main(String[] args) {

        List<Model> models = readAllTestCases();
        //Model mergedModel = Merger.mergeGraphs(models);
        identifyAndPrintConflicts(models);

    }
}
