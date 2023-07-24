package islam;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;


class KnowledgeGraph {
    Model model1 = ModelFactory.createDefaultModel();
    private int priority;
    private Date date;

    public KnowledgeGraph(int priority, Model graph, Date date) {
        this.date = date;
        this.model1 = graph;
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
    public Date getDate() {
        return date;
    }
   //setter for date
    public void setDate(Date date) {
        this.date = date;
    }
    public Model getGraph() {
        return model1;
    }
}
