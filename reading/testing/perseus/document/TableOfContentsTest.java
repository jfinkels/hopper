package testing.perseus.document;

import junit.framework.TestCase;

import perseus.document.Chunk;
import perseus.document.InvalidQueryException;
import perseus.document.Query;
import perseus.document.TableOfContents;

public class TableOfContentsTest extends TestCase {
    public static void testForChunkDocIDWithSubdoc() {
	doTest("Perseus:text:1999.01.0001",
		"Perseus:text:1999.01.0001:speech=1");
    }

    public static void testForChunkSubdocID() {
	doTest("Perseus:text:1999.01.0001:speech=1",
		"Perseus:text:1999.01.0001:speech=1");
    }

    public static void testForChunkSubdocQuery() {
	doTest("Perseus:text:1999.01.0001:speech=1:section=1",
		"Perseus:text:1999.01.0001:speech=1");
    }

    public static void testForChunkDocID() {
	doTest("Perseus:text:1999.01.0003",
		"Perseus:text:1999.01.0003");
    }

    public static void testForChunkDocQuery() {
	doTest("Perseus:text:1999.01.0003:card=100",
		"Perseus:text:1999.01.0003");
    }

    public static void testForChunkViewID() {
	doTest("Perseus:text:1999.02.0140:view=notes",
		"Perseus:text:1999.02.0140:view=notes");
    }

    public static void testForChunkViewQuery() {
	doTest("Perseus:text:1999.02.0140:view=notes:book=58",
		"Perseus:text:1999.02.0140:view=notes");
    }

    public static void testGetPrevious() {
	Chunk chunk = new Chunk(new Query("Perseus:text:1999.01.0001:speech=1"));
	try {
	    TableOfContents toc = TableOfContents.forChunk(chunk);

	    Chunk sectionChunk = toc.getByQuery(new Query(
			"Perseus:text:1999.01.0001:speech=1:section=139"));
	    Chunk prevChunk = sectionChunk.getPrevious();
	    assertEquals(prevChunk.getType(), "section");
	    assertEquals(prevChunk.getValue(), "138");

	    sectionChunk = toc.getByQuery(new Query(
			"Perseus:text:1999.01.0001:speech=1:section=1"));
	    prevChunk = sectionChunk.getPrevious();
	    assertNull(prevChunk);
	} catch (InvalidQueryException iqe) {
	    fail();
	}
    }

    public static void testGetNext() {
	Chunk chunk = new Chunk(new Query("Perseus:text:1999.01.0001:speech=2"));
	try {
	    TableOfContents toc = TableOfContents.forChunk(chunk);

	    Chunk sectionChunk = toc.getByQuery(new Query(
			"Perseus:text:1999.01.0001:speech=2:section=139"));
	    Chunk nextChunk = sectionChunk.getNext();
	    assertEquals(nextChunk.getType(), "section");
	    assertEquals(nextChunk.getValue(), "140");

	    sectionChunk = toc.getByQuery(new Query(
			"Perseus:text:1999.01.0001:speech=2:section=184"));
	    nextChunk = sectionChunk.getNext();
	    assertNull(nextChunk);
	} catch (InvalidQueryException iqe) {
	    fail();
	}
    }

    private static void doTest(String input, String target) {
	Query query = new Query(input);
	Chunk textChunk = new Chunk(query);

	try {
	    TableOfContents toc = TableOfContents.forChunk(textChunk);
	    assertEquals(toc.getQuery().toString(), target);
	} catch (InvalidQueryException iqe) {
	    fail();
	}
    }
}
