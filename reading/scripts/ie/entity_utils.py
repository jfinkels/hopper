import math.fabs

import org.xml.sax as sax
import org.apache.xerces.parsers.DOMParser as DOMParser

from org.w3c.dom import Node

from perseus.ie.entity import EntityTuple

from time import clock

snippeted_document = None
element_starts = {}
element_ends = {}

"""
String concatenation is Python, as in Java, is horrifyingly slow, and Python
has no StringBuffer class: it does have lists, though, which can effectively be
used like a StringBuffer. Thus, we use a list to keep track of our document
text.
"""
document_text = []

# How many occurrences, at most, in a particular snippet?
max_elements = 10
total_snippet_length = 1000

def process_snippet_chunk(document):
    snippeted_document = document
    element_starts = {}
    element_ends = {}
    del document_text[:]
    process_snippet_chunk_traverse(
	    document.getDocumentElement(), document_text)

def process_snippet_chunk_traverse(element, document_text):
    element_starts[element] = len(document_text)
    children = children_as_list(element)
    if len(children) == 0:
	document_text.extend([z for z in element.getTextContent()])
    else:
	for c in children_as_list(element):
	    process_snippet_chunk_traverse(c, document_text)
    element_ends[element] = len(document_text)

def extract_snippet(elements, length_per_element):
    elements.sort(lambda x, y: element_starts[x]-element_starts[y])
    last_end = 0

    snippet_text = []

    for elt in elements:
	start, end = element_starts[elt], element_ends[elt]
	space_left = length_per_element - (end - start)
	left_bound = start - space_left/2
	right_bound = end + space_left/2

	if left_bound <= last_end:
	    right_bound += last_end - left_bound
	    left_bound += last_end - left_bound
	if left_bound < 0:
	    right_bound += -left_bound
	    left_bound = 0
	if right_bound > len(document_text):
	    left_bound -= right_bound - len(document_text)
	    right_bound = len(document_text)

	if left_bound > last_end:
	    snippet_text.extend([z for z in "<gap />"])

	snippet_text.extend(document_text[left_bound:start])
	snippet_text.extend([z for z in "<span class=\"match\">"])
	snippet_text.extend(document_text[start:end])
	snippet_text.extend([z for z in "</span>"])
	snippet_text.extend(document_text[end:right_bound])

	last_end = right_bound

    if last_end < len(document_text):
	snippet_text.extend([z for z in "<gap />"])
    snippet = "".join(snippet_text)

    return snippet

def create_snippet(elements):
    total_elements = len(elements)
    if total_elements > max_elements:
	elements = elements[0:max_elements]
    length_per_element = total_snippet_length / len(elements)
    snippet = extract_snippet(elements, length_per_element)

    if total_elements > max_elements:
	snippet += "<gap /><hi rend=\"ital\">[%d more...]</hi>" % \
	    (total_elements - max_elements)

    return snippet

def create_tuples(occs_elements, chunk, document):
    entity_ids = {}
    query = chunk.getQuery()
    tuples = []
    entities = []
    entity_elts = {}

    for oe_pair in occs_elements:
	occurrence = oe_pair[0]
	element = oe_pair[1]
	entity = occurrence.getEntity()
	auth_name = entity.getAuthorityName()

	if not(entity_ids.has_key(auth_name)):
	    entity_ids[auth_name] = 1
	    entities.append(entity)
	    entity_elts[auth_name] = []

	entity_elts[auth_name].append(element)

    occurrences = [oe[0] for oe in occs_elements]

    process_snippet_chunk(document)
    count = 0
    print "%d unique entities" % len(entities)
    for e in entities:
	matching_elts = entity_elts[e.getAuthorityName()]
	first_pos = min([o.getLocation().getPosition() for o in occurrences \
				    if e == o.getEntity()])
	my_auth_name = e.getAuthorityName()

	matching_occs = [o for o in occurrences if \
			    o.getEntity().getAuthorityName() == my_auth_name]
	snippet_text = create_snippet(matching_elts)

	tuple = EntityTuple(e, query, len(matching_occs), \
			    first_pos, snippet_text)
	tuples.append(tuple)
	count += 1
	if count % 50 == 0:
	    print "Created #%d" % count
    return tuples

def children_as_list(parent):
    children = parent.getChildNodes()
    return [children.item(i) for i in range(children.getLength())]

def entity_clearer(doc_id, mgr):
    entity_clearer.manager = mgr
    entity_clearer.id = doc_id
    def clear_entities(entity_type):
	entity_clearer.manager.clearReferences(entity_clearer.id, entity_type)
    return clear_entities
