package perseus.ie.entity;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Text;

/**
 * Helper class used by the EntityExtractor to create snippets for
 * EntityTuples.
 */
public class SnippetCreator {
    private Map<Content,Integer> startOffsets = new HashMap<Content,Integer>();
    private Map<Content,Integer> endOffsets = new HashMap<Content,Integer>();

    private String documentText;

    private static final int MAX_ELEMENTS = 10;
    private static final int TOTAL_SNIPPET_LENGTH = 1000;

    public SnippetCreator(Document document) {
        StringBuilder buffer = new StringBuilder();
        traverse(document.getRootElement(), buffer);

        documentText = buffer.toString();
    }

    private void traverse(Content content, StringBuilder buffer) {
        startOffsets.put(content, buffer.length());

        if (content instanceof Text) {
            buffer.append(((Text) content).getValue());
        } else if (content instanceof Element) {
            for (Object child : ((Element) content).getContent()) {
                traverse((Content) child, buffer);
            }
        }

        endOffsets.put(content, buffer.length());
    }

    public String createSnippet(List<Element> elements) {
        List<Element> snippetedElements = elements;
        if (elements.size() > MAX_ELEMENTS) {
            snippetedElements = elements.subList(0, MAX_ELEMENTS);
        }

        int charsPerElement = TOTAL_SNIPPET_LENGTH / snippetedElements.size();
        StringBuilder snippet =
            extractSnippet(snippetedElements, charsPerElement);

        if (elements.size() > MAX_ELEMENTS) {
            snippet.append(
                    String.format("<gap /><hi rend=\"ital\">[%d more...]</hi>",
                        elements.size() - snippetedElements.size()));
        }

        return snippet.toString();
    }

    public StringBuilder extractSnippet(List<Element> elements,
            int charsPerElement) {

        Collections.sort(elements, new Comparator<Element>() {
            public int compare(Element a, Element b) {
                return startOffsets.get(a) - startOffsets.get(b);
            }
        });

        int lastEnd = 0;
        StringBuilder snippetText = new StringBuilder();

        int textSize = documentText.length();

        for (Element element : elements) {
            int start = startOffsets.get(element);
            int end = endOffsets.get(element);
            int spaceLeft = charsPerElement - (end - start);
            int leftBound = start - (spaceLeft / 2);
            int rightBound = end + (spaceLeft / 2);

            if (leftBound <= lastEnd) {
                rightBound += lastEnd - leftBound;
                leftBound += lastEnd - leftBound;
            }

            if (leftBound < 0) {
                rightBound += -leftBound;
                leftBound = 0;
            }

            if (rightBound > textSize) {
                leftBound -= rightBound - textSize;
                rightBound = textSize;
            }

            if (leftBound > lastEnd) {
                snippetText.append("<gap />");
            }
            if (leftBound < 0) leftBound = 0;
            if (leftBound > start) leftBound = start;

            String m1 = documentText.substring(leftBound, start);
            String m2 = documentText.substring(start, end);
            String m3 = documentText.substring(end, rightBound);
            snippetText.append(String.format(
                        "%s<span class=\"match\">%s</span>%s",
                        documentText.substring(leftBound, start),
                        documentText.substring(start, end),
                        documentText.substring(end, rightBound)));

            lastEnd = rightBound;
        }

        if (lastEnd < textSize) {
            snippetText.append("<gap />");
        }

        return snippetText;
    }

    public String getDocumentText() {
        return documentText;
    }
}
