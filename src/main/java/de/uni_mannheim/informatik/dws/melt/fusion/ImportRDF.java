package de.uni_mannheim.informatik.dws.melt.fusion;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.tdb.TDBLoader;

import java.util.Iterator;


//test from Alsu
public class ImportRDF {
    public static void importDataset(String directory, String sparqlQueryString){
        //importing rdf data from directory
        ImportRDF importRDF = new ImportRDF();
        String Directory="";
        //String sparqlQueryString ="PREFIX humans: <http://www.inria.fr/2007/09/11/humans.rdfs#> " +
        //        "PREFIX inst: <http://www.inria.fr/2007/09/11/humans.rdfs-instances#> SELECT ?x WHERE " +
        //        "{?x humans:hasSpouse inst:Catherine .}";
        importRDF.importDataset(Directory, sparqlQueryString);

        Dataset d = TDBFactory.createDataset(directory);
        Model model = d.getDefaultModel();

        d.begin(ReadWrite.READ); try {
            Iterator<Quad> iter = d.asDatasetGraph().find(); int i=0;
            System.out.println("begin ");
            while (iter.hasNext() && i < 20) {
                Quad quad = iter.next(); System.out.println("iteration "+i); System.out.println(quad);
                i++;
            }
        } finally { d.end(); } d.close(); System.out.println("finish ...");



        // See http://incubator.apache.org/jena/documentation/query/app_api.html
        Query query = QueryFactory.create(sparqlQueryString) ;
        QueryExecution qexec = QueryExecutionFactory.create(query, d) ;
        try {
            ResultSet results = qexec.execSelect() ;
            while (results.hasNext()) {
                QuerySolution sol = results.next();
                System.out.println("Solution := "+sol);
                for (Iterator<String> names = sol.varNames(); names.hasNext(); ) {
                    final String name = names.next();
                    System.out.println("\t"+name+" := "+sol.get(name)); }
            }
        } finally { qexec.close() ; }
        // Close the dataset.
        d.close();
    }
    public Dataset createDataset(String file, String directory) {
        try{
            Dataset dataset = TDBFactory.createDataset(directory);
            dataset.begin(ReadWrite.WRITE) ;
            Model model = dataset.getDefaultModel(); TDBLoader.loadModel(model, file);
            dataset.commit();
            dataset.end();
            return dataset;
        }catch(Exception ex)
        {
            System.out.println("##### Error Fonction: createDataset #####"); System.out.println(ex.getMessage());
            return null;
        }
    }
}
