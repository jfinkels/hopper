/**
 * This file will hopefully grow in the future; right now it just contains
 * a couple tests for getInitialChunk() and its ilk.
 */

package testing.perseus.document;

import junit.framework.TestCase;

import perseus.document.Chunk;
import perseus.document.InvalidQueryException;
import perseus.document.Query;
import perseus.document.TableOfContents;

public class ChunkTest extends TestCase {
    public static void testGetInitialChunkDocID() {
	assertGICEquals(
		"Perseus:text:1999.01.0001",
		"Perseus:text:1999.01.0001");
    }

    public static void testGetInitialChunkSubdocID() {
	assertGICEquals(
		"Perseus:text:1999.01.0001:speech=1",
		"Perseus:text:1999.01.0001:speech=1");
    }

    public static void testGetInitialChunkSubdocText() {
	assertGICEquals(
		"Perseus:text:1999.01.0001:speech=1:section=1",
		"Perseus:text:1999.01.0001:speech=1");
    }

    public static void testGetInitialChunkNonSubdocID() {
	assertGICEquals(
		"Perseus:text:1999.01.0003",
		"Perseus:text:1999.01.0003");
    }
    public static void testGetInitialChunkNonSubdocText() {
	assertGICEquals(
		"Perseus:text:1999.01.0003:card=1",
		"Perseus:text:1999.01.0003");
    }
    public static void testGetInitialChunkViewDocID() {
	assertGICEquals(
		"Perseus:text:1999.02.0140",
		"Perseus:text:1999.02.0140");
    }
    public static void testGetInitialChunkViewSubdocID() {
	assertGICEquals(
		"Perseus:text:1999.02.0140:view=notes",
		"Perseus:text:1999.02.0140:view=notes");
    }
    public static void testGetInitialChunkViewSubdocText() {
	assertGICEquals(
		"Perseus:text:1999.01.0001:view=notes:book=45:section=1",
		"Perseus:text:1999.01.0001:view=notes");
    }

    private static void assertGICEquals(String input, String target) {
	Query query = new Query(input);
	Chunk chunk = Chunk.getInitialChunk(query);

	String output = chunk.getQuery().toString();
	assertEquals(output, target);
    }

    public static void testGetTextlessChunkDocID() {
	assertGTCEquals(
		"Perseus:text:1999.01.0001",
		"Perseus:text:1999.01.0001:speech=1:section=1");
    }

    public static void testGetTextlessChunkSubdocID() {
	assertGTCEquals(
		"Perseus:text:1999.01.0001:speech=1",
		"Perseus:text:1999.01.0001:speech=1:section=1");
    }

    public static void testGetTextlessChunkSubdocText() {
	assertGTCEquals(
		"Perseus:text:1999.01.0001:speech=1:section=1",
		"Perseus:text:1999.01.0001:speech=1:section=1");
    }

    public static void testGetTextlessChunkNonSubdocID() {
	assertGTCEquals(
		"Perseus:text:1999.01.0003",
		"Perseus:text:1999.01.0003:card=1");
    }
    public static void testGetTextlessChunkNonSubdocText() {
	assertGTCEquals(
		"Perseus:text:1999.01.0003:card=1",
		"Perseus:text:1999.01.0003:card=1");
    }

    public static void testGetTextlessChunkLineQuery() {
	assertGTCEquals(
		"Perseus:text:1999.01.0003:card=4",
		"Perseus:text:1999.01.0003:card=1");
    }

    public static void testGetTextlessChunkViewDocID() {
	// Quirk. Change this back when we figure out how to deal with
	// milestones.
	assertGTCEquals(
		"Perseus:text:1999.02.0140",
		"Perseus:text:1999.02.0140:book=41:chapter=1:section=8");
    }
    public static void testGetTextlessChunkViewSubdocID() {
	assertGTCEquals(
		"Perseus:text:1999.02.0140:view=text",
		"Perseus:text:1999.02.0140:view=text:book=41:chapter=1:section=1");
    }
    public static void testGetTextlessChunkViewSubdocText() {
	assertGTCEquals(
		"Perseus:text:1999.02.0140:view=text:book=41:chapter=1",
		"Perseus:text:1999.02.0140:view=text:book=41:chapter=1");
    }

    private static void assertGTCEquals(String input, String target) {
	try {
	    Query query = new Query(input);
	    Chunk chunk = query.getTextlessChunk();

	    String output = chunk.getQuery().toString();
	    assertEquals(output, target);
	} catch (InvalidQueryException iqe) {
	    fail();
	}
    }

    public static void testGetTOCChunkDocID() {
	assertGTocCEquals(
		"Perseus:text:1999.01.0001",
		"Perseus:text:1999.01.0001:speech=1");
    }

    public static void testGetTOCChunkSubdocID() {
	assertGTocCEquals(
		"Perseus:text:1999.01.0001:speech=1",
		"Perseus:text:1999.01.0001:speech=1");
    }

    public static void testGetTOCChunkSubdocText() {
	assertGTocCEquals(
		"Perseus:text:1999.01.0001:speech=1:section=1",
		"Perseus:text:1999.01.0001:speech=1");
    }

    public static void testGetTOCChunkNonSubdocID() {
	assertGTocCEquals(
		"Perseus:text:1999.01.0003",
		"Perseus:text:1999.01.0003");
    }
    public static void testGetTOCChunkNonSubdocText() {
	assertGTocCEquals(
		"Perseus:text:1999.01.0003:card=1",
		"Perseus:text:1999.01.0003");
    }

    public static void testGetTOCChunkLineQuery() {
	assertGTocCEquals(
		"Perseus:text:1999.01.0003:card=4",
		"Perseus:text:1999.01.0003");
    }

    public static void testGetTOCChunkViewDocID() {
	assertGTocCEquals(
		"Perseus:text:1999.02.0140",
		"Perseus:text:1999.02.0140");
    }
    public static void testGetTOCChunkViewSubdocID() {
	assertGTocCEquals(
		"Perseus:text:1999.02.0140:view=text",
		"Perseus:text:1999.02.0140:view=text");
    }
    public static void testGetTOCChunkViewSubdocText() {
	assertGTocCEquals(
		"Perseus:text:1999.01.0001:view=text:book=41:chapter=1",
		"Perseus:text:1999.01.0001:view=text");
    }

    private static void assertGTocCEquals(String input, String target) {
	Query query = new Query(input);
	Chunk chunk = Chunk.getTOCChunk(query);

	String output = chunk.getQuery().toString();
	assertEquals(output, target);
    }

}
