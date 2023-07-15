package alsu;

import org.apache.jena.rdf.model.Model;

public class WeightedModel {
    private Model model;
    private double weight;

    public WeightedModel(Model model, double weight) {
        this.model = model;
        this.weight = weight;
    }

    public Model getModel() {
        return model;
    }

    public double getWeight() {
        return weight;
    }

}
