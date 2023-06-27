package alsu;

public class kg {
    // create an empty model
/*
Model modelA = ModelFactory.createDefaultModel();
Model modelB = ModelFactory.createDefaultModel();
// use the RDFDataMgr to find the input file
InputStream testA = RDFDataMgr.open("/Users/alsufathutdinova/Desktop/testA.rdf");
if (testA == null) {
    throw new IllegalArgumentException("File: " + "/Users/alsufathutdinova/Desktop/testA.rdf" + " not found");
}

// read the RDF/XML file
modelA.read(testA, null);

InputStream testB = RDFDataMgr.open("/Users/alsufathutdinova/Desktop/testB.rdf");
if (testB == null) {
    throw new IllegalArgumentException("File: " + "/Users/alsufathutdinova/Desktop/testB.rdf" + " not found");
}

// read the RDF/XML file
modelB.read(testB, null);

System.out.println("#testA");
// write it to standard out
modelA.write(System.out);

System.out.println("#testB");
modelB.write(System.out);

System.out.println("#FUSED models");
Model fusedModels = modelA.union(modelB);
fusedModels.write(System.out);

// Load the RDF document
Model model = FileManager.get().loadModel("/Users/alsufathutdinova/Desktop/testA.rdf");

// Create the ontology model
OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

// Get all the subjects in the RDF document
ResIterator subjects = model.listSubjects();

while (subjects.hasNext()) {
    Resource subject = subjects.next();

    // Check if the subject is a class or an instance
    if (subject.hasProperty(RDF.type, OWL.Class)) {
        // Create a new class in the ontology model
        OntClass ontClass = ontModel.createClass(subject.getURI());

        // Get all the properties of the class
        StmtIterator properties = subject.listProperties();
        while (properties.hasNext()) {

            Statement property = properties.next();

            // Check if the property is a sub-class or an equivalent class
            if (property.getPredicate().equals(RDFS.subClassOf) ||
                    property.getPredicate().equals(OWL.equivalentClass)) {
                Resource object = property.getObject().asResource();
                OntClass superClass = ontModel.getOntClass(object.getURI());
                ontClass.addSuperClass(superClass);
            }
        }
    } else {
        Resource type = subject.getPropertyResourceValue(RDF.type);
        if(type != null) {
            Individual individual = ontModel.createIndividual(subject.getURI(),
                    ontModel.createClass(type.getURI()));
        } else {
            Individual individual = null;
            if (subject.hasProperty(RDF.type)) {
                Resource typeResource = subject.getProperty(RDF.type).getObject().asResource();
                OntClass ontClass = ontModel.getOntClass(typeResource.getURI());
                individual = ontModel.createIndividual(subject.getURI(), ontClass);
            } else {
                individual = ontModel.createIndividual(subject);
            }
        }
    }
}
System.out.println("smth happened!");
// Output the ontology model
ontModel.write(System.out, "RDF/XML-ABBREV");
*/

}
