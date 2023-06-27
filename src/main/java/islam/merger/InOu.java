package islam.merger;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;

import java.io.InputStream;

public class InOu {
    public Model inputModel(String path){
        Model model1 = ModelFactory.createDefaultModel();
        InputStream testA = RDFDataMgr.open(path);
        if (testA == null)
        {
            throw new IllegalArgumentException("File: " + path + " not found");
        }

        // read the RDF/XML file
        model1.read(testA, null);
        return model1;
    }
}
