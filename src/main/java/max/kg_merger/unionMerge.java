package max.kg_merger;

import org.apache.jena.rdf.model.*;

/*
Merge the knowledge graphs by combining all the nodes and edges from each graph into a single graph.
This strategy results in a larger merged graph that contains all the information from the individual graphs.
 */
public class unionMerge {

    public static Model mergeGraphs(Model graph1, Model graph2){
        return ModelFactory.createUnion(graph1, graph2);
    }

    public static void main(String[] args) {

    }



}

