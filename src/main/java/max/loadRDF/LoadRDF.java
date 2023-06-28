package max.loadRDF;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

public class LoadRDF {

    public static Model getModel(String filename){
        Model model = ModelFactory.createDefaultModel();
        model.read(filename);
        return model;
    }


}
