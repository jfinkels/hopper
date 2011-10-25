package perseus.qa;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;

import perseus.document.Chunk;
import perseus.document.Metadata;
import perseus.document.Query;

public class VerificationResult {

    private Query query;
    private ResultStatus status = ResultStatus.SUCCESS;
    private List<VerificationException> errors =
	new ArrayList<VerificationException>();

    public VerificationResult parent = null;
    private List<VerificationResult> children = new ArrayList<VerificationResult>();
    private Logger logger = Logger.getLogger(getClass());

    public enum ResultStatus  {
	SUCCESS("Success"),
	FAILURE("Failed"),
	CHILD_FAILURE("Child Failed");
	
	private String description;
	
	ResultStatus(String desc) {
	    description = desc;
	}
	
	public String description() { return description; }
    }
    
    private class VerificationException extends Exception {
	private static final long serialVersionUID = 1L;

	VerificationException(Exception e) {
	    super(e);
	}
    }

    public VerificationResult(Query query) {
	this.query = query;
    }

    public VerificationResult(Chunk chunk) {
	this(chunk.getQuery());
    }

    public Query getQuery() {
	return query;
    }

    public void set(ResultStatus stat) {
	status = stat;
    }

    public ResultStatus getStatus() {
	return status;
    }

    public void setParent(VerificationResult parent) {
	this.parent = parent;
    }

    public VerificationResult getParent() {
	return parent;
    }

    public boolean hasParent() {
	return (parent != null);
    }

    public void reportError(Exception e) {
	logger.info(query + ": reporting FAILURE!");
	status = ResultStatus.FAILURE;

	if (parent != null) {
	    parent.childFailed();
	}

	errors.add(new VerificationException(e));
    }

    public void addChild(VerificationResult result) {
	addChild(result, true);
    }

    public void addChildIfFailed(VerificationResult result) {
	addChild(result, false);
    }

    private void addChild(VerificationResult result, boolean alwaysAdd) {
	if (!alwaysAdd && result.getStatus() == ResultStatus.SUCCESS) {
	    return;
	}

	if (status == ResultStatus.SUCCESS) {
	    status = ResultStatus.CHILD_FAILURE;
	}
	if (parent != null) {
	    parent.childFailed();
	}
	children.add(result);

	result.setParent(this);
    }

    public List getErrors() {
	return errors;
    }

    public List getChildren() {
	return children;
    }

    public void childFailed() {
	if (status == ResultStatus.SUCCESS) {
	    status = ResultStatus.CHILD_FAILURE;
	}

	if (parent != null) {
	    parent.childFailed();
	}
    }

    public String toString() {
	return toString(0);
    }

    public String toString(int indentLevel) {
	StringBuffer buffer = new StringBuffer();

	for (int i = 0; i < indentLevel; i++) {
	    buffer.append("  ");
	}

	buffer.append(query);
	buffer.append("\t");
	buffer.append(status.description());

	for (Exception exc : errors) {
	    buffer.append("\n");
	    buffer.append(printException(exc,indentLevel+2));
	}

	for (VerificationResult child : children) {
	    buffer.append("\n");
	    buffer.append(child.toString(indentLevel+1));
	}

	return buffer.toString();
    }

    public Element toXML() {

	Element root = new Element("verificationResult")
		.setAttribute("documentID", query.toString());
	if (query.getDisplayQuery() != null) {
	    root.setAttribute("subquery", query.getQuery());
	}

	Metadata metadata = query.getMetadata();

	if (metadata.has(Metadata.CREATOR_KEY)) {
	    root.addContent(new Element("author")
	    	.addContent(metadata.get(Metadata.CREATOR_KEY)));
	}
	
	root.addContent(new Element("title")
		.addContent(metadata.get(Metadata.TITLE_KEY)));

	if (metadata.has(Metadata.LANGUAGE_KEY)) {
	    root.addContent(new Element("language")
	    	.addContent(metadata.get(Metadata.LANGUAGE_KEY)));
	}
	
	root.setAttribute("status", Integer.toString(status.ordinal()));

	if (!errors.isEmpty()) {
	    Element errorRoot = new Element("errors");
	    root.addContent(errorRoot);

	    for (Exception exc : errors) {
		errorRoot.addContent(exceptionToXML(exc));
	    }
	}

	if (!children.isEmpty()) {
	    Element childRoot = new Element("children");
	    for (VerificationResult child : children) {
		childRoot.addContent(child.toXML());
	    }
	}

	return root;
    }

    public Element exceptionToXML(Exception e) {

	Element root = new Element("error");
	
	StackTraceElement[] stackTrace = e.getStackTrace();

	root.addContent(
		new Element("name").addContent(e.getClass().getName()))
		.addContent(new Element("message").addContent(e.getMessage()));
	
	Element traceElement = new Element("stackTrace");
	root.addContent(traceElement);
	for (StackTraceElement ste : stackTrace) {
	    traceElement.addContent(new Element("element")
	    	.addContent(ste.toString()));
	}

	return root;
    }

    public String printException(Exception e, int indentLevel) {

	StringBuffer buffer = new StringBuffer();

	StackTraceElement[] stackTrace = e.getStackTrace();

	for (StackTraceElement ste : stackTrace) {
	    for (int j = 0; j < indentLevel+2; j++) {
		buffer.append("  ");
	    }

	    buffer.append(ste);
	    buffer.append("\n");
	}

	return buffer.toString();
    }
}
