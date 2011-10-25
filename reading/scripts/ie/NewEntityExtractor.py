from perseus.ie import Location

from perseus.ie.entity import Entity, EntityOccurrence, EntityFrequency
from perseus.ie.entity import HibernateEntityManager

from perseus.document import DOMOffsetAdder, RecentlyModifiedCorpusProcessor

import entity_utils
import jarray

import java.lang.String

class NewEntityExtractor(RecentlyModifiedCorpusProcessor):

    def __init__(self):
	self.frequencies = {}
	self.current_query = None
	self.manager = HibernateEntityManager()
	self.entity_cache = {}

    def getRequiredTasks(self, documentChunk):
	return jarray.array(["chunkify"], java.lang.String)

    # This should never be called itself, but it's here to fulfill its
    # superclass's API.
    def getTaskName(self):
	return "extract-entities"

    def startDocument(self, documentChunk):
	print "Starting document %s" % documentChunk.getQuery()
	query = documentChunk.getQuery()

	# this is ended in endDocument
	self.manager.beginWrite()

	clearer = \
	    entity_utils.entity_clearer(query.getDocumentID(), self.manager)
	print "Clearing previous occurrences/frequencies/tuples"
	map(clearer, self.get_entity_classes())

	self.frequencies.clear()
	self.current_query = query

    def endDocument(self, documentChunk):
	RecentlyModifiedCorpusProcessor.endDocument(self, documentChunk)

	print "Calculating frequencies for %s" % documentChunk.getQuery()
	doc_id = self.current_query.getDocumentID()
	self.current_query = None
	freqs = self.frequencies

	cache = self.entity_cache

	for auth_name in freqs.keys():
	    count = freqs[auth_name]
	    entity = cache[auth_name]
	    #entity = self.manager.getEntityByAuthName(auth_name)
	    freq = EntityFrequency(entity, doc_id, count, count, count, 0)
	    #entity.addFrequency(freq)
	    self.manager.addFrequency(freq)

	# this ends the beginWrite in startDocument
	self.manager.endWrite()
	cache.clear()
	print "Done with document"

    def processChunk(self, documentChunk):
	print "Now processing chunk %s" % documentChunk.getQuery()
	self.current_query = documentChunk.getQuery()

	doc = DOMOffsetAdder.domFromChunk(documentChunk)

	print "Processing elements"
	elements = doc.getElementsByTagName("*")
	# I don't like the DOM. :(
	element_list = [elements.item(i) for i in range(elements.getLength())]
	occs_elts = filter(None, map(self.do_processing, element_list))

	print "Creating tuples for %d occurrences" % len(occs_elts)
	tuples = entity_utils.create_tuples(occs_elts, documentChunk, doc)
	print "Created %d tuples" % len(tuples)

	occurrences = [oe[0] for oe in occs_elts]

	map(self.manager.addOccurrence, occurrences)
	map(self.manager.addTuple, tuples)
	#map(self.manager.updateEntity, entities_to_update.keys())
	
	print "Recording frequencies for this chunk"
	self.record_frequencies(tuples)

    def record_frequencies(self, tuples):
	freqs = self.frequencies
	for (count, auth) in [(t.getCount(), t.getEntity().getAuthorityName())\
				for t in tuples]:
	    freqs[auth] = freqs.has_key(auth) and freqs[auth] + count or count

    def do_processing(self, element):
	func_name = "process_" + element.getNodeName()
	if hasattr(self, func_name):
	    entity = getattr(self, func_name)(element)
	    if entity != None:
		# if we've got this entity in our own cache, grab it;
		# otherwise, if it's in the database, grab that; if it's not
		# there, create it ourselves and register it
		auth_name = entity.getAuthorityName()
		if self.entity_cache.has_key(auth_name):
		    auth_entity = self.entity_cache[auth_name]
		else:
		    auth_entity = self.manager.getEntityByAuthName(auth_name)
		    if auth_entity == None:
			self.manager.registerEntity(entity)
			auth_entity = entity
		    self.entity_cache[auth_name] = auth_entity
		return (self.create_occurrence(auth_entity, element), element)
	return None

    def create_occurrence(self, entity, elt):
	byte_offset = elt.getUserData("perseus:start_offset")
	display_text = elt.getTextContent()

	loc = Location(self.current_query, byte_offset)
	return EntityOccurrence(entity, loc, display_text)

    def get_entity_classes(self):
	return (Entity)

    def reportException(self, chunk, e):
	e.printStackTrace()
