package uni_ma.kg_merger;

import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.OWL;

public class OntologyMapping {

    public Model map(Model model1, Model model2){
        Model mappingOntModel = ModelFactory.createDefaultModel();
        OntModel ontModel1 = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, model1);
        OntModel ontModel2 = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, model2);

        // Iterate over the classes in ontology 1 and find matching classes in ontology 2
        for (OntClass ontClass1 : ontModel1.listClasses().toList()) {
            for (OntClass ontClass2 : ontModel2.listClasses().toList()) {
                if (ontClass1.getLabel(null).equals(ontClass2.getLabel(null))) {
                    mappingOntModel.add(ontClass1, OWL.sameAs, ontClass2);
                }
            }
        }

        // Iterate over the properties in ontology 1 and find matching properties in ontology 2
        for (OntProperty ontProp1 : ontModel1.listAllOntProperties().toList()) {
            for (OntProperty ontProp2 : ontModel2.listAllOntProperties().toList()) {
                if (ontProp1.getLabel(null).equals(ontProp2.getLabel(null))) {
                    mappingOntModel.add(ontProp1, OWL.sameAs, ontProp2);
                }
            }
        }

        // Iterate over the individuals in ontology 1 and find matching individuals in ontology 2
        for (Individual ind1 : ontModel1.listIndividuals().toList()) {
            for (Individual ind2 : ontModel2.listIndividuals().toList()) {
                if (ind1.getLabel(null).equals(ind2.getLabel(null))) {
                    mappingOntModel.add(ind1, OWL.sameAs, ind2);
                }
            }
        }

        return mappingOntModel;
    }

    /*public static void main(String[] args) {
        // Load the two RDF models to be merged
        Model model1 = FileManager.get().loadModel("knowledge_graph1.rdf");
        Model model2 = FileManager.get().loadModel("knowledge_graph2.rdf");

        // Load the common ontology
        Model ontologyModel = FileManager.get().loadModel("common_ontology.rdf");

        // Create a new model to hold the merged knowledge graph
        Model mergedModel = ModelFactory.createDefaultModel();

        // Map the ontology terms used in model1 to the common ontology
        Property hasName1 = model1.createProperty("http://example.com/hasName");
        Property hasAge1 = model1.createProperty("http://example.com/hasAge");
        Property isFriendWith1 = model1.createProperty("http://example.com/isFriendWith");

        Property hasName = ontologyModel.getProperty("http://example.com/hasName");
        Property hasAge = ontologyModel.getProperty("http://example.com/hasAge");
        Property isFriendWith = ontologyModel.getProperty("http://example.com/isFriendWith");

        // Map the ontology terms used in model2 to the common ontology
        Property hasName2 = model2.createProperty("http://example.com/personName");
        Property hasAge2 = model2.createProperty("http://example.com/personAge");
        Property isFriendWith2 = model2.createProperty("http://example.com/personFriend");

        // Add the mapped statements from model1 to the merged model
        StmtIterator iter1 = model1.listStatements();
        while (iter1.hasNext()) {
            Statement stmt = iter1.next();
            Resource subj = stmt.getSubject();
            Property pred = stmt.getPredicate();
            RDFNode obj = stmt.getObject();

            if (pred.equals(hasName1)) {
                mergedModel.add(subj, hasName, obj);
            } else if (pred.equals(hasAge1)) {
                mergedModel.add(subj, hasAge, obj);
            } else if (pred.equals(isFriendWith1)) {
                mergedModel.add(subj, isFriendWith, obj);
            }
        }

        // Add the mapped statements from model2 to the merged model
        StmtIterator iter2 = model2.listStatements();
        while (iter2.hasNext()) {
            Statement stmt = iter2.next();
            Resource subj = stmt.getSubject();
            Property pred = stmt.getPredicate();
            RDFNode obj = stmt.getObject();

            if (pred.equals(hasName2)) {
                mergedModel.add(subj, hasName, obj);
            } else if (pred.equals(hasAge2)) {
                mergedModel.add(subj, hasAge, obj);
            } else if (pred.equals(isFriendWith2)) {
                mergedModel.add(subj, isFriendWith, obj);
            }
        }

        // Output the merged model to an RDF file or other destination
        mergedModel.write(System.out, "RDF/XML-ABBREV");
    }*/

}

