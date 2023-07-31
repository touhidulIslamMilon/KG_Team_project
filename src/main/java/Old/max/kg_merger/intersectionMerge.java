package Old.max.kg_merger;

import org.apache.jena.rdf.model.*;

/*
Merge the knowledge graphs by retaining only the common nodes and edges present in all the graphs.
This strategy focuses on finding shared knowledge between the graphs and discarding any unique information
 */
public class intersectionMerge {

    public static Model mergeGraphs(Model graph1, Model graph2){
        Model mergedModel = ModelFactory.createDefaultModel();

        // Iterate over the statements in model1
        ResIterator iter = graph1.listSubjects();
        while (iter.hasNext()) {
            Resource resource = iter.nextResource();
            if (graph2.containsResource(resource)) {
                // Add statements from both models for the matching resource
                Statement[] statements1 = graph1.listStatements(resource, null, (String) null).toList().toArray(new Statement[0]);
                Statement[] statements2 = graph2.listStatements(resource, null, (String) null).toList().toArray(new Statement[0]);
                mergedModel.add(statements1);
                mergedModel.add(statements2);
            }
        }

        return mergedModel;
    }

    public static void main(String[] args) {

    }



}

