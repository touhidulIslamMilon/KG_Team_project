package max.kg_merger;

import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.vocabulary.RDFS;

/*
Perform entity alignment or schema matching between the knowledge graphs to identify corresponding entities or concepts.
By aligning similar entities, you can merge the graphs by connecting related nodes and merging their attributes or properties.
 */
public class alignmentBasedMerge {

    public static Model mergeGraphs(Model graph1, Model graph2){
        // Create an alignment reasoner
        Reasoner reasoner = ReasonerRegistry.getRDFSReasoner();

        // Align the models using RDFS reasoning
        InfModel infModel1 = ModelFactory.createInfModel(reasoner, graph1);
        InfModel infModel2 = ModelFactory.createInfModel(reasoner, graph2);

        // Create a merged model
        Model mergedModel = ModelFactory.createDefaultModel();

        // Iterate over the resources in model1
        ResIterator iter = infModel1.listSubjects();
        while (iter.hasNext()) {
            Resource resource1 = iter.nextResource();

            // Find the corresponding resource in model2
            ResIterator alignedIter = infModel2.listSubjects();
            while (alignedIter.hasNext()) {
                Resource resource2 = alignedIter.nextResource();

                // Check if the resources are aligned based on their labels
                if (hasSameLabel(resource1, resource2)) {
                    // Merge the properties of aligned resources
                    StmtIterator stmtIter = infModel1.listStatements(resource1, null, (RDFNode) null);
                    while (stmtIter.hasNext()) {
                        Statement stmt = stmtIter.nextStatement();
                        mergedModel.add(stmt);
                    }

                    stmtIter = infModel2.listStatements(resource2, null, (RDFNode) null);
                    while (stmtIter.hasNext()) {
                        Statement stmt = stmtIter.nextStatement();
                        mergedModel.add(stmt);
                    }
                }
            }
        }

        return mergedModel;
    }

    // Helper method to check if two resources have the same label
    private static boolean hasSameLabel(Resource resource1, Resource resource2) {
        Statement labelStmt1 = resource1.getProperty(RDFS.label);
        Statement labelStmt2 = resource2.getProperty(RDFS.label);

        if (labelStmt1 == null || labelStmt2 == null) {
            return false; // At least one resource doesn't have a label property
        }

        String label1 = labelStmt1.getObject().toString();
        String label2 = labelStmt2.getObject().toString();
        return label1.equals(label2);
    }

    public static void main(String[] args) {

    }



}

