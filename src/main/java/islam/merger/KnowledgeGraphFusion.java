package islam.merger;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.util.Iterator;
import java.util.Map;
import java.util.Iterator;


public class KnowledgeGraphFusion {

    public static OntModel mergeKnowledgeGraphs(OntModel knowledgeGraph1, OntModel knowledgeGraph2) {
        OntModel fusedKnowledgeGraph = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

        // Merge knowledgeGraph1 into fusedKnowledgeGraph
        fusedKnowledgeGraph.addSubModel(knowledgeGraph1);

        // Apply rule-based fusion for knowledgeGraph2
        /*!SECTION
         * In this updated code, we apply the rule-based fusion for knowledgeGraph2. 
         * We iterate through the resources in knowledgeGraph2 and check if each
         *  entity already exists in the fusedKnowledgeGraph. If the entity does not 
         * exist, we simply add its model to the fusedKnowledgeGraph. If the entity already 
         * exists, we merge its attributes by adding the model of the corresponding entity 
         * from knowledgeGraph2 into the fusedKnowledgeGraph.
         */
        Iterator<Resource> iterator = knowledgeGraph2.listSubjects();
        while (iterator.hasNext()) {
            Resource entity = iterator.next();
            if (fusedKnowledgeGraph.getIndividual(entity.getURI()) == null) {
                fusedKnowledgeGraph.addSubModel(knowledgeGraph2.getResource(entity.getURI()).getModel());
            } else {
                Resource entityInFusedGraph = fusedKnowledgeGraph.getIndividual(entity.getURI());
                entityInFusedGraph.getModel().add(knowledgeGraph2.getResource(entity.getURI()).getModel());
            }
        }

        return fusedKnowledgeGraph;
    }
    public static OntModel mergeKnowledgeGraphsrule2(OntModel knowledgeGraph1, OntModel knowledgeGraph2) {
        OntModel fusedKnowledgeGraph = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

        // Merge knowledgeGraph1 into fusedKnowledgeGraph
        fusedKnowledgeGraph.addSubModel(knowledgeGraph1);

        // Apply rule-based fusion for knowledgeGraph2
        /*In this updated code, when an entity exists in both knowledge graphs,
         we clear all the existing attributes of the entity in the fusedKnowledgeGraph 
         and then add the attributes from knowledgeGraph2. This way, we prioritize 
         the information from knowledgeGraph2 over knowledgeGraph1. */
        Iterator<Resource> iterator = knowledgeGraph2.listSubjects();
        while (iterator.hasNext()) {
            Resource entity = iterator.next();
            if (fusedKnowledgeGraph.getIndividual(entity.getURI()) == null) {
                fusedKnowledgeGraph.addSubModel(knowledgeGraph2.getResource(entity.getURI()).getModel());
            } else {
                // Ignore attributes from knowledgeGraph1 and retain attributes from knowledgeGraph2
                Resource entityInFusedGraph = fusedKnowledgeGraph.getIndividual(entity.getURI());
                entityInFusedGraph.getModel().removeAll(entityInFusedGraph, null, null);
                entityInFusedGraph.getModel().add(knowledgeGraph2.getResource(entity.getURI()).getModel());
            }
        }

        return fusedKnowledgeGraph;
    }
    public static OntModel mergeKnowledgeGraphslength(OntModel model1, OntModel model2) {
        StmtIterator iter1 = model1.listStatements();
        StmtIterator iter2 = model2.listStatements();
        Model conflict = ModelFactory.createDefaultModel();
        Model fusedModels = ModelFactory.createDefaultModel();
        OntModel fusedKnowledgeGraph = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

        fusedModels.add(model1);
        while (iter1.hasNext()) {
            Statement stmt = iter1.next();
            Resource subj = stmt.getSubject();

            // Add the mapped statements from model2 to the merged model
            while (iter2.hasNext()) {
                Statement stmt1 = iter2.next();
                String uri = stmt1.getSubject().getURI();
                //if the subject of model1 is equal to the subject of model2
                Resource resource = model2.getResource(uri);
                Resource resource1 = model1.getResource(uri);
                if(stmt.getSubject().hasURI(uri)){

                    //if the predicate of model1 is equal to the predicate of model2

                    Property stmtpredicate = stmt.getPredicate();
                    //System.out.println(resource.getProperty(stmt.getPredicate()));
                    Statement conflict1 = resource.getProperty(stmtpredicate);
                    Statement conflict2 = resource1.getProperty(stmtpredicate);
                    if(conflict1!=null ){

                        conflict.add(conflict1);
                        

                        System.out.println("1: "+conflict1);

                        // If the predicate of model1 is equal to the predicate of model2
                        if(conflict2!=null&&conflict1.getPredicate().equals(conflict2.getPredicate())){
                            RDFNode res = resolvePredicateConflict(conflict1.getObject(),conflict2.getObject());
                            fusedModels.add(stmt1.getSubject(), stmt1.getPredicate(),res);
                            conflict.add(conflict2);
                            System.out.println("2: "+conflict2);
                        }
                    }else {
                        fusedModels.add(stmt1.getSubject(), stmt1.getPredicate(), stmt1.getObject());
                    }
                    break;
                }else{
                    fusedModels.add(stmt1.getSubject(), stmt1.getPredicate(), stmt1.getObject());
                }

                
            }
//
        }
        OntModel ontModelont = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, fusedModels);


        return ontModelont;
    }
    private static RDFNode resolvePredicateConflict(RDFNode object, RDFNode object1) {
        if(object.toString().length()>object1.toString().length()){
            return object;
        }
        return object1;
    }


        public static OntModel mergeKnowledgeGraphsrule3(OntModel knowledgeGraph1, OntModel knowledgeGraph2) {
        OntModel fusedKnowledgeGraph = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

        // Merge knowledgeGraph1 into fusedKnowledgeGraph
        fusedKnowledgeGraph.addSubModel(knowledgeGraph1);

        // Apply rule-based fusion for knowledgeGraph2
        Iterator<Resource> iterator = knowledgeGraph2.listSubjects();
        while (iterator.hasNext()) {
            Resource entity = iterator.next();
            Resource entityInFusedGraph = fusedKnowledgeGraph.getIndividual(entity.getURI());

            if (entityInFusedGraph == null) {
                fusedKnowledgeGraph.addSubModel(knowledgeGraph2.getResource(entity.getURI()).getModel());
            } else {
                // Merge properties from both graphs for entities that exist in both graphs
                ExtendedIterator<Statement> stmtIterator = entity.listProperties();
                while (stmtIterator.hasNext()) {
                    Statement stmt = stmtIterator.next();
                    Property property = stmt.getPredicate();
                    RDFNode object = stmt.getObject();

                    // Check if the property is already present in the fusedKnowledgeGraph for this entity
                    if (!entityInFusedGraph.hasProperty(property)) {
                        entityInFusedGraph.addProperty(property, object);
                    }
                }
            }
        }

        return fusedKnowledgeGraph;
    }
    public static OntModel mergeKnowledgeGraphsrule4(OntModel knowledgeGraph1, OntModel knowledgeGraph2) {
        OntModel fusedKnowledgeGraph = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

        // Merge knowledgeGraph1 into fusedKnowledgeGraph
        fusedKnowledgeGraph.addSubModel(knowledgeGraph1);

        // Apply rule-based fusion for knowledgeGraph2
        Iterator<Resource> iterator = knowledgeGraph2.listSubjects();
        while (iterator.hasNext()) {
            Resource entity = iterator.next();
            Resource entityInFusedGraph = fusedKnowledgeGraph.getIndividual(entity.getURI());

            if (entityInFusedGraph == null) {
                fusedKnowledgeGraph.addSubModel(knowledgeGraph2.getResource(entity.getURI()).getModel());
            } else {
                ExtendedIterator<Statement> stmtIterator = entity.listProperties();
                while (stmtIterator.hasNext()) {
                    Statement stmt = stmtIterator.next();
                    Property property = stmt.getPredicate();
                    RDFNode object = stmt.getObject();

                    // Merge properties from both graphs for entities that exist in both graphs

                    if (!entityInFusedGraph.hasProperty( property)) {
                        entityInFusedGraph.addProperty( property, entity.getProperty( property).getObject());
                    }
                }
                
            }
        }

        return fusedKnowledgeGraph;
    }
    public static OntModel mergeKnowledgeGraphspriority(OntModel knowledgeGraph1, OntModel knowledgeGraph2, Map<Property, Integer> propertyPriority) {
        OntModel fusedKnowledgeGraph = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

        // Merge knowledgeGraph1 into fusedKnowledgeGraph
        fusedKnowledgeGraph.addSubModel(knowledgeGraph1);

        // Apply priority-based fusion for knowledgeGraph2
        Iterator<Resource> iterator = knowledgeGraph2.listSubjects();
        while (iterator.hasNext()) {
            Resource entity = iterator.next();
            Resource entityInFusedGraph = fusedKnowledgeGraph.getIndividual(entity.getURI());

            if (entityInFusedGraph == null) {
                fusedKnowledgeGraph.addSubModel(knowledgeGraph2.getResource(entity.getURI()).getModel());
            } else {
                // Merge properties from both graphs based on priority
                ExtendedIterator<Statement> stmtIterator = entity.listProperties();
                while (stmtIterator.hasNext()) {
                    Statement stmt = stmtIterator.next();
                    Property property = stmt.getPredicate();
                    RDFNode object = stmt.getObject();

                    if (propertyPriority.containsKey(property)) {
                        int priorityInGraph2 = propertyPriority.get(property);

                        if (entityInFusedGraph.hasProperty(property)) {
                            int priorityInFusedGraph = propertyPriority.getOrDefault(property, Integer.MAX_VALUE);
                            if (priorityInGraph2 < priorityInFusedGraph) {
                                // Remove existing property with lower priority in fusedKnowledgeGraph
                                entityInFusedGraph.removeAll(property);
                                entityInFusedGraph.addProperty(property, object);
                            }
                        } else {
                            // Add new property from graph2
                            entityInFusedGraph.addProperty(property, object);
                        }
                    }
                }

                
            }
        }

        return fusedKnowledgeGraph;
    }
    public static OntModel mergeKnowledgeGraphslink(OntModel knowledgeGraph1, OntModel knowledgeGraph2) {
        // Create a temporary fused knowledge graph
        OntModel fusedKnowledgeGraph = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

        // Merge knowledgeGraph1 into the fusedKnowledgeGraph
        fusedKnowledgeGraph.addSubModel(knowledgeGraph1);

        // Iterate through the entities in knowledgeGraph2
        Iterator<Resource> iterator = knowledgeGraph2.listSubjects();
        while (iterator.hasNext()) {
            Resource entity2 = iterator.next();
            Resource entityInFusedGraph = fusedKnowledgeGraph.getIndividual(entity2.getURI());

            if (entityInFusedGraph == null) {
                // If the entity does not exist in the fusedKnowledgeGraph, add it along with its properties
                fusedKnowledgeGraph.addSubModel(knowledgeGraph2.getResource(entity2.getURI()).getModel());
            } else {
                // If the entity already exists in the fusedKnowledgeGraph, merge its properties
                ExtendedIterator<Statement> stmtIterator = entity2.listProperties();
                while (stmtIterator.hasNext()) {
                    Statement stmt = stmtIterator.next();
                    Property property = stmt.getPredicate();
                    RDFNode object = stmt.getObject();

                    RDFNode object2 =stmt.getObject();
                    if (!entityInFusedGraph.hasProperty(property, object2)) {
                        entityInFusedGraph.addProperty(property, object2);
                    }
                }
                
            }

            // Merge the relations (hasRelation property) between entities
            Iterator<Resource>stmtIterator = entity2.listProperties().mapWith(null);
            while (stmtIterator.hasNext()) {
                Resource relation = stmtIterator.next();
                Property hasRelation = knowledgeGraph2.getProperty("http://example.org/hasRelation");
                if (relation.hasProperty(hasRelation)) {
                    Resource relatedEntity2 = relation.getPropertyResourceValue(hasRelation);
                    Resource relatedEntityInFusedGraph = fusedKnowledgeGraph.getIndividual(relatedEntity2.getURI());

                    if (relatedEntityInFusedGraph == null) {
                        // If the related entity does not exist in the fusedKnowledgeGraph, add it along with its properties
                        fusedKnowledgeGraph.addSubModel(knowledgeGraph2.getResource(relatedEntity2.getURI()).getModel());
                    }

                    // Add the link between entities in the fusedKnowledgeGraph
                    if (relatedEntityInFusedGraph != null && !entityInFusedGraph.hasProperty(hasRelation, relatedEntityInFusedGraph)) {
                        entityInFusedGraph.addProperty(hasRelation, relatedEntityInFusedGraph);
                    }
                }
            }
        }

        // Run an inference engine (optional) to infer additional links between entities
        Reasoner reasoner = ReasonerRegistry.getRDFSReasoner();
        InfModel infModel = ModelFactory.createInfModel(reasoner, fusedKnowledgeGraph);
        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, infModel);

        // Return the final fused knowledge graph
        return ontModel;
    }
    
}
