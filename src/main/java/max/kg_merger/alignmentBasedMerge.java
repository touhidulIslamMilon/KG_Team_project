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

            // Find the corresponding resource in model2 based on property alignment
            ResIterator alignedIter = infModel2.listSubjects();
            while (alignedIter.hasNext()) {
                Resource resource2 = alignedIter.nextResource();

                // Check if the resources are aligned based on property alignment
                if (arePropertiesAligned(resource1, resource2)) {
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

    // Helper method to check if two resources have aligned properties
    private static boolean arePropertiesAligned(Resource resource1, Resource resource2) {
        StmtIterator stmtIter1 = resource1.listProperties();
        StmtIterator stmtIter2 = resource2.listProperties();

        while (stmtIter1.hasNext()) {
            Statement stmt1 = stmtIter1.nextStatement();

            while (stmtIter2.hasNext()) {
                Statement stmt2 = stmtIter2.nextStatement();

                // Check if the property of stmt1 exists in stmt2
                if (stmt1.getPredicate().equals(stmt2.getPredicate())) {
                    return true; // Properties are aligned
                }
            }

            // Reset the iterator for stmt2
            stmtIter2 = resource2.listProperties();
        }

        return false; // No aligned properties found
    }

    public static void main(String[] args) {

    }



}

