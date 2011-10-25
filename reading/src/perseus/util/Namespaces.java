package perseus.util;

import java.util.Map;
import java.util.HashMap;

import org.jdom.Element;
import org.jdom.Namespace;

public class Namespaces {
    private Namespaces() {}

    public static String DUBLIN_CORE = "dc";
    public static String DC_TERMS = "dcterms";
    public static String DC_TYPE = "dctype";
    public static String PERSEUS = "perseus";
    public static String PERSQ = "persq";
    public static String RDF = "rdf";
    public static String TUFTS = "tufts";
    public static String XHTML = "xhtml";
    public static String XSL = "xsl";

    private static Map ns = new HashMap();

    static {
	register(DUBLIN_CORE, "http://purl.org/dc/elements/1.1");
	register(DC_TERMS, "http://purl.org/dc/terms/");
	register(DC_TYPE, "http://purl.org/dc/dcmitype/");
	register(PERSEUS, "http://www.perseus.org/meta/perseus.rdfs#");
	register(PERSQ, "http://www.perseus.org/meta/petsq.rdfs#");
	register(RDF, "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
	register(TUFTS, "http://www.tufts.edu/");
	register(XHTML, "http://www.w3.org/1999/xhtml");
	register(XSL, "http://www.w3.org/1999/XSL/Transform");
    }

    public static String get(String key) {
	if (!ns.containsKey(key)) return null;
	return (String) ns.get(key);
    }

    public static boolean isRecognized(String key) {
	return ns.containsKey(key);
    }

    public static void register(String key, String namespace) {
	ns.put(key, namespace);
    }

    public static String[] knownIdentifiers() {
	return (String []) ns.keySet().toArray(new String[0]);
    }

    public static void addToElement(Element element, String[] namespaces) {
	for (int i = 0; i < namespaces.length; i++) {
	    Namespace namespace =
		Namespace.getNamespace(namespaces[i], get(namespaces[i]));
	    element.addNamespaceDeclaration(namespace);
	}
    }
}
