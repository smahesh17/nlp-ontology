package org.research.owl;

import static org.semanticweb.owlapi.search.Searcher.annotations;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.RDFS_LABEL;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.RDFS_DOMAIN;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.RDFS_RANGE;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class OwlParser {

	private final OWLOntology ontology;
	private OWLDataFactory df;

	public OwlParser(OWLOntology ontology, OWLDataFactory df) {
		this.ontology = ontology;
		this.df = df;
	}

	public void parseOntology() throws OWLOntologyCreationException {

		BiConsumer<String, String> cons = (label, id) -> {
			System.out.println(label + " [" + id + "]");
		};

		for (OWLClass cls : ontology.getClassesInSignature()) {
			String id = printClass(cls, RDFS_LABEL.toString(), cons);
			System.out.println("\t id = " + id);
		}

		BiConsumer<String, String> objectConsumer = (label, id) -> {
			System.out.println(id + " {" + label + "}");
		};

		System.out.println("---------------------- Object properties ----------------------");

		for (OWLObjectProperty objectPropertyInSignature : ontology.getObjectPropertiesInSignature()) {
			System.out.println("objectPropertyInSignature  = " +objectPropertyInSignature.toStringID());
			for (OWLClass classesInSignature : objectPropertyInSignature.getClassesInSignature()) {
				printClass(classesInSignature,RDFS_DOMAIN.toString(), cons);
				printClass(classesInSignature,RDFS_RANGE.toString(), cons);
			}

			for (OWLNamedIndividual individualsInSignature : objectPropertyInSignature.getIndividualsInSignature()) {

				for (OWLClass classInSignature : individualsInSignature.getClassesInSignature()) {
					System.out.println("\t classInSignature = "+classInSignature.toStringID());
//					printClass(classInSignature, objectConsumer);
				}
			}
		}
	}

	private String printClass(OWLClass cls, String childName, BiConsumer<String, String> consumer) {
		String id = cls.getIRI().toString();
		List<String> list = get(cls, childName);
		if (list.size() > 0) {
			String label = list.get(0);
			// consumer.accept(label + " [" + id + "]");
			consumer.accept(label, id);
		}
		return id;
	}

	private List<String> get(OWLClass clazz, String property) {
		List<String> ret = new ArrayList<String>();

		final OWLAnnotationProperty owlProperty = df.getOWLAnnotationProperty(IRI.create(property));
		for (OWLOntology o : ontology.getImportsClosure()) {
			for (OWLAnnotation annotation : annotations(o.getAnnotationAssertionAxioms(clazz.getIRI()), owlProperty)) {
				if (annotation.getValue() instanceof OWLLiteral) {
					OWLLiteral val = (OWLLiteral) annotation.getValue();
					ret.add(val.getLiteral());
				}
			}
		}
		return ret;
	}

	public static void main(String[] args)
			throws OWLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

		System.out.println("STARTING .....");
		// String x =
		// "http://ontology.neuinfo.org/NIF/Dysfunction/NIF-Dysfunction.owl";
		String x = "file:///C:/E/tech/nlp-ontology/requirement/Finance.owl";

		IRI documentIRI = IRI.create(x);
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(documentIRI);
		OwlParser parser = new OwlParser(ontology, manager.getOWLDataFactory());
		parser.parseOntology();
	}
}