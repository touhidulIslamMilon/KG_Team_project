package FinalPackage;

import FinalPackage.Merging.Merger;
import org.apache.jena.rdf.model.*;

import java.util.*;
import static FinalPackage.LoadRDF.readAllTestCases;
import static FinalPackage.analyzeGraph.analyzeGraph.*;

public class MainForConflictResolutionDisplay {
    public static void main(String[] args) {
        List<Model> models = readAllTestCases();
        Model mergedModel = Merger.mergeGraphs(models);

        System.out.println("For string conflict");
        retrieveStatementsForSubjectPredicate(mergedModel,"http://dbkwik.webdatacommons.org/marvel.wikia.com/resource/Category:Kevin_McTaggart_(Earth-10005)/Appearances", "http://www.w.org/2000/01/rdf-schema#label");

        System.out.println("For integer conflict");
        retrieveStatementsForSubjectPredicate(mergedModel, "http://dbkwik.webdatacommons.org/memory-beta.wikia.com/resource/Kopman", "http://dbkwik.webdatacommons.org/memory-beta.wikia.com/property/insignia");

        System.out.println("For decimal conflict");
        retrieveStatementsForSubjectPredicate(mergedModel, "http://dbkwik.webdatacommons.org/memory-beta.wikia.com/resource/2400s","http://dbkwik.webdatacommons.org/memory-beta.wikia.com/property/dp");

        System.out.println("For date conflict");
        retrieveStatementsForSubjectPredicate(mergedModel, "http://dbkwik.webdatacommons.org/stexpanded.wikia.com/resource/When_It_Rains...","http://dbkwik.webdatacommons.orq/stexpanded.wikia.com/property/originaldate");

        System.out.println("For URI conflict");
        retrieveStatementsForSubjectPredicate(mergedModel, "http://dbkwik.webdatacommons.org/memory-beta.wikia.com/resource/D%27deridex_class","http://dbkwik.webdatacommons.org/memory-beta.wikia.com/property/servicePeriod");


    }
}
