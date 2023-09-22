package Old.alsu.kg_fusion;

import Old.alsu.WeightedModel;
import org.apache.jena.rdf.model.*;

import java.util.List;

import static FinalPackage.Old.FunctionalPropertyDetector.isFunctionalProperty;

public class WeightedFusion {

    public static Model WeightedFusion(List<WeightedModel> list) {
        // create fused model
        Model fusedModel = ModelFactory.createDefaultModel();
        Model firstModel = list.get(0).getModel();
        fusedModel.add(firstModel.listStatements());

        for (WeightedModel modelFromList : list) {
            Model model1 = modelFromList.getModel();

            // Iterate over all the statements in the model
            StmtIterator stmtIterator2 = model1.listStatements();
            while (stmtIterator2.hasNext()) {
                Statement stmt2 = stmtIterator2.next();
                Property prop2 = stmt2.getPredicate();
                RDFNode lit2 = stmt2.getObject();

                // If fusedModel already contains a statement with the same subject and predicate
                boolean statementExists = fusedModel.contains(stmt2.getSubject(), prop2);

                if (statementExists) {
                    if (isFunctionalProperty(fusedModel, prop2)) {
                    }
                    // If property is not functional, add statement without any checks
                    else {
                        fusedModel.add(stmt2);
                    }
                }
                // If no such statement exists, just add it
                else {
                    fusedModel.add(stmt2);
                }
            }
        }
        return fusedModel;
    }

}


