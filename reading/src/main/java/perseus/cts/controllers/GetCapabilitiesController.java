package perseus.cts.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import perseus.document.ChunkSchemes;
import perseus.document.Corpus;
import perseus.document.Metadata;
import perseus.document.MetadataAccessException;
import perseus.document.Query;
import perseus.document.dao.MetadataDAO;
import perseus.document.dao.SQLMetadataDAO;
import perseus.language.LanguageCode;

public class GetCapabilitiesController implements Controller {

    private Logger logger = Logger.getLogger(getClass());

    private MetadataDAO dao = new SQLMetadataDAO();

    public ModelAndView handleRequest(HttpServletRequest request,
	    HttpServletResponse response) 
        throws ServletException, IOException {

	response.setContentType("text/xml;charset=utf-8");
	response.setHeader("Access-Control-Allow-Origin","*");

	Map<String,Object> model = new HashMap<String,Object>();

	// TODO put request-data into the document via a post-filter
	model.put("output", new XMLOutputter().outputString(getXMLDocument()));

	return new ModelAndView("cts_GetCapabilities", model);
    }

    public Document getXMLDocument() {
	// This is in its own function to allow for caching.

	Element root = new Element("TextInventory");
	root.setAttribute("registryid", "perseuscts");
	root.setAttribute("tsversion", "1.0");

	// TODO namespaces, ack
	root.addContent(createNamespaceElement("perseus",
		    "Classical texts hosted by the Perseus Project at " +
		    "Tufts University"));

	Element otherNamespaces = new Element("namespaces");
	otherNamespaces.addContent(createNamespaceElement("camena",
		    "Texts from the CAMENA (Corpus Automatum Multiplex " +
		    "Electorum Neolatinitatis Auctorum) project at the " +
		    "University of Mannheim."));
	otherNamespaces.addContent(createNamespaceElement("ddbdp",
		    "Papyri from the Duke Databank of Documentary Papyri."
		    ));
	otherNamespaces.addContent(createNamespaceElement("mpi",
		    "Texts from the Max Planck Institute."
		    ));
	root.addContent(otherNamespaces);

	List<Corpus> corpora = new ArrayList<Corpus>();
	corpora.add(new Corpus(
		    new Query("Perseus:collection:Greco-Roman")));
	corpora.addAll(Corpus.getConglomerates());
	corpora.addAll(Corpus.getAuthorCorpora());

	// Only deal with classics for now. This may be all we ever want
	// to display, CTS-wise...
	for (int i = 0, n = corpora.size(); i < n; i++) {
	    Corpus corpus = (Corpus) corpora.get(i);
	    root.addContent(getCorpus(corpus));
	}

	root.addContent(getTextGroups());

	return new Document(root);
    }

    private Element getCorpus(Corpus corpus) {
	Metadata metadata = corpus.getMetadata();

	Element root = new Element("collection");
	root.setAttribute("id", corpus.getQuery().getShortID());

	// FIXME-- is this our "default"?
	if (corpus.getID().equals("Perseus:collection:Greco-Roman")) {
	    root.setAttribute("isdefault", "yes");
	}

	addIfNonNull(root, "title", metadata.get(Metadata.TITLE_KEY));
	addIfNonNull(root, "scope", metadata.get(Metadata.DESCRIPTION_KEY));
	addIfNonNull(root, "method", metadata.get(Metadata.METHOD_KEY));
	addIfNonNull(root, "rights", metadata.get(Metadata.RIGHTS_KEY));

	return root;
    }

    private void addIfNonNull(Element root, String name, String value) {
	if (value != null) {
	    root.addContent(new Element(name).addContent(value));
	}
    }

    private List getTextGroups() {
	Map groupsByAuthor = getGroupsByAuthor();

	List textGroups = new ArrayList();
	for (Iterator it = groupsByAuthor.keySet().iterator();
		it.hasNext(); ) {

	    String author = (String) it.next();
	    if (author == null) continue;

	    Element groupElement = new Element("textgroup")
		.setAttribute("projid", author);

	    List workElements = (List) groupsByAuthor.get(author);

	    textGroups.add(groupElement.addContent(workElements));
	}

	return textGroups;
    }
    
    private Map getGroupsByAuthor() {
	// The getABOs() method is slightly broken: it gets all ABOs regardless
	// of whether they're in the corpus that asked for them. As a result,
	// I've changed it from an instance method to a static method.
	List abos = Corpus.getABOs();

	Map groupsByAuthor = new HashMap();

	// The idea: we have no good way of retrieving all texts by a given
	// author, without knowing all the authors beforehand. Instead, get
	// all our ABOs; for each one, find the author, and add the ABO in a
	// list in a hash indexed by author.

	for (int i = 0, n = abos.size(); i < n; i++) {

	    String abo = (String) abos.get(i);
	    Query aboQuery = new Query(abo);
	    Metadata aboMetadata = aboQuery.getMetadata();

	    Element workElement = new Element("work");
	    // We do eventually add the "lang" attribute here--see below.
	    workElement.setAttribute("projid", aboQuery.getShortID());

	    List<Metadata.NodeValue> nodeVals =
		aboMetadata.getNodeValues(Metadata.TITLE_KEY);
	    
	    for (Metadata.NodeValue nodeVal : nodeVals) {
		Element titleElement = new Element("title");
		if (nodeVal.getLanguage() != null) {
		    titleElement.setAttribute(
			    "lang", nodeVal.getLanguage().getCode());
		}
		titleElement.addContent(nodeVal.getValue());

		workElement.addContent(titleElement);
	    }

	    List<Query> editions = null;
	    try {
		editions = dao.getDocuments(Metadata.ABO_KEY, null, abo);
	    } catch (MetadataAccessException ex) {
		logger.warn("Metadata issue", ex);
		return new HashMap();
	    }

	    // Ugh. ABOs aren't designated as part of collections-- only actual
	    // documents. CTS expects the work to know which collections it's a
	    // member of, however. Add the collections that each edition is
	    // part of to a set, and then add each element of the set to the
	    // ABO's collection-list.
	    Set containers = new HashSet();
	    for (Query edition : editions) {
		workElement.addContent(getEditionElement(edition));
		containers.addAll(
			edition.getMetadata()
			.getList(Metadata.CORPUS_KEY));

		if (workElement.getAttribute("work") == null) {
		    String editionLanguage = edition.
			getMetadata().get(Metadata.LANGUAGE_KEY);
		    if (!LanguageCode.ENGLISH.equals(editionLanguage)) {
			// Stupid hack: to get the "default" language for a
			// work, take the first language that isn't English.
			// This will work in all cases (except, perhaps, for
			// the Bible) until we have a better mechanixm in
			// place.
			workElement.setAttribute("work", editionLanguage);
		    }
		}
	    }

	    // Now do the aforementioned adding.
	    for (Iterator it = containers.iterator(); it.hasNext(); ) {
		workElement.addContent(new Element("memberof")
			.setAttribute("collection", (String) it.next()));
	    }

	    // Now that we've finally assembled the group, add it to the
	    // author-grouplist hash.
	    String author = aboMetadata.get(Metadata.CREATOR_KEY);
	    List groups;
	    if (groupsByAuthor.containsKey(author)) {
		groups = (List) groupsByAuthor.get(author);
	    } else {
		groups = new ArrayList();
		groupsByAuthor.put(author, groups);
	    }

	    groups.add(workElement);
	}

	return groupsByAuthor;
    }

    private Element getEditionElement(Query edition) {
	Metadata metadata = edition.getMetadata();
	// SIGH no way of determining the canonical language of an edition

	boolean isTranslation =
	    metadata.get(Metadata.LANGUAGE_KEY).equals(LanguageCode.ENGLISH);

	Element editionElement = new Element(
		isTranslation ? "translationcomments" : "editioncomments");
	editionElement.setAttribute("label", "See element text");
	editionElement.setAttribute("projid", edition.getShortID());

	if (metadata.has(Metadata.SOURCE_KEY)) {
	    editionElement.addContent(metadata.get(Metadata.SOURCE_KEY));
	}

	if (metadata.has(Metadata.SOURCE_TEXT_PATH_KEY)) {
	    Element onlineElement = new Element("online")
		.setAttribute("local",
			metadata.get(Metadata.SOURCE_TEXT_PATH_KEY));

	    onlineElement.addContent(getCitations(metadata.getChunkSchemes()));

	    editionElement.addContent(onlineElement);
	}

	return editionElement;
    }

    private List getCitations(ChunkSchemes chunkSchemes) {
	List<Element> citations = new ArrayList<Element>();

	List<String> schemeList = chunkSchemes.getSchemes();
	for (String scheme : schemeList) {
	    citations.add(processScheme(scheme));
	}

	return citations;
    }

    private Element processScheme(String scheme) {
	String[] types = scheme.split(":");
	LinkedList<Element> elements = new LinkedList<Element>();

	for (int i = 0; i < types.length; i++) {
	    // sigh...
	    types[i] = types[i].replaceAll("[*]", "");
	    elements.add(new Element("citation")
		    .setAttribute("element", "?")
		    .setAttribute("label", types[i]));
	}

	Element rootElement = elements.removeFirst();
	Element currentElement = rootElement;
	while (!elements.isEmpty()) {
	    Element newElement = elements.removeFirst();
	    currentElement.addContent(newElement);
	    currentElement = newElement;
	}

	return rootElement;
    }

    private Element createNamespaceElement(String id, String description) {
	return new Element("namespace").setAttribute("id", id)
	    .addContent(new Element("description").addContent(description));
    }
}
