package org.example;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.Query ;
import org.apache.jena.query.QueryExecution ; import org.apache.jena.query.QueryExecutionFactory ; import org.apache.jena.query.QueryFactory ;
import org.apache.jena.query.QuerySolution ; import org.apache.jena.query.ResultSet ;

import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.vocabulary.*;

import java.util.Iterator;

// Press ⇧ twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        //importing rdf data from directory
        ImportRDF importRDF = new ImportRDF();
        String Directory="";
        importRDF.importDataset(Directory);

    }
}